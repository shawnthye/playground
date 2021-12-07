package api.product.hunt

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloMutationCall
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.api.Mutation
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

private const val API_PATH = "api.producthunt.com/v2/api/graphql"

private val SERVER_URL = "https://$API_PATH".toHttpUrl()

@Suppress("SpellCheckingInspection")
private const val AUTHORIZATION = "Bearer YFvkSRZUDIZFnCnXAmthYqEfXAJj5803JoE8Yk6OLuU"

@MustBeDocumented
@Retention(value = AnnotationRetention.BINARY)
@RequiresOptIn(level = RequiresOptIn.Level.WARNING)
annotation class ExperimentalProductHuntApi

@Singleton
@ExperimentalProductHuntApi
class ProductHunt @Inject constructor(
    client: OkHttpClient,
) {

    private val lazy: ApolloClient by lazy {
        val okHttpClient = client.newBuilder()
            .addInterceptor(AuthorizationInterceptor())
            .build()

        ApolloClient.builder()
            .serverUrl(SERVER_URL)
            .okHttpClient(okHttpClient)
            .build()
    }

    fun <D : Operation.Data, T, V : Operation.Variables> query(
        query: Query<D, T, V>,
    ): ApolloQueryCall<T> = lazy.query(query)

    fun <D : Operation.Data, T, V : Operation.Variables> mutate(
        mutation: Mutation<D, T, V>,
    ): ApolloMutationCall<T> = lazy.mutate(mutation)

    // fun <D : Operation.Data, T, V : Operation.Variables> mutate(
    //     mutation: Mutation<D, T, V>,
    //     withOptimisticUpdates: D,
    // ): ApolloMutationCall<T> = lazy.mutate(mutation, withOptimisticUpdates)
}

private class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", AUTHORIZATION)
            .build()

        return chain.proceed(request)
    }
}
