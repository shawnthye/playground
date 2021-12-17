package app.playground.store.mappers

import api.product.hunt.PostsQuery
import app.playground.store.database.entities.Post
import core.playground.data.Mapper
import javax.inject.Inject

class PostsQueryToPosts @Inject constructor(
    private val postFieldsToPost: PostFieldsToPost,
) : Mapper<PostsQuery.Data, List<Post>>() {
    override suspend fun parse(from: PostsQuery.Data): List<Post> = from.posts.edges.map {
        postFieldsToPost(it.node.postFields)
    }
}
