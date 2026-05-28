package com.by_korchagin.core.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Base ViewModel for MVI architecture with full KMP support.
 *
 * Built on top of androidx.lifecycle.ViewModel (2.8+) which provides:
 * - Automatic lifecycle management
 * - Platform-specific dispatchers (Main on Android/iOS)
 * - Automatic cancellation on onCleared()
 *
 * This base class adds:
 * - Unidirectional Data Flow (UDF)
 * - Single Source of Truth via StateFlow
 * - Side effects via Channel (SingleEvent)
 * - Automatic exception handling
 * - Testability (dispatcher override)
 *
 * Architecture:
 * ```
 * UI Layer → processUiEvent() → reduce() → StateFlow → UI Layer
 *                                    ↓
 *                              Side effects (launch, sendSingleEvent)
 * ```
 *
 * Thread Safety: All public methods are thread-safe.
 * Testing: Override createCoroutineContext() to inject TestDispatcher.
 *
 * @param VIEW_STATE Type of UI state (must be immutable data class)
 *
 * Example:
 * ```
 * class MyViewModel : BaseViewModel<MyViewState>() {
 *
 *     override fun initialViewState() = MyViewState(
 *         isLoading = false,
 *         data = emptyList()
 *     )
 *
 *     override fun reduce(event: Event): MyViewState {
 *         return when (event) {
 *             is MyUiEvent.LoadData -> {
 *                 loadData() // side-effect
 *                 currentState.copy(isLoading = true)
 *             }
 *             is MyDataEvent.DataLoaded -> {
 *                 currentState.copy(isLoading = false, data = event.data)
 *             }
 *             else -> currentState
 *         }
 *     }
 *
 *     override fun onHandleErrorEvent(event: ErrorEvent): MyViewState {
 *         sendSingleEvent(MessageEvent.ShowError(event.error.message))
 *         return currentState.copy(isLoading = false)
 *     }
 *
 *     private fun loadData() {
 *         launch {
 *             try {
 *                 val data = repository.getData()
 *                 processDataEvent(MyDataEvent.DataLoaded(data))
 *             } catch (e: Exception) {
 *                 processErrorEvent(MyErrorEvent(e))
 *             }
 *         }
 *     }
 * }
 * ```
 */
@Suppress("TooManyFunctions")
abstract class BaseViewModel<VIEW_STATE : Any> : ViewModel() {

    private val viewModelName: String = this::class.simpleName ?: "BaseViewModel"

