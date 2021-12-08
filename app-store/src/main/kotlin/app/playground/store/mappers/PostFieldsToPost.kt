package app.playground.store.mappers

import api.product.hunt.fragment.PostFields
import app.playground.store.database.entities.Post
import core.playground.data.Mapper
import javax.inject.Inject

class PostFieldsToPost @Inject constructor() : Mapper<PostFields, Post>() {
    override suspend fun parse(from: PostFields): Post {
        return Post(
            postId = from.id,
            name = from.name,
            thumbnailUrl = from.thumbnail?.url,
        )
    }
}
