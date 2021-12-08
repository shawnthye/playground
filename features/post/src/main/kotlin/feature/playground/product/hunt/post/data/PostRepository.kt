package feature.playground.product.hunt.post.data

import api.product.hunt.PostQuery
import api.product.hunt.ProductHuntGraphQL
import javax.inject.Inject

internal class PostRepository
@Inject constructor(
    private val graphql: ProductHuntGraphQL,
) {
    fun queryPost(id: String) = graphql.query(PostQuery(id))
}