    /**
     * CoroutineExceptionHandler for catching unhandled exceptions in coroutines.
     *
     * Why needed:
     * - Without it: Unhandled exception in launch {} crashes the app
     * - With it: Exception is logged and handled via handleUnexpectedException()
     *
     * Critical for production stability!
     */
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        log { e(viewModelName, "unhandled exceptions", throwable) }
        handleUnexpectedException(throwable)
    }

    /**
     * Creates additional coroutine context for testing purposes.
     *
     * IMPORTANT FOR TESTING!
     * Override in tests to inject TestDispatcher:
     *
     * ```
     * // In test
     * class TestViewModel : BaseViewModel<State>() {
     *     override fun createCoroutineContext() = testDispatcher
     * }
     * ```
     *
     * Production: Returns EmptyCoroutineContext (uses platform dispatcher from viewModelScope)
     *
     * @return CoroutineContext (usually a Dispatcher for tests)
     */
    protected open fun createCoroutineContext(): CoroutineContext {
        return EmptyCoroutineContext
    }

    /**
     * Safe CoroutineScope with automatic exception handling.
     *
     * Composition:
     * - viewModelScope (from androidx.lifecycle)
     *   - Already contains SupervisorJob (error isolation)
     *   - Already contains platform-specific dispatcher (Main.immediate)
     *   - Automatically cancelled in onCleared()
     * - createCoroutineContext() (custom dispatcher for tests)
     * - exceptionHandler (crash prevention)
     *
     * Why this approach:
     * ✅ Leverages existing viewModelScope infrastructure
     * ✅ No manual cancellation needed (automatic)
     * ✅ No hardcoded Dispatchers.Main (platform-independent)
     * ✅ Testable via createCoroutineContext()
     * ✅ Exception-safe via exceptionHandler
     */
    protected val safeScope: CoroutineScope by lazy {
        viewModelScope + createCoroutineContext() + exceptionHandler
    }

    /**
     * StateFlow for UI state - Single Source of Truth.
     *
     * Characteristics:
     * - Thread-safe: Can be updated from any thread
     * - Hot flow: Always has current value
     * - Survives configuration changes (thanks to ViewModel)
     * - Distinct values: Only emits when state actually changes
     *
     * Why lazy: Allows initialViewState() to access child class properties
     */
    private val _viewState: MutableStateFlow<VIEW_STATE> by lazy {
        MutableStateFlow((initialViewState()))
    }

    /**
     * Public read-only StateFlow for UI layer.
     * Collect in UI to receive state updates.
     *
     * Example:
     * ```
     * // In Compose
     * val state by viewModel.viewState.collectAsState()
     * ```
     */
    val viewState: StateFlow<VIEW_STATE> by lazy {
        _viewState.asStateFlow()
    }

    /**
     * Channel for one-time events (side effects).
     *
     * Configuration:
     * - Capacity: 64 events (BUFFERED)
     * - Overflow: DROP_OLDEST (prevents memory leaks)
     *
     * Why Channel, not SharedFlow:
     * ✅ Events must be consumed EXACTLY ONCE
     * ✅ Unprocessed events should be lost on UI recreation (correct behavior)
     * ✅ Simpler API than SharedFlow
     *
     * Why DROP_OLDEST:
     * ✅ Prevents sender blocking if UI is slow
     * ✅ Prevents memory leaks if events accumulate
     * ✅ Most recent events are more relevant than old ones
     *
     * Use cases:
     * - Navigation commands
     * - Show dialogs/snackbars
     * - One-time animations
     * - Any non-state side effects
     *
     * IMPORTANT: Events may be lost during configuration changes - this is CORRECT!
     */
    private val _singleEvent: Channel<SingleEvent> = Channel(
        Channel.BUFFERED,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    /**
     * Public Flow for consuming one-time events.
     *
     * Example:
     * ```
     * // In Compose
     * LaunchedEffect(Unit) {
     *     viewModel.singleEvent.collect { event ->
     *         when (event) {
     *             is NavigationEvent -> navController.navigate(event.route)
     *             is MessageEvent -> showSnackbar(event.message)
     *         }
     *     }
     * }
     * ```
     */
    val singleEvent = _singleEvent.receiveAsFlow()

    /**
     * Current state accessor (convenience for reduce functions).
     *
     * Thread-safe, always returns actual current value.
     */
    protected val currentState: VIEW_STATE
        get() = viewState.value

    init {
        log { d(viewModelName, "ViewModel initialized") }
    }

    // ========== Abstract Methods (must be implemented) ==========

    /**
     * Provides initial UI state.
     *
     * Called once during ViewModel creation (lazy initialization).
     *
     * Requirements:
     * - Must return immutable data class
     * - Should represent empty/default state
     * - Must not trigger side effects
     *
     * Example:
     * ```
     * override fun initialViewState() = MyViewState(
     *     isLoading = false,
     *     data = emptyList(),
     *     error = null
     * )
     * ```
     */
    protected abstract fun initialViewState(): VIEW_STATE

    /**
     * Reducer - pure function for state transitions.
     *
     * CRITICAL RULES:
     * ✅ Must be PURE (same input → same output)
     * ✅ Must NOT call suspend functions
     * ✅ Must be FAST (no heavy computations)
     * ✅ Must NOT have side effects directly
     * ❌ Do NOT update StateFlow directly here
     *
     * For side effects:
     * - Use launch {} for async operations
     * - Use sendSingleEvent() for one-time actions
     * - Call them before returning new state
     *
     * Pattern:
     * ```
     * override fun reduce(event: Event): MyViewState {
     *     return when (event) {
     *         is MyUiEvent.LoadData -> {
     *             loadData() // ← side effect (launches coroutine)
     *             currentState.copy(isLoading = true) // ← new state
     *         }
     *         is MyDataEvent.Success -> {
     *             currentState.copy(isLoading = false, data = event.data)
     *         }
     *         is MyDataEvent.Error -> {
     *             sendSingleEvent(ShowErrorDialog(event.error)) // ← side effect
     *             currentState.copy(isLoading = false)
     *         }
     *         else -> currentState // ← no change
     *     }
     * }
     * ```
     *
     * @param event Event to process
     * @return New state (or currentState if no changes)
     */
    protected abstract fun reduce(event: Event): VIEW_STATE

    /**
     * Handles error events after global error handling.
     *
     * Called when handleCommonError() returns false (error not handled globally).
     *
     * Responsibilities:
     * - Update state with error information
     * - Send SingleEvent to show error to user
     * - Reset loading flags
     *
     * Example:
     * ```
     * override fun onHandleErrorEvent(event: ErrorEvent): MyViewState {
     *     val message = when (event.error) {
     *         is NetworkException -> "No internet connection"
     *         is ValidationException -> event.error.message
     *         else -> "Unknown error occurred"
     *     }
     *
     *     sendSingleEvent(MessageEvent.ShowSnackbar(message))
     *
     *     return currentState.copy(
     *         isLoading = false,
     *         error = message
     *     )
     * }
     * ```
     *
     * @param event Error event containing throwable
     * @return New state reflecting error condition
     */
    protected abstract fun onHandleErrorEvent(event: ErrorEvent): VIEW_STATE

    // ========== Optional Override Methods ==========

    /**
     * Callback invoked after state change.
     *
     * Use cases:
     * - Analytics tracking
     * - Logging state transitions
     * - Complex side effects depending on new state
     *
     * IMPORTANT:
     * ❌ Do NOT modify state directly inside this method (causes recursion)
     * ✅ OK to launch new coroutines or send events
     *
     * Example:
     * ```
     * override fun onAfterStateChanged(newViewState: MyViewState, event: Event) {
     *     if (newViewState.isLoggedIn && !currentState.isLoggedIn) {
     *         analyticsTracker.trackLogin()
     *         sendSingleEvent(NavigationEvent.NavigateToHome)
     *     }
     * }
     * ```
     *
     * @param newViewState New state that was just set
     * @param event Event that triggered the change
     */
    protected open fun onAfterStateChanged(newViewState: VIEW_STATE, event: Event) {
        // Override if needed
    }

    /**
     * Handles uncaught exceptions from coroutines.
     *
     * Called by CoroutineExceptionHandler when exception escapes all try-catch blocks.
     *
     * Default: Just logs (exception already caught, app won't crash)
     *
     * Override for:
     * - Sending to crash reporting (Crashlytics, Sentry)
     * - Showing fallback UI
     * - Specific recovery logic
     *
     * Example:
     * ```
     * override fun handleUnexpectedException(throwable: Throwable) {
     *     Crashlytics.recordException(throwable)
     *     sendSingleEvent(MessageEvent.ShowError("Something went wrong"))
     *     updateState { copy(isLoading = false) } // reset loading state
     * }
     * ```
     *
     * @param throwable Uncaught exception
     */
    protected open fun handleUnexpectedException(throwable: Throwable) {
        // Default: just logged by exceptionHandler
        // Override for custom handling (e.g., Crashlytics)
    }

    /**
     * Global error handling for common error types.
     *
     * Use for app-wide error handling logic:
     * - Network errors (no connection)
     * - Authentication errors (session expired)
     * - Validation errors
     *
     * Pattern:
     * ```
     * override fun handleCommonError(error: Throwable): Boolean {
     *     return when (error) {
     *         is NetworkException -> {
     *             sendSingleEvent(MessageEvent.ShowNoInternetDialog)
     *             updateState { copy(isLoading = false) }
     *             true // handled globally
     *         }
     *         is UnauthorizedException -> {
     *             sendSingleEvent(NavigationEvent.NavigateToLogin)
     *             true // handled globally
     *         }
     *         else -> false // needs custom handling via onHandleErrorEvent
     *     }
     * }
     * ```
     *
     * @param error Exception to handle
     * @return true if handled globally, false to delegate to onHandleErrorEvent()
     */
    protected open fun handleCommonError(error: Throwable): Boolean {
        return false
    }

    /**
     * Logging configuration.
     *
     * Default: No-op (logs disabled for production)
     *
     * Override to enable logging:
     * ```
     * override fun log(block: Logger.() -> Unit) {
     *     if (BuildConfig.DEBUG) {
     *         PlatformLogger.block() // expect/actual implementation
     *     }
     * }
     * ```
     *
     * Or always log (development builds):
     * ```
     * override fun log(block: Logger.() -> Unit) {
     *     PlatformLogger.block()
     * }
     * ```
     */
    protected open fun log(block: Logger.() -> Unit) {
        // No-op by default (production-safe)
        // Override to enable logging in debug builds
    }

    // ========== Public API ==========

    /**
     * Processes UI events (clicks, text input, swipes, etc.).
     *
     * Call from UI layer for all user interactions.
     *
     * Thread-safe, can be called from any thread.
     *
     * Example:
     * ```
     * // In Compose
     * Button(onClick = { viewModel.processUiEvent(MyUiEvent.ButtonClicked) }) {
     *     Text("Click me")
     * }
     *
     * TextField(
     *     value = state.query,
     *     onValueChange = { viewModel.processUiEvent(MyUiEvent.QueryChanged(it)) }
     * )
     * ```
     *
     * @param event UI event to process
     */
    fun processUiEvent(event: UiEvent) {
        log { d(viewModelName, "UI event: ${event::class.simpleName}") }
        updateState(event)
    }

    // ========== Protected API (for child ViewModels) ==========
    /**
     * Processes Data events (results from domain/data layer).
     *
     * Call after receiving data from use cases or repositories.
     *
     * Example:
     * ```
     * private fun loadData() {
     *     launch {
     *         val result = loadDataUseCase()
     *         processDataEvent(MyDataEvent.DataLoaded(result))
     *     }
     * }
     * ```
     *
     * @param event Data event to process
     */
    protected fun processDataEvent(event: DataEvent) {
        log { d(viewModelName, "Data event: ${event::class.simpleName}") }
        updateState(event)
    }

    /**
     * Processes Output events (inter-screen communication).
     *
     * Use for events from other ViewModels or screens.
     *
     * Example:
     * ```
     * // DetailsViewModel sends result back
     * fun onItemSelected(item: Item) {
     *     processOutputEvent(OutputEvent.ItemSelected(item))
     * }
     * ```
     *
     * @param event Output event to process
     */
    protected fun processOutputEvent(event: OutputEvent) {
        log { d(viewModelName, "Output Event: ${event::class.simpleName}") }
        updateState(event)
    }

    /**
     * Processes error events.
     *
     * Automatically:
     * 1. Tries handleCommonError() for global handling
     * 2. If not handled → calls onHandleErrorEvent() for custom handling
     * 3. Updates state accordingly
     *
     * Example:
     * ```
     * private fun loadData() {
     *     launch {
     *         try {
     *             val data = repository.getData()
     *             processDataEvent(MyDataEvent.DataLoaded(data))
     *         } catch (e: Exception) {
     *             processErrorEvent(MyErrorEvent(e))
     *         }
     *     }
     * }
     * ```
     *
     * @param errorEvent Error event containing throwable
     */
    protected fun processErrorEvent(errorEvent: ErrorEvent) {
        log { e(viewModelName, "Error event:${errorEvent.error.message}", errorEvent.error) }
        val newViewState = if (handleCommonError(errorEvent.error)) {
            log { d(viewModelName, "Error handled globally") }
            currentState
        } else {
            log { d(viewModelName, "Custom error handling") }
            onHandleErrorEvent(errorEvent)
        }

        compareAndUpdate(newViewState, errorEvent)
    }

    /**
     * Sends one-time event for side effects.
     *
     * Thread-safe, non-blocking (buffered with DROP_OLDEST strategy).
     *
     * Behavior:
     * - If buffer full (>64 events): Oldest event dropped, new one added
     * - If UI not collecting: Events accumulate up to buffer size
     * - On UI recreation: Unprocessed events are lost (CORRECT behavior)
     *
     * Use cases:
     * - Navigation: sendSingleEvent(NavigationEvent.Navigate("details"))
     * - Dialogs: sendSingleEvent(ShowDialog("Confirm delete?"))
     * - Snackbars: sendSingleEvent(ShowSnackbar("Saved successfully"))
     * - Toasts: sendSingleEvent(ShowToast("Item added"))
     *
     * Example:
     * ```
     * private fun onDeleteClicked() {
     *     launch {
     *         repository.deleteItem()
     *         sendSingleEvent(NavigationEvent.NavigateBack)
     *         sendSingleEvent(MessageEvent.ShowSnackbar("Item deleted"))
     *     }
     * }
     * ```
     *
     * @param event One-time event to send
     */
    protected fun sendSingleEvent(event: SingleEvent) {
        log { d(viewModelName, "Single event:${event::class.simpleName}") }
        safeScope.launch {
            _singleEvent.send(event)
        }
    }

    /**
     * Direct state update via DSL (bypasses Event system).
     *
     * Use for simple state changes without creating dedicated events.
     *
     * Thread-safe.
     *
     * Example:
     * ```
     * // Simple flag toggle
     * updateState { copy(isDialogVisible = true) }
     *
     * // Multiple changes
     * updateState {
     *     copy(
     *         isLoading = false,
     *         selectedTab = 1
     *     )
     * }
     * ```
     *
     * When to use:
     * ✅ Internal ViewModel state changes
     * ✅ Simple flag toggles
     * ✅ Temporary UI state
     *
     * When NOT to use:
     * ❌ Complex business logic (use Events instead)
     * ❌ State changes from UI (use processUiEvent)
     * ❌ When you need event tracking/logging
     *
     * @param update Lambda to transform current state
     */
    protected fun updateState(update: VIEW_STATE.() -> VIEW_STATE) {
        _viewState.update(update)
        log { d(viewModelName, "State updated directly via DSL") }
    }

    /**
     * Launches coroutine in safe scope.
     *
     * Automatically:
     * - Cancelled when ViewModel is cleared
     * - Catches unhandled exceptions via exceptionHandler
     * - Isolated from other coroutines via SupervisorJob
     *
     * ALWAYS USE THIS instead of viewModelScope.launch for safety!
     *
     * Example:
     * ```
     * launch {
     *     val data = repository.getData() // suspend function
     *     processDataEvent(MyDataEvent.DataLoaded(data))
     * }
     *
     * // Multiple operations
     * launch {
     *     updateState { copy(isLoading = true) }
     *     try {
     *         val data = repository.getData()
     *         processDataEvent(MyDataEvent.Success(data))
     *     } catch (e: Exception) {
     *         processErrorEvent(MyErrorEvent(e))
     *     }
     * }
     * ```
     *
     * @param block Coroutine code block
     */
    protected fun launch(block: suspend CoroutineScope.() -> Unit) {
        safeScope.launch(block = block)
    }

    // ========== Private Methods ==========
    /**
     * Internal method: Updates state via reduce() and triggers callbacks.
     */
    private fun updateState(event: Event) {
        log { d(viewModelName, "Processing event: ${event::class.simpleName}") }
        val newViewState = reduce(event)
        compareAndUpdate(newViewState, event)
    }

    /**
     * Internal method: Compares states and updates if changed.
     */
    private fun compareAndUpdate(newViewState: VIEW_STATE, event: Event) {
        if (newViewState != currentState) {
            log { d(viewModelName, "State changed, updating...") }
            _viewState.update { newViewState }
            onAfterStateChanged(newViewState, event)
        } else {
            log { d(viewModelName, "State unchanged, skipping update") }
        }
    }

    /**
     * Lifecycle callback - called when ViewModel is destroyed.
     *
     * viewModelScope (and thus safeScope) is automatically cancelled.
     * No manual cleanup needed!
     */
    override fun onCleared() {
        super.onCleared()
        log { d(viewModelName, "ViewModel cleared") }
        // Note: viewModelScope automatically cancels all coroutines
        // safeScope is derived from viewModelScope, so it's also cancelled
    }
}

