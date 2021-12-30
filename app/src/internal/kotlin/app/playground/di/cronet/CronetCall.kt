package app.playground.di.cronet

import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.closeQuietly
import okhttp3.internal.toImmutableList
import okio.AsyncTimeout
import okio.Timeout
import org.chromium.net.CronetEngine
import java.io.IOException
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CronetCall(
    private val engine: CronetEngine,
    private val dispatcher: CoroutineDispatcher,
    private val okhttp: OkHttpClient,
    private val request: Request,
) : Call, CoroutineScope {

    override val coroutineContext: CoroutineContext get() = dispatcher

    private var job: Job? = null

    private val timeout: AsyncTimeout = object : AsyncTimeout() {
        override fun timedOut() {
            cancel()
        }
    }.apply {
        timeout(okhttp.callTimeoutMillis.toLong(), TimeUnit.MILLISECONDS)
    }

    private val executed = AtomicBoolean()

    private val eventListener = okhttp.eventListenerFactory.create(this)

    private val interceptor: CronetInterceptor =
        CronetInterceptor(engine, dispatcher, eventListener)

    @Volatile
    private var canceled = false

    override fun cancel() {
        if (canceled) return

        canceled = true
        interceptor.cancel()
        job?.cancel()
        job = null
        eventListener.canceled(this)
    }

    override fun clone(): Call {
        return CronetCall(
            okhttp = okhttp,
            request = request,
            engine = engine,
            dispatcher = dispatcher,
        )
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override fun enqueue(responseCallback: Callback) {
        check(executed.compareAndSet(false, true)) { "Already Executed" }

        timeout.enter()
        eventListener.callStart(this)
        //TODO: better way to handle cancel like, we can't just call execute() like this
        // also check if cronet is cancel
        job = launch {
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

        return runBlocking(dispatcher) { getResponseWithInterceptorChain() }
    }

    override fun isCanceled() = canceled

    override fun isExecuted(): Boolean {
        return executed.get()
    }

    override fun request(): Request = request

    override fun timeout(): Timeout = timeout

    @Throws(IOException::class)
    private suspend fun getResponseWithInterceptorChain(): Response {

        return suspendCancellableCoroutine { continuation ->
            val interceptors = mutableListOf<Interceptor>()
            interceptors += okhttp.interceptors
            interceptors += okhttp.networkInterceptors
            interceptors += interceptor

            val chain = CronetChain(
                call = this,
                interceptors = interceptors.toImmutableList(),
                index = 0,
                request = request,
                okhttp.connectTimeoutMillis,
                okhttp.readTimeoutMillis,
                okhttp.writeTimeoutMillis,
            )

            val response = chain.proceed(request)

            if (isCanceled()) {
                response.closeQuietly()
                continuation.resumeWithException(IOException("Canceled"))
            } else {
                continuation.resume(response)
            }
        }
    }
}
