package core.playground.data

import androidx.annotation.Keep
import core.playground.Reason
import core.playground.data.Response.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.mapLatest
import retrofit2.HttpException

/**
 * Common class used by API responses.
 * @param <T> the type of the response object
</T> */
@Keep
sealed class Response<out T> {

    /**
     * separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null.
     */

    data class Success<T>(val body: T) : Response<T>()
    data class Error<T>(val exception: Throwable) : Response<T>()

    /**
     * a special case that we can't convert empty for 204,
     * consumer must decide to serve their content with cache
     * or do something else in other situation
     */
    object Empty : Response<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success[body=$body]"
            is Error -> "Error[exception=$exception]"
            Empty -> "Empty"
        }
    }

    internal companion object {
        fun <T> create(error: Throwable): Error<T> {
            return Error(error)
        }

        fun <T> create(response: retrofit2.Response<T>): Response<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (null != body && 204 != response.code()) {
                    Success(body)
                } else {
                    Empty
                }
            } else {
                Error(response.asHttpError())
            }
        }
    }
}

@Throws(
    Reason.Connection::class,
    Reason.HttpError::class,
    IllegalStateException::class,
)
suspend inline fun <reified T> Flow<Response<T>>.execute(): T? = when (val response = last()) {
    is Success -> response.body
    is Response.Error -> throw response.exception
    is Response.Empty -> null
}

private fun <T> retrofit2.Response<T>.asHttpError(): Reason.HttpError {
    val body = errorBody()?.string().takeUnless { it.isNullOrBlank() }
    val errorMsg = body ?: message()

    return Reason.HttpError(code(), errorMsg, HttpException(this))
}

infix fun <From, To> Flow<Response<From>>.withMapper(
    mapper: Mapper<From, To>,
): Flow<Response<To>> {
    return mapLatest { response ->
        when (response) {
            is Success -> {
                Success(mapper(response.body))
            }
            is Response.Error -> Response.Error(response.exception)
            Response.Empty -> Response.Empty
        }
    }
}
