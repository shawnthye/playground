package feature.playground.product.hunt.post.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.playground.store.database.entities.PostId
import core.playground.domain.Result.Loading
import core.playground.domain.data
import core.playground.domain.mapOnError
import core.playground.ui.UiMessage
import core.playground.ui.WhileViewSubscribed
import core.playground.ui.asUiMessageOr
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.playground.product.hunt.post.domain.LoadPostUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class PostViewModel @Inject constructor(
    loadPostUseCase: LoadPostUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val id: PostId = savedStateHandle.get<PostId>("postId")!!

    private val actionRefresh = Channel<Unit>(Channel.CONFLATED).apply {
        trySend(Unit)
    }

    private val result = actionRefresh.receiveAsFlow().flatMapLatest {
        loadPostUseCase(id)
    }.stateIn(
        viewModelScope, WhileViewSubscribed, Loading(),
    )

    val refreshing: Flow<Boolean> = result.map { it is Loading }.stateIn(
        viewModelScope, WhileViewSubscribed, true,
    )

    val uiState = result.mapLatest { it.data }.stateIn(
        viewModelScope, WhileViewSubscribed, null,
    )

    val snackBarMessage = result.filter { it.data != null }.mapOnError { error ->
        error.asUiMessageOr { UiMessage.String(it.message) }
    }.shareIn(viewModelScope, WhileViewSubscribed)

    fun refresh() {
        actionRefresh.trySend(Unit)
    }
}
