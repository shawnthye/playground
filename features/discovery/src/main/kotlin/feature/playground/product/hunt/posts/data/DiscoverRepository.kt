package feature.playground.product.hunt.posts.data

import api.product.hunt.PostsQuery
import api.product.hunt.ProductHuntGraphQL
import app.playground.store.mappers.PostsQueryToPosts
import core.playground.data.withMapper
import core.playground.domain.toResult
import javax.inject.Inject

internal class DiscoverRepository
@Inject constructor(
    private val graphql: ProductHuntGraphQL,
    private val postsQueryToPosts: PostsQueryToPosts,
) {
    fun queryPosts() = graphql.query(PostsQuery()).withMapper(postsQueryToPosts).toResult()
}
