package feature.playground.product.hunt.post.data

import api.product.hunt.PostQuery
import api.product.hunt.ProductHuntGraphQL
import com.apollographql.apollo.coroutines.await
import javax.inject.Inject

internal class PostRepository
@Inject constructor(
    private val graphql: ProductHuntGraphQL,
) {
    suspend fun queryPost(id: String) = graphql.query(PostQuery(id)).await()
}
