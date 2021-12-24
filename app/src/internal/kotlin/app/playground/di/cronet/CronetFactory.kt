package app.playground.di.cronet

import app.playground.di.cronet.CronetCall
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import org.chromium.net.CronetEngine

class CronetFactory(
    private val okhttp: OkHttpClient,
    private val engine: CronetEngine,
    private val dispatcher: CoroutineDispatcher
) : Call.Factory {

    override fun newCall(request: Request): Call {
        return CronetCall(engine, dispatcher, okhttp, request)
    }
}