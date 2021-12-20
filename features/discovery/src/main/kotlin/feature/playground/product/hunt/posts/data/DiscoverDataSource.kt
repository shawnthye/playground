package feature.playground.product.hunt.posts.data

import api.product.hunt.PostsQuery
import api.product.hunt.ProductHuntGraphQL
import app.playground.store.database.entities.Post
import app.playground.store.mappers.PostsQueryToPosts
import core.playground.data.Response
import core.playground.data.withMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal interface DiscoverDataSource {
    fun queryPost(): Flow<Response<List<Post>>>
}

class DiscoverDataSourceImpl @Inject constructor(
    private val graphql: ProductHuntGraphQL,
    private val postsQueryToPosts: PostsQueryToPosts,
) : DiscoverDataSource {
    override fun queryPost() = graphql.query(PostsQuery()).withMapper(postsQueryToPosts)
}
