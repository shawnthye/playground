package api.art.deviant

import com.google.gson.Gson
import okhttp3.CacheControl
import okhttp3.FormBody
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

private data class DeviantArtToken(val access_token: String, val token_type: String)

@Singleton
class AuthenticatorInterceptor @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : Interceptor {

    private var tokenCache: DeviantArtToken? = null

    override fun intercept(chain: Interceptor.Chain): Response {

        val response = if (tokenCache != null) {
            chain.proceed(chain.request())
        } else {
            null
        }

        if (response == null || response.code == 401) {
            tokenCache = null
            tokenCache = requestToken()
        } else {
            return response
        }

        val request = chain.request().newBuilder().apply {
            tokenCache?.also {
                header("Authorization", "${it.token_type} ${it.access_token}")
            }
        }.build()

        return chain.proceed(request)
    }

    private fun requestToken(): DeviantArtToken? {
        val form = FormBody.Builder()
            .add("grant_type", "client_credentials")
            .add("client_id", "17390")
            .add("client_secret", "d88b3cc2995f26b4f5cc76da3166e465")
            .build()

        val request = Request.Builder()
            .url("https://www.deviantart.com/oauth2/token".toHttpUrl())
            .post(form)
            .addHeader("Cache-Control", "no-cache")
            .cacheControl(CacheControl.FORCE_NETWORK)
            .build()

        val response = client.newBuilder().build().newCall(request).execute()

        if (!response.isSuccessful) {
            return null
        }

        return gson.fromJson(response.body!!.string(), DeviantArtToken::class.java)
    }
}
