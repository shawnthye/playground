package feature.playground.product.hunt.posts.data

import api.product.hunt.DiscoveryQuery
import api.product.hunt.ProductHuntGraphQL
import app.playground.store.database.entities.Discovery
import app.playground.store.mappers.DiscoveryQueryToDiscovery
import com.apollographql.apollo3.api.Optional.Companion.presentIfNotNull
import core.playground.data.Response
import core.playground.data.withMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal interface DiscoverDataSource {
    fun query(nextPage: String?): Flow<Response<List<Discovery>>>
}

class DiscoverDataSourceImpl @Inject constructor(
    private val graphql: ProductHuntGraphQL,
    private val discoveryQueryToDiscovery: DiscoveryQueryToDiscovery,
) : DiscoverDataSource {

    override fun query(nextPage: String?) = graphql.query(
        DiscoveryQuery(nextPage = presentIfNotNull(nextPage)),
    ).withMapper(discoveryQueryToDiscovery)
}
