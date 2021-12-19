package core.playground.domain

import core.playground.Generated
import core.playground.data.Response
import core.playground.domain.Result.Error
import core.playground.domain.Result.Loading
import core.playground.domain.Result.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.transformLatest

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()

    data class Loading<out T>(val data: T? = null) : Result<T>()

    data class Error<out T>(val throwable: Throwable, val data: T? = null) : Result<T>()

    @Generated(comments = "for debugging only")
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
    get() = this is Success

val <T> Result<T>.data: T?
    get() = (this as? Success)?.data ?: (this as? Loading)?.data ?: (this as? Error)?.data

fun <T> Result<T>.successOr(fallback: T): T {
    return when (this) {
        is Success<T> -> data
        else -> fallback
    }
}

@Generated(
    comments = "This still won't ignore in coverage," +
        " see this also: issue https://github.com/jacoco/jacoco/issues/654",
)
inline fun <reified T, R> Flow<Result<T>>.mapOnError(
    crossinline transform: suspend (throwable: Throwable) -> R,
): Flow<R> {
    return transform { result ->
        if (result is Error) {
            emit(transform(result.throwable))
        }
    }
}

inline fun <reified T> Flow<Response<T>>.toResult(): Flow<Result<T>> {
    return mapLatest { response ->
        when (response) {
            is Response.Success -> Success(response.body)
            is Response.Empty -> Success(Unit as T)
            is Response.Error -> Error(response.exception, data = null)
        }
    }
}
