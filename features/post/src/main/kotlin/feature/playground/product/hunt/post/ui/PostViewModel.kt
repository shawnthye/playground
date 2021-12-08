package feature.playground.product.hunt.post.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.playground.store.database.entities.PostId
import core.playground.domain.data
import core.playground.ui.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.playground.product.hunt.post.domain.LoadPostUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class PostViewModel @Inject constructor(
    loadPostUseCase: LoadPostUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val id: PostId = savedStateHandle.get<String>("postId")!!

    private val actionRefresh = Channel<Unit>(Channel.CONFLATED).apply {
        trySend(Unit)
    }

    private val refresh = actionRefresh.receiveAsFlow()

    private val result = refresh.flatMapLatest { loadPostUseCase(id) }

    val state = result.map {
        it.data

    }.stateIn(viewModelScope, WhileViewSubscribed, null)
}
