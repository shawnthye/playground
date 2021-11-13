package feature.playground.deviant.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.playground.entities.DeviationEntity
import core.playground.domain.Result
import core.playground.ui.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.playground.deviant.domain.LoadDeviantUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DeviationViewModel @Inject constructor(
    loadDeviantUseCase: LoadDeviantUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val deviantId: String = savedStateHandle.get<String>(EXTRA_ID)!!

    private val _swipeRefreshing = Channel<Unit>(Channel.CONFLATED).apply {
        // init start loading
        trySend(Unit)
    }

    val loadDeviantResult: StateFlow<Result<DeviationEntity>> = _swipeRefreshing
        .receiveAsFlow()
        .flatMapLatest { loadDeviantUseCase(deviantId) }.stateIn(
            scope = viewModelScope,
            started = WhileViewSubscribed,
            initialValue = Result.Loading(),
        )

    fun onSwipeRefresh() {
        _swipeRefreshing.trySend(Unit)
    }

    companion object {
        const val EXTRA_ID = "extra.id"
    }
}
