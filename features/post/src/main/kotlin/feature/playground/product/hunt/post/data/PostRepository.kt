package feature.playground.product.hunt.post.data

import api.product.hunt.PostQuery
import api.product.hunt.ProductHunt
import com.apollographql.apollo.coroutines.await
import javax.inject.Inject

internal class PostRepository
@Inject constructor(
    private val graphQL: ProductHunt,
) {
    suspend fun queryPost(id: String) = graphQL.query(PostQuery(id)).await()
}
