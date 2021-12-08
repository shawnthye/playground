package feature.playground.product.hunt.posts.data

import api.product.hunt.PostsQuery
import api.product.hunt.ProductHuntGraphQL
import com.apollographql.apollo.coroutines.await
import javax.inject.Inject

internal class DiscoverRepository
@Inject constructor(
    private val graphql: ProductHuntGraphQL,
) {
    suspend fun queryPosts() = graphql.query(PostsQuery()).await()
}
