package api.art.deviant

import api.art.deviant.model.DeviantArtToken
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.CacheControl
import okhttp3.FormBody
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviantArtAuthenticator @Inject constructor(
    private val client: OkHttpClient,
    private val json: Json,
) : Authenticator {

    private var tokenCache: DeviantArtToken? = null
    internal val interceptor = AuthenticatorInterceptor()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.request.header("Authorization") != null) {
            tokenCache = null
        }

        if (tokenCache == null) {
            // try request token, since token might expired, or nvr request before
            tokenCache = requestToken()
        }

        if (tokenCache == null) {
            return null
        }

        return buildAuthRequest(response.request)
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

        val token = json.decodeFromString<DeviantArtToken>(response.body!!.string())
        response.close()

        return token
    }

    private fun buildAuthRequest(origin: Request): Request {
        return origin.newBuilder().apply {
            tokenCache?.also {
                header("Authorization", "${it.token_type} ${it.access_token}")
            }
        }.build()
    }

    internal inner class AuthenticatorInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            if (tokenCache == null) {
                tokenCache = requestToken()
            }

            return chain.proceed(buildAuthRequest(chain.request()))
        }
    }
}