// ========== Interfaces ==========

/**
 * Marker interface for UI state.
 *
 * Requirements:
 * - Must be immutable (data class with val properties)
 * - Should implement equals/hashCode (data class does this)
 * - Should not contain mutable collections
 *
 * Example:
 * ```
 * data class MyViewState(
 *     val isLoading: Boolean,
 *     val data: List<Item>, // ← List is immutable interface
 *     val error: String?
 * ) : ViewState
 * ```
 */
interface ViewState

/**
 * Base interface for oll Events
 */
interface Event

/**
 * One-time events for side effects (not part of state).
 *
 * Should contain commands, not data.
 *
 * Examples:
 * ```
 * sealed interface MySingleEvent : SingleEvent {
 *     data class Navigate(val route: String) : MySingleEvent
 *     data class ShowSnackbar(val message: String) : MySingleEvent
 *     data class ShowDialog(val title: String, val message: String) : MySingleEvent
 *     object NavigateBack : MySingleEvent
 * }
 * ```
 */
interface SingleEvent

/**
 * Events from UI layer (user interactions).
 *
 * Examples:
 * ```
 * sealed interface MyUiEvent : UiEvent {
 *     object ButtonClicked : MyUiEvent
 *     data class TextChanged(val text: String) : MyUiEvent
 *     data class ItemSelected(val id: String) : MyUiEvent
 * }
 * ```
 */
interface UiEvent : Event

/**
 * Internal ViewModel events (from domain/data layer).
 *
 * Examples:
 * ```
 * sealed interface MyDataEvent : DataEvent {
 *     data class DataLoaded(val data: List<Item>) : MyDataEvent
 *     object CacheCleared : MyDataEvent
 * }
 * ```
 */
interface DataEvent : Event

/**
 * Events for inter-screen communication.
 *
 * Examples:
 * ```
 * sealed interface MyOutputEvent : OutputEvent {
 *     data class ItemSelected(val item: Item) : MyOutputEvent
 *     object ResultConfirmed : MyOutputEvent
 * }
 * ```
 */
interface OutputEvent : Event

/**
 * Error events.
 *
 * Must contain the throwable cause.
 *
 * Example:
 * ```
 * data class MyErrorEvent(
 *     override val error: Throwable,
 *     val context: String? = null
 * ) : ErrorEvent
 * ```
 */
interface ErrorEvent : Event {
    val error: Throwable
}

interface Logger {
    fun d(tag: String, message: String)
    fun e(tag: String, message: String, throwable: Throwable? = null)
    fun w(tag: String, message: String)
    fun i(tag: String, message: String)
}
