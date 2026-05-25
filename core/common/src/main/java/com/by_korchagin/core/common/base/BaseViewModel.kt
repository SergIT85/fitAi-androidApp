package com.by_korchagin.core.common.base

import com.by_korchagin.core.common.logger.MyLogger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Base ViewModel for MVI architecture.
 *
 * Completely platform-independent — ready for KMP common module.
 * Uses Kotlin Coroutines exclusively.
 *
 * Core principles:
 * - Unidirectional Data Flow (UDF)
 * - Single Source of Truth (StateFlow)
 * - Immutable State
 * - Side Effects via SingleEvent
 *
 * Thread Safety: All methods are thread-safe.
 * Testing: Override createScope() to use TestDispatcher.
 *
 * @param VIEW_STATE Type of the UI state (must be an immutable data class).
 */
abstract class BaseViewModel<VIEW_STATE : Any> {

    private val viewModelName: String = this::class.simpleName ?: "BaseViewModel"

    /**
     * Job for managing coroutine lifecycle
     */
    private val viewModelJob = SupervisorJob()

    /**
     * CoroutineExceptionHandler for catching unhandled exceptions
     */
    private val exceptionHandler = CoroutineExceptionHandler{ _, throwable ->
        log { e(viewModelName, "unhandled exceptions", throwable) }
        handleUnexpectedException(throwable)
    }

    /**
     * Coroutine scope for the ViewModel.
     * - Runs on the Dispatchers.Main by default.
     *
     * Override this to:
     * - Provide a TestDispatcher for testing purposes
     * - Configure a custom context
     *
     * Example for tests:
     * ```
     * override fun createScope() = CoroutineScope(
     *     testDispatcher + viewModelJob + exceptionHandler
     * )
     * ```
     */
    protected open fun createScope(): CoroutineScope {
        return CoroutineScope(Dispatchers.Main + viewModelJob + exceptionHandler)
    }

    private val _vmScope by lazy { createScope() }

    /**
     * Scope for launching coroutines.
     * Use launch { } for asynchronous operations.
     */
    protected val vmScope: CoroutineScope
        get() = _vmScope

    /**
     * StateFlow for the UI state - Single Source of Truth.
     * Thread-safe, always holds a value, and survives configuration changes.
     */
    private val _viewState: MutableStateFlow<VIEW_STATE> by lazy {
        MutableStateFlow((initialViewState()))
    }
    val viewState: StateFlow<VIEW_STATE> get() = _viewState.asStateFlow()

    /**
     * Channel for single/one-time events (side effects)
     *
     * IMPORTANT:
     * - Buffer is limited (64 events)
     * - On overflow, new events will SUSPEND the sender
     * - Unprocessed events may be lost when the Activity is recreated
     * - This is the expected behavior for one-time events
     */
    private val _singleEvent: Channel<SingleEvent> = Channel(Channel.BUFFERED)
    val singleEvent = _singleEvent.receiveAsFlow()

    /**
     * Current state (for convenience in reduce functions)
     */
    protected val currentState: VIEW_STATE
        get() = viewState.value

    init {
        log { d(viewModelName, "ViewModel init") }
    }

    // Abstract methods

    /**
     * Initial UI state
     * Called once when the ViewModel is created
     */
    protected abstract fun initialViewState(): VIEW_STATE

    /**
     * Reducer - a pure function for creating a new state
     *
     * IMPORTANT:
     * - Must be a PURE function (no side effects)
     * - Must not call suspend functions
     * - Must be fast (no heavy computations)
     * - For side effects, use sendSingleEvent() or launch {}
     *
     * @param event The event to process
     * @return The new state
     */
    protected abstract fun reduce(event: Event): VIEW_STATE

    /**
     * Processing of error events
     * Called when handleCommonError returns false
     *
     * @param event The error event
     * @return The new state with the processed error
     */
    protected abstract fun onHandleErrorEvent(event: ErrorEvent): VIEW_STATE

    // ========== Optional methods ==========

    /**
     * Callback after state change
     * Use this for side effects that depend on the new state
     *
     * IMPORTANT: Do not modify the state directly inside this method!
     */
    protected open fun onAfterStateChanged(newViewState: VIEW_STATE, event: Event) {
        //Override if needed
    }

    /**
     * Handling of uncaught exceptions from coroutines
     * Override for custom processing (e.g., sending to Crashlytics)
     */
    protected open fun handleUnexpectedException(throwable: Throwable) {
        // Default: just logged. Override for custom handling
    }

