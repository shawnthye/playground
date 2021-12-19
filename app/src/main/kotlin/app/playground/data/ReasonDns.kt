package app.playground.data

import core.playground.Reason
import okhttp3.CacheControl
import okhttp3.Dns
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.InetAddress
import java.time.Duration

class ReasonDns(
    private val okhttp: OkHttpClient,
) : Dns {
    override fun lookup(hostname: String): List<InetAddress> {
        return try {
            Dns.SYSTEM.lookup(hostname)
        } catch (t: Throwable) {
            throw okhttp.wrapThrowable(t)
        }
    }
}

/**
 * Here we use google to check if the connect too slow
 */
private fun OkHttpClient.wrapThrowable(original: Throwable): Throwable {
    val client = newBuilder()
        .callTimeout(Duration.ofSeconds(10))
        .connectTimeout(Duration.ofSeconds(5))
        .retryOnConnectionFailure(true)
        .apply {
            /**
             * clear all the interceptor, since we doesn't need it
             * we only need other setting eg: DNS,
             * so that we can use the closet environment to check connection
             */
            interceptors().clear()
        }
        .build()

    return try {
        client.newCall(GENERATE_204_REQUEST).execute()
        // no problem, return the original
        original
    } catch (e: IOException) {
        return Reason.Connection(original)
    } catch (e: Throwable) {
        /**
         * something else, return the original too
         * TODO: a way to return both? in case?
         */
        original
    }
}

private val GENERATE_204_REQUEST by lazy {
    Request.Builder()
        .url("https://clients3.google.com/generate_204")
        .method("HEAD", null)
        .header("Cache-Control", "no-cache")
        .cacheControl(CacheControl.FORCE_NETWORK)
        .build()
}
