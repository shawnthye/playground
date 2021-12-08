package feature.playground.product.hunt.posts.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.playground.domain.Result
import core.playground.ui.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.playground.product.hunt.posts.domain.LoadPostsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class DiscoverViewModel @Inject constructor(
    loadPostsUseCase: LoadPostsUseCase,
) : ViewModel() {

    private val actionRefresh = Channel<Unit>(Channel.CONFLATED).apply { trySend(Unit) }

    private val refresh = actionRefresh.receiveAsFlow()

    private val postsResult = refresh.flatMapLatest { loadPostsUseCase(Unit) }.stateIn(
        viewModelScope, WhileViewSubscribed, Result.Loading(),
    )

    val state = postsResult.map {
        when (it) {
            is Result.Success -> {
                DiscoverState(
                    posts = it.data.posts.edges.map { edge -> edge.node.fragments.post },
                )
            }
            is Result.Error -> TODO()
            is Result.Loading -> DiscoverState(refreshing = true)
        }

    }.stateIn(viewModelScope, WhileViewSubscribed, DiscoverState.EMPTY)

    fun onRefresh() {
        actionRefresh.trySend(Unit)
    }
}
