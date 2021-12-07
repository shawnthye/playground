package feature.playground.product.hunt.posts.ui

import api.product.hunt.fragment.PostFragment

internal data class DiscoverState(
    val posts: List<PostFragment> = emptyList(),
    val refreshing: Boolean = false,
) {
    companion object {
        val EMPTY = DiscoverState()
    }
}
