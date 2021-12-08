package api.product.hunt

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloMutationCall
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.api.Mutation
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.coroutines.await
import core.playground.Reason
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

private const val API_PATH = "api.producthunt.com/v2/api/graphql"

private val SERVER_URL = "https://$API_PATH".toHttpUrl()

/**
 * Temporary use developer key
 */
@Suppress("SpellCheckingInspection")
private const val AUTHORIZATION = "Bearer YFvkSRZUDIZFnCnXAmthYqEfXAJj5803JoE8Yk6OLuU"

@Singleton
class ProductHuntGraphQL @Inject constructor(
    client: OkHttpClient,
) {

    private val graphql: ApolloClient by lazy {
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
    ): ApolloQueryCall<T> = graphql.query(query)

    fun <D : Operation.Data, T, V : Operation.Variables> mutate(
        mutation: Mutation<D, T, V>,
    ): ApolloMutationCall<T> = graphql.mutate(mutation)
}

inline fun <reified T> ApolloQueryCall<T>.asFlow(): Flow<core.playground.data.Response<T>> {
    return flow {
        val response = await()

        if (!response.hasErrors()) {
            if (response.data != null) {
                emit(core.playground.data.Response.Success(response.data!!))
            } else {
                emit(core.playground.data.Response.Empty)
            }
        } else {
            val errorMessages = response.errors?.joinToString("\n") ?: "No error message"
            emit(core.playground.data.Response.Error(Reason.HttpError(-1, errorMessages)))
        }
    }
}

private class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", AUTHORIZATION)
            .build()

        return chain.proceed(request)
    }
}
