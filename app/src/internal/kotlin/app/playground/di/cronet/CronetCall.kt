package app.playground.di.cronet

import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.toImmutableList
import okio.AsyncTimeout
import okio.IOException
import okio.Timeout
import org.chromium.net.CronetEngine
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

class CronetCall(
    private val engine: CronetEngine,
    private val dispatcher: CoroutineDispatcher,
    private val okhttp: OkHttpClient,
    private val request: Request,
) : Call {

    private var job: Job? = null

    private val timeout: Timeout = object : AsyncTimeout() {
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
        job?.cancel()
        job = null
        eventListener.canceled(this)
    }

    override fun clone(): Call {
        return CronetCall(
            okhttp = okhttp,
            request = request,
            engine = engine,
            dispatcher = dispatcher
        )
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override fun enqueue(responseCallback: Callback) {
        job = CoroutineScope(dispatcher).launch {
            try {
                val response = execute()
                responseCallback.onResponse(this@CronetCall, response)
            } catch (io: IOException) {
                responseCallback.onFailure(this@CronetCall, io)
            }
        }
    }

    @WorkerThread
    override fun execute(): Response {
        check(executed.compareAndSet(false, true)) { "Already Executed" }

        eventListener.callStart(this)

        val interceptors = mutableListOf<Interceptor>()
        interceptors += okhttp.interceptors
        interceptors += okhttp.networkInterceptors
        interceptors += CronetInterceptor(engine, dispatcher, eventListener)

        val chain = CronetChain(
            call = this,
            interceptors = interceptors.toImmutableList(),
            index = 0,
            request = request,
            okhttp.connectTimeoutMillis,
            okhttp.readTimeoutMillis,
            okhttp.writeTimeoutMillis,
        )

        return chain.proceed(request)
    }

    override fun isCanceled() = canceled

    override fun isExecuted(): Boolean {
        return executed.get()
    }

    override fun request(): Request = request

    override fun timeout(): Timeout = timeout
}
