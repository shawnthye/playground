package api.product.hunt

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Query
import com.apollographql.apollo3.network.okHttpCallFactory
import core.playground.data.CallFactory
import core.playground.data.Response
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import javax.inject.Inject
import javax.inject.Singleton

private const val API_PATH = "api.producthunt.com/v2/api/graphql"

private const val SERVER_URL = "https://$API_PATH"

@Singleton
class ProductHuntGraphQL @Inject constructor(
    client: OkHttpClient,
    callFactory: CallFactory,
) {

    private val graphql: ApolloClient by lazy {
        val okHttpClient = client.newBuilder()
            .addInterceptor(AuthorizationInterceptor())
            .build()

        ApolloClient.Builder()
            .serverUrl(SERVER_URL)
            .okHttpCallFactory(callFactory { okHttpClient })
            .build()
    }

    fun <D : Query.Data> query(
        query: Query<D>,
    ): Flow<Response<D>> = graphql.query(query).asResponse()

    fun <D : Mutation.Data> mutation(
        mutation: Mutation<D>,
    ): Flow<Response<D>> = graphql.mutation(mutation).asResponse()
}
