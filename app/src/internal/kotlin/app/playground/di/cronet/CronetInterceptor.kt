package app.playground.di.cronet

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.EventListener
import okhttp3.Headers
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.platform.Platform
import okio.Buffer
import org.chromium.net.CronetEngine
import org.chromium.net.UploadDataProviders
import org.chromium.net.UrlRequest
import org.chromium.net.UrlResponseInfo
import java.io.IOException
import java.net.ProtocolException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CronetInterceptor(
    private val engine: CronetEngine,
    private val dispatcher: CoroutineDispatcher,
    private val listener: EventListener,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return runBlocking(dispatcher) {

            val url = chain.request().url

            if (!url.isHttps) {
                if (!Platform.get().isCleartextTrafficPermitted(url.host)) {
                    throw ProtocolException(
                        """CLEARTEXT communication to ${url.host} not permitted
                            | by network security policy""".trimMargin(),
                    )
                }
            }

            engine.proceed(chain)
        }
    }

    private suspend fun CronetEngine.proceed(chain: Interceptor.Chain): Response {
        val sentRequestMillis = System.currentTimeMillis()
        return suspendCancellableCoroutine { continuation ->
            val request = chain.request()
            val call = chain.call()

            val callback = object : ReadToMemoryCronetCallback() {

                var builder = Response.Builder()
                    .request(request)
                    .sentRequestAtMillis(sentRequestMillis)

                override fun onResponseStarted(urlRequest: UrlRequest, info: UrlResponseInfo) {
                    builder = builder
                        .parse(info)
                        .request(request.newBuilder().url(info.url.toHttpUrl()).build())
                    listener.responseHeadersEnd(call, builder.build())
                    listener.responseBodyStart(call)
                    super.onResponseStarted(urlRequest, info)
                }

                override fun onSucceeded(
                    urlRequest: UrlRequest,
                    info: UrlResponseInfo,
                    bodyBytes: ByteArray,
                ) {
                    listener.responseBodyEnd(call, info.receivedByteCount)

                    val mimeType = info
                        .allHeadersAsList
                        .first { it.key.lowercase() == "content-type" }
                        .value
                        .toMediaTypeOrNull()

                    val response = Response.Builder().apply {
                        request(request.newBuilder().url(info.url.toHttpUrl()).build())
                        parse(info)
                        body(bodyBytes.toResponseBody(mimeType))

                        if (info.wasCached()) {
                            cacheResponse(build())
                        }
                        // TODO: we haven't verify if the cached is working really working
                        // else {
                        //     networkResponse(build())
                        // }
                    }.build()

                    listener.callEnd(call)
                    continuation.resume(response)
                }

                override fun onFailed(
                    urlRequest: UrlRequest,
                    info: UrlResponseInfo,
                    error: IOException,
                ) {
                    listener.callFailed(call, error)
                    continuation.resumeWithException(error)
                }

                override fun onCanceled() {
                    listener.canceled(call)
                }
            }

            val urlRequest = newUrlRequestBuilder(
                request.url.toString(),
                callback,
                dispatcher.asExecutor(),
            ).applyRequest(
                request,
                dispatcher,
            ).build().also { it.start() }

            continuation.invokeOnCancellation {
                urlRequest.cancel()
            }
        }
    }
}

private fun UrlRequest.Builder.applyRequest(request: Request, dispatcher: CoroutineDispatcher) =
    apply {
        setHttpMethod(request.method)
        request.headers.forEach { (header, value) -> addHeader(header, value) }

        request.body?.let { body ->
            addHeader("Content-Type", body.contentType().toString())
            val buffer = Buffer()
            body.writeTo(buffer)
            UploadDataProviders.create(buffer.readByteArray())
        }?.also { provider ->
            setUploadDataProvider(provider, dispatcher.asExecutor())
        }
    }

private fun Response.Builder.parse(info: UrlResponseInfo) = apply {
    receivedResponseAtMillis(System.currentTimeMillis())
    protocol(info.protocol)
    code(info.httpStatusCode)
    message(info.httpStatusText)
    headers(info.headers)
}

private val UrlResponseInfo.headers: Headers
    get() = Headers.Builder().apply {
        allHeadersAsList.forEach {
            add(it.key, it.value)
        }

        add("X-ENGINE", "cronet")
    }.build()

private val UrlResponseInfo.protocol: Protocol
    get() = when {
        @Suppress("SpellCheckingInspection")
        negotiatedProtocol.contains("quic") -> Protocol.QUIC
        negotiatedProtocol.contains("spdy") -> Protocol.HTTP_2
        negotiatedProtocol.contains("h2") -> Protocol.HTTP_2
        negotiatedProtocol.contains("1.1") -> Protocol.HTTP_1_1
        else -> Protocol.HTTP_1_0
    }
