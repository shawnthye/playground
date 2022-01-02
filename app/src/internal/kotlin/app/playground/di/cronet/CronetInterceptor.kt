package app.playground.di.cronet

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

interface CronetProcessor {

    @Throws(IOException::class)
    fun proceed(chain: Interceptor.Chain): Response
}

class CronetInterceptor(
    private val processor: CronetProcessor,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return processor.proceed(chain)
    }
}
