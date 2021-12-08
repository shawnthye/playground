package feature.playground.product.hunt.post.data

import api.product.hunt.PostQuery
import api.product.hunt.ProductHuntGraphQL
import app.playground.store.database.entities.Post
import app.playground.store.database.entities.PostId
import app.playground.store.mappers.PostQueryToPost
import core.playground.data.Response
import core.playground.data.withMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal interface PostDataSource {
    fun queryPost(id: PostId): Flow<Response<Post>>
}

internal class PostDataSourceImpl @Inject constructor(
    private val graphql: ProductHuntGraphQL,
    private val postQueryToPost: PostQueryToPost,
) : PostDataSource {

    override fun queryPost(
        id: PostId,
    ): Flow<Response<Post>> = graphql.query(PostQuery(id)).withMapper(postQueryToPost)
}




