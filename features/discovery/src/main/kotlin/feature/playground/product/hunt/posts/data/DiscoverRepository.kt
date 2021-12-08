package feature.playground.product.hunt.posts.data

import api.product.hunt.PostsQuery
import api.product.hunt.ProductHuntGraphQL
import api.product.hunt.asFlow
import core.playground.domain.toResult
import javax.inject.Inject

internal class DiscoverRepository
@Inject constructor(
    private val graphql: ProductHuntGraphQL,
) {
    fun queryPosts() = graphql.query(PostsQuery()).asFlow().toResult()
}
