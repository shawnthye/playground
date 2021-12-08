package feature.playground.product.hunt.posts.ui

import api.product.hunt.fragment.PostFields

internal data class DiscoverState(
    val posts: List<PostFields> = emptyList(),
    val refreshing: Boolean = false,
) {
    companion object {
        val EMPTY = DiscoverState()
    }
}