    /**
     * Global handling of common/typical errors
     *
     * @param error The exception
     * @return true if the error was handled globally, false for custom processing
     */
    protected open fun handleCommonError(error: Throwable): Boolean {
        return false
    }

    /**
     * Called when the ViewModel is cleared
     * Override to clear/release resources
     *
     * IMPORTANT:
     * - On Android, with androidx.lifecycle.ViewModel, this is called automatically
     * - In pure KMP, call clear() manually when the screen is destroyed
     */
    protected open fun onCleared() {
        log {d(viewModelName, "ViewModel clearing")}
    }

    /**
     * Logging configuration
     */
    protected open fun log(block: Logger.() -> Unit) {
        MyLogger.block()
    }

    // ========== Public methods ==========

    /**
     * Direct state update via DSL
     * Use for simple changes without creating an Event
     *
     * Example:
     * ```
     * updateState { copy(isLoading = true) }
     * ```
     */
    protected fun updateState(update: VIEW_STATE.() -> VIEW_STATE) {
        val newState =_viewState.update(update)
        log { d(viewModelName, "State updated DIRECTLY via DSL -> New State: $newState") }
    }

    /**
     * Processing of UI events (clicks, text input, etc.)
     * Call this from the UI layer
     */
    fun processUiEvent(event: UiEvent) {
        log { d(viewModelName, "UI Event: ${event::class.simpleName}") }
        updateState(event)
    }
    /**
     * Processing of Data events (results from domain/data layer)
     */
    protected fun processDataEvent(event: DataEvent) {
        log { d(viewModelName, "Data vent: ${event::class.simpleName}") }
        updateState(event)
    }

    /**
     * Events for inter-module communication
     */
    protected fun processOutputEvent(event: OutputEvent) {
        log { d(viewModelName, "Output event: ${event::class.simpleName}") }
        updateState(event)
    }

    /**
     * Error event processing
     */

    protected fun processErrorEvent(errorEvent: ErrorEvent) {
        log { e(
            viewModelName,
            "Error event ${errorEvent.error.message}", errorEvent.error
        ) }

        val newViewState = if(handleCommonError(errorEvent.error)) {
            log { d(viewModelName, "Global handling error") }
            currentState
        } else {
            log { d(viewModelName, "Custom handling error") }
            onHandleErrorEvent(errorEvent)
        }
        compareNewStateWithCurrentAndUpdate(newViewState, errorEvent)
    }

    /**
     * Sends a single event for side effects.
     *
     * Thread-safe, non-blocking call (events are sent within a coroutine).
     *
     * IMPORTANT:
     * - If the buffer is full (>64 events), the event will be postponed.
     * - Events may be lost during Activity recreation.
     * - This is expected behavior for one-time events.
     *
     * Usage examples:
     * - Navigation
     * - Showing dialogs
     * - Showing Toast/SnackBar
     *
     * @param event The event to be sent.
     */
    protected fun sentSingleEvent(event: SingleEvent) {
        log { d(viewModelName, "SingleEvent ${event::class.simpleName}") }
        vmScope.launch {
            _singleEvent.send(event)
        }
    }

    /**
     * Safe launch within the ViewModel scope.
     */
    protected fun launch(block: suspend CoroutineScope.() -> Unit) {
        vmScope.launch(block = block)
    }


    // ========== Private methods ==========

    /**
     * Private method for the actual StateFlow update.
     * No logs here to prevent duplicate log messages.
     */
    private fun commitState(update: VIEW_STATE.() -> VIEW_STATE) {
        _viewState.update(update)
    }

    private fun updateState(event: Event) {
        log { d(viewModelName, "Updating state:: ${event::class.simpleName}") }
        val newViewState = reduce(event)
        compareNewStateWithCurrentAndUpdate(newViewState, event)
    }

    private fun compareNewStateWithCurrentAndUpdate(newViewState: VIEW_STATE, event: Event) {
        if (newViewState != currentState) {
            log { d(viewModelName, "State updated") }
            _viewState.update { newViewState }
            onAfterStateChanged(newViewState, event)
        } else {
            log { d(viewModelName, "State not updated") }
        }
    }
}

// ========== Interfaces ==========

/**
 * Marker interface for UI state
 * All ViewState implementations must be immutable data classes
 */
interface ViewState

/**
 * Base interface for oll Events
 */
interface Event

/**
 * Single events for side effects
 */
interface SingleEvent

/**
 * Event from the UI layer
 */
interface UiEvent : Event

/**
 * Internal ViewModel event
 */
interface DataEvent : Event

/**
 * Events for inter-module communication
 */
interface OutputEvent : Event

/**
 * Error events
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