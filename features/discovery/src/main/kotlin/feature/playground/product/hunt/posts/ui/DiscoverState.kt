package feature.playground.product.hunt.posts.ui

import api.product.hunt.fragment.Post

internal data class DiscoverState(
    val posts: List<Post> = emptyList(),
    val refreshing: Boolean = false,
) {
    companion object {
        val EMPTY = DiscoverState()
    }
}
