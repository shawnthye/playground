package api.product.hunt

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Temporary use developer key
 */
@Suppress("SpellCheckingInspection")
private const val AUTHORIZATION = "Bearer YFvkSRZUDIZFnCnXAmthYqEfXAJj5803JoE8Yk6OLuU"

internal class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", AUTHORIZATION)
            .build()

        return chain.proceed(request)
    }
}
