package app.playground.di.cronet

import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Headers
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.closeQuietly
import okhttp3.internal.toImmutableList
import okio.AsyncTimeout
import okio.Buffer
import okio.Timeout
import org.chromium.net.CronetEngine
import org.chromium.net.UploadDataProviders
import org.chromium.net.UrlRequest
import org.chromium.net.UrlResponseInfo
import java.io.IOException
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

private val OkHttpClient.coroutineDispatcher: CoroutineDispatcher
    get() {
        return dispatcher.executorService.asCoroutineDispatcher()
    }

class CronetCall(
    private val engine: CronetEngine,
    private val okhttp: OkHttpClient,
    private val request: Request,
) : Call,
    CronetProcessor {

    private val scope = CoroutineScope(Job() + okhttp.coroutineDispatcher)

    private val timeout: AsyncTimeout = object : AsyncTimeout() {
        override fun timedOut() {
            cancel()
        }
    }.apply {
        timeout(okhttp.callTimeoutMillis.toLong(), TimeUnit.MILLISECONDS)
    }

    private val executed = AtomicBoolean()

    private val eventListener = okhttp.eventListenerFactory.create(this)

    @Volatile
    private var canceled = false

    override fun cancel() {
        if (canceled) return
        canceled = true
        scope.cancel()
        eventListener.canceled(this)
    }

    override fun clone(): Call {
        return CronetCall(
            okhttp = okhttp,
            request = request,
            engine = engine,
        )
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override fun enqueue(responseCallback: Callback) {
        check(executed.compareAndSet(false, true)) { "Already Executed" }

        timeout.enter()
        eventListener.callStart(this)
        //TODO: better way to handle cancel like, we can't just call execute() like this
        // also check if cronet is cancel

        val handler = CoroutineExceptionHandler { _, e ->
            print(e.localizedMessage)
        }

        scope.launch(handler) {
            try {
                val response = getResponseWithInterceptorChain()
                responseCallback.onResponse(this@CronetCall, response)
            } catch (io: IOException) {
                responseCallback.onFailure(this@CronetCall, io)
            }
        }
    }

    @WorkerThread
    override fun execute(): Response {
        check(executed.compareAndSet(false, true)) { "Already Executed" }

        timeout.enter()
        eventListener.callStart(this)

        return getResponseWithInterceptorChain()
    }

    override fun isCanceled() = canceled

    override fun isExecuted(): Boolean {
        return executed.get()
    }

    override fun request(): Request = request

    override fun timeout(): Timeout = timeout

    @Throws(IOException::class)
    private fun getResponseWithInterceptorChain(): Response {
        val interceptors = mutableListOf<Interceptor>()
        interceptors += okhttp.interceptors
        interceptors += okhttp.networkInterceptors
        interceptors += CronetInterceptor(this)

        val chain = CronetChain(
            call = this,
            interceptors = interceptors.toImmutableList(),
            index = 0,
            request = request,
            okhttp.connectTimeoutMillis,
            okhttp.readTimeoutMillis,
            okhttp.writeTimeoutMillis,
        )

        val response = try {
            chain.proceed(request)
        } catch (e: CancellationException) {
            throw IOException("Canceled", e)
        }

        if (isCanceled()) {
            response.closeQuietly()
            throw IOException("Canceled")
        }

        return response
    }

    override fun proceed(chain: Chain): Response {
        return runBlocking(scope.coroutineContext) {
            val sentRequestMillis = System.currentTimeMillis()

            suspendCancellableCoroutine { continuation ->
                val request = chain.request()
                val call = chain.call()

                if (call.isCanceled()) {
                    continuation.cancel(IOException("Canceled"))
                }

                val callback = object : ReadToMemoryCronetCallback() {

                    var builder = Response.Builder()
                        .request(request)
                        .sentRequestAtMillis(sentRequestMillis)

                    override fun onResponseStarted(urlRequest: UrlRequest, info: UrlResponseInfo) {
                        builder = builder
                            .parse(info)
                            .request(request.newBuilder().url(info.url.toHttpUrl()).build())
                        eventListener.responseHeadersEnd(call, builder.build())
                        eventListener.responseBodyStart(call)
                        super.onResponseStarted(urlRequest, info)
                    }

                    override fun onSucceeded(
                        urlRequest: UrlRequest,
                        info: UrlResponseInfo,
                        bodyBytes: ByteArray,
                    ) {
                        eventListener.responseBodyEnd(call, info.receivedByteCount)

                        try {
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

                            eventListener.callEnd(call)
                            continuation.resume(response)
                        } catch (t: Throwable) {
                            eventListener.callEnd(call)
                            continuation.resumeWithException(t)
                        }
                    }

                    override fun onFailure(
                        urlRequest: UrlRequest,
                        info: UrlResponseInfo?,
                        error: IOException,
                    ) {
                        eventListener.callFailed(call, error)
                        continuation.resumeWithException(error)
                    }

                    override fun onCanceled() {
                        eventListener.canceled(call)
                    }
                }

                val urlRequest = request.toUrlRequest(
                    engine = engine,
                    callback = callback,
                    dispatcher = okhttp.coroutineDispatcher,
                )

                urlRequest.start()

                continuation.invokeOnCancellation {
                    urlRequest.cancel()
                }
            }
        }
    }
}

private fun Request.toUrlRequest(
    engine: CronetEngine,
    callback: UrlRequest.Callback,
    dispatcher: CoroutineDispatcher,
): UrlRequest = engine.newUrlRequestBuilder(url.toString(), callback, dispatcher.asExecutor())
    .setHttpMethod(method)
    .apply {
        headers.forEach { (header, value) -> addHeader(header, value) }
    }
    .apply {
        body?.let { body ->
            addHeader("Content-Type", body.contentType().toString())
            val buffer = Buffer()
            body.writeTo(buffer)
            UploadDataProviders.create(buffer.readByteArray())
        }?.also { provider ->
            setUploadDataProvider(provider, dispatcher.asExecutor())
        }
    }
    .build()

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
