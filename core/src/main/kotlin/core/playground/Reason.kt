package core.playground

import java.io.IOException

sealed class Reason(original: Throwable?) : IOException(original) {
    data class Connection(val original: Throwable) : Reason(original)

    // TODO: timeout for retry
    // data class Timeout(val original: Throwable) : Connection(original)

    /**
     * SeeAlso [Http status codes](https://en.wikipedia.org/wiki/List_of_HTTP_status_codes)
     *
     * Standard http approach is
     *
     * 5xx - Client should retry later
     *
     * 4xx - Client should not retry until application is updated,
     * basically something wrong that client doesn't do it properly
     * eg: 401 Unauthorized - Client failed to redirect client to logic,
     * so retry on the same request will not be helpful
     *
     * TODO: maybe different Error for different context?
     * so that client doesn't need to have nested condition to check status code
     */
    data class HttpError(val code: Int, override val message: String) : Reason(null)

    override fun toString(): String {
        return when (this) {
            is Connection -> {
                "Look like the connection is lost or either too slow, ${super.toString()}"
            }
            is HttpError -> {
                "Http Status: $code, $message"
            }
        }
    }
}
