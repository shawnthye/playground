package app.playground.store.mappers

import api.product.hunt.fragment.PostsFields
import app.playground.store.database.entities.Post
import core.playground.data.Mapper
import javax.inject.Inject

class PostsFieldsToPosts @Inject constructor(
    private val postFieldsToPost: PostFieldsToPost,
) : Mapper<PostsFields, List<Post>>() {
    override suspend fun parse(from: PostsFields) = from.edges.map {
        postFieldsToPost(it.node.postFields)
    }
}
