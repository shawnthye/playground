package core.playground.domain

import core.playground.domain.Result.Error
import core.playground.domain.Result.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out R>(open val data: R?) {

    data class Success<out T>(override val data: T) : Result<T>(data)

    data class Loading<out T>(override val data: T? = null) : Result<T>(data)

    data class Error<out T>(
        val throwable: Throwable,
        override val data: T? = null,
    ) : Result<T>(data)

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$throwable][message=${throwable.message}][data=$data]"
            is Loading -> "Loading[data=$data]"
        }
    }
}

/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
val Result<*>.succeeded
    get() = this is Success && data != null

fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Success<T>)?.data ?: fallback
}

// val <T> Result<T>.data: T?
//     get() = (this as? Success)?.data

/**
 * Updates value of [MutableStateFlow] if [Result] is of type [Success]
 */
inline fun <reified T> Result<T>.updateOnSuccess(stateFlow: MutableStateFlow<T>) {
    if (this is Success) {
        stateFlow.value = data
    }
}

inline fun <reified T, R> Flow<Result<T>>.mapLatestError(
    crossinline transform: suspend (throwable: Throwable) -> R,
): Flow<R> {
    return flatMapLatest { result ->
        flow {
            if (result is Error) {
                emit(transform(result.throwable))
            }
        }
    }
}

inline fun <reified T> Flow<Result<T>>.runOnSucceeded(
    crossinline onSucceeded: suspend (data: T) -> Unit,
): Flow<Result<T>> {
    return mapLatest {
        if (it is Success) {
            onSucceeded(it.data)
        }
        it
    }
}
