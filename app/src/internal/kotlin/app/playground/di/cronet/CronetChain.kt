package app.playground.di.cronet

import okhttp3.Call
import okhttp3.Connection
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

class CronetChain(
    internal val call: Call,
    private val interceptors: List<Interceptor>,
    private val index: Int,
    internal val request: Request,
    internal val connectTimeoutMillis: Int,
    internal val readTimeoutMillis: Int,
    internal val writeTimeoutMillis: Int
) : Interceptor.Chain {

    private var calls: Int = 0

    private fun copy(
        index: Int = this.index,
        request: Request = this.request,
        connectTimeoutMillis: Int = this.connectTimeoutMillis,
        readTimeoutMillis: Int = this.readTimeoutMillis,
        writeTimeoutMillis: Int = this.writeTimeoutMillis
    ) = CronetChain(
        call = call,
        interceptors = interceptors,
        index = index,
        request = request,
        connectTimeoutMillis = connectTimeoutMillis,
        readTimeoutMillis = readTimeoutMillis,
        writeTimeoutMillis = writeTimeoutMillis
    )

    override fun call(): Call = call

    override fun connectTimeoutMillis(): Int = connectTimeoutMillis

    override fun connection(): Connection? = null

    @Throws(IOException::class)
    override fun proceed(request: Request): Response {
        check(index < interceptors.size)

        calls++

        val next = copy(index = index + 1, request = request)
        val interceptor = interceptors[index]

        return interceptor.intercept(next)
    }

    override fun readTimeoutMillis(): Int = readTimeoutMillis

    override fun request(): Request = request

    override fun withConnectTimeout(timeout: Int, unit: TimeUnit): Interceptor.Chain = this

    override fun withReadTimeout(timeout: Int, unit: TimeUnit): Interceptor.Chain = this

    override fun withWriteTimeout(timeout: Int, unit: TimeUnit): Interceptor.Chain = this

    override fun writeTimeoutMillis(): Int = writeTimeoutMillis
}
