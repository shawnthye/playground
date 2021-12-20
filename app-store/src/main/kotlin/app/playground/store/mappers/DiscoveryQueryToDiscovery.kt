package app.playground.store.mappers

import api.product.hunt.DiscoveryQuery
import app.playground.store.database.entities.Discovery
import app.playground.store.database.entities.DiscoveryPostEntry
import core.playground.data.Mapper
import javax.inject.Inject

class DiscoveryQueryToDiscovery @Inject constructor(
    private val postsFieldsToPosts: PostsFieldsToPosts,
) : Mapper<DiscoveryQuery.Data, List<Discovery>>() {
    override suspend fun parse(from: DiscoveryQuery.Data) = postsFieldsToPosts(
        from.posts.postsFields,
    ).map {
        Discovery(
            entry = DiscoveryPostEntry(
                nextPage = from.posts.pageInfo.pageFields.nextPage,
                postId = it.postId,
            ),
            post = it,
        )
    }
}
