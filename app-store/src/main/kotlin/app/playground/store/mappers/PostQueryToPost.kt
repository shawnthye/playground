package app.playground.store.mappers

import api.product.hunt.PostQuery
import app.playground.store.database.entities.Post
import core.playground.data.Mapper
import javax.inject.Inject

class PostQueryToPost @Inject constructor(
    private val postFieldsToPost: PostFieldsToPost,
) : Mapper<PostQuery.Data, Post>() {
    override suspend fun parse(from: PostQuery.Data): Post {
        val fields = from.post!!.postFields
        return postFieldsToPost(fields)
    }
}
