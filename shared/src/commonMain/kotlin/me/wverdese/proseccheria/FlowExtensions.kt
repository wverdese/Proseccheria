package me.wverdese.proseccheria

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Needed to use flows from Swift.
 */
fun <T> Flow<T>.collect(
    scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main),
    onEach: (T) -> Unit,
    onCompletion: (cause: Throwable?) -> Unit
): Cancellable {
    scope.launch {
        try {
            collect {
                onEach(it)
            }

            onCompletion(null)
        } catch (e: Throwable) {
            onCompletion(e)
        }
    }

    return object : Cancellable {
        override fun cancel() {
            scope.cancel()
        }
    }
}

interface Cancellable {
    fun cancel()
}
