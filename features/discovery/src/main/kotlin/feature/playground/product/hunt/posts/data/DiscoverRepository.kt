package feature.playground.product.hunt.posts.data

import api.product.hunt.PostsQuery
import api.product.hunt.ProductHunt
import com.apollographql.apollo.coroutines.await
import javax.inject.Inject

internal class DiscoverRepository
@Inject constructor(
    private val productHunt: ProductHunt,
) {
    suspend fun queryPosts() = productHunt.query(PostsQuery()).await()
}
