package core.playground

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

/**
 * delay to collect latest event,
 * usually use this to handle rapid clicks
 */
suspend fun <T> Flow<T>.throttledCollectLatest(
    delay: Long = 200L,
    action: suspend (value: T) -> Unit,
) = collectLatest {
    delay(delay)
    action(it)
}
