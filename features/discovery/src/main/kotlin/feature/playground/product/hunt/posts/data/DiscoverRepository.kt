package feature.playground.product.hunt.posts.data

import core.playground.domain.toResult
import javax.inject.Inject

internal class DiscoverRepository @Inject constructor(
    private val discoverDataSource: DiscoverDataSource,
) {
    fun queryPosts() = discoverDataSource.queryPost().toResult()
}
