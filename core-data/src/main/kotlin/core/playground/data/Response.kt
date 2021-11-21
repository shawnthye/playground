package core.playground.data

import androidx.annotation.Keep
import core.playground.Reason

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

    internal companion object {
        fun <T> create(error: Throwable): Error<T> {
            return Error(error)
        }

        fun <T> create(response: retrofit2.Response<T>): Response<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204) {
                    Empty
                } else {
                    Success(body)
                }
            } else {
                Error(response.asHttpError())
            }
        }
    }
}

private fun <T> retrofit2.Response<T>.asHttpError(): Reason.HttpError {
    val body = errorBody()?.string().takeUnless { it.isNullOrBlank() }
    val errorMsg = body ?: message()

    return Reason.HttpError(code(), errorMsg)
}
