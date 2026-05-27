package com.by_korchagin.core.common.base

/**
 * Execute block and handle errors automatically.
 *
 * Usage:
 * ```
 * launch {
 *     executeSafely(
 *         action = { repository.loadData() },
 *         onSuccess = { data -> processDataEvent(DataLoaded(data)) },
 *         onError = { e -> processErrorEvent(LoadError(e)) }
 *     )
 * }
 * ```
 */
suspend fun <T> BaseViewModel<*>.executeSafuly(
    action: suspend () -> T,
    onSuccess:(T) -> Unit,
    onError: (Throwable) -> Unit
) {
    try{
        val result = action()
        onSuccess(result)
    } catch (e: Throwable) {
        onError(e)
    }
}