package feature.playground.product.hunt.posts.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.playground.domain.Result
import core.playground.domain.Result.Success
import core.playground.domain.data
import core.playground.ui.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.playground.product.hunt.posts.domain.LoadPostsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
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

    private val result = refresh.flatMapLatest { loadPostsUseCase(Unit) }.stateIn(
        viewModelScope, WhileViewSubscribed, Result.Loading(),
    )

    private val cache = result.filter {
        it is Success
    }.map {
        it.data
    }.stateIn(viewModelScope, WhileViewSubscribed, null)

    val uiState = combine(cache, result) { posts, result ->
        val newState = result.toUiState()
        newState.copy(
            posts = newState.posts.takeUnless { it.isEmpty() } ?: posts ?: emptyList(),
        )
    }.stateIn(viewModelScope, WhileViewSubscribed, DiscoveryUiState.EMPTY)

    fun onRefresh() {
        actionRefresh.trySend(Unit)
    }
}
