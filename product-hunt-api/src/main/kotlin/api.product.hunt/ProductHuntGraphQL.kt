package api.product.hunt

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Mutation
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query
import core.playground.data.Response
import kotlinx.coroutines.flow.Flow
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import javax.inject.Inject
import javax.inject.Singleton

private const val API_PATH = "api.producthunt.com/v2/api/graphql"

private val SERVER_URL = "https://$API_PATH".toHttpUrl()

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
    ): Flow<Response<T>> = graphql.query(query).asResponse()

    fun <D : Operation.Data, T, V : Operation.Variables> mutate(
        mutation: Mutation<D, T, V>,
    ): Flow<Response<T>> = graphql.mutate(mutation).asResponse()
}
