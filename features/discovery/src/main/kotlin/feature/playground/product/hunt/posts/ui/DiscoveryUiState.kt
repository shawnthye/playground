package feature.playground.product.hunt.posts.ui

import app.playground.store.database.entities.Post
import core.playground.domain.Result
import core.playground.ui.UiMessage
import core.playground.ui.asUiMessage

internal data class DiscoveryUiState(
    val posts: List<Post> = emptyList(),
    val refreshing: Boolean = false,
    val uiMessage: UiMessage? = null,
) {
    companion object {
        val EMPTY = DiscoveryUiState()
    }
}

internal fun Result<List<Post>>.toUiState(): DiscoveryUiState {
    return when (this) {
        is Result.Error -> DiscoveryUiState(uiMessage = throwable.asUiMessage())
        is Result.Loading -> DiscoveryUiState(refreshing = true)
        is Result.Success -> DiscoveryUiState(posts = data)
    }
}
