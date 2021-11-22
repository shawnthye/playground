package feature.playground.deviant.ui.track

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.playground.domain.Result
import core.playground.ui.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.playground.deviant.domain.LoadTrackDeviantsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DeviantTrackViewModel
@Inject constructor(
    loadTrackDeviantsUseCase: LoadTrackDeviantsUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), DeviantTrackAdapter.OnClickListener {

    private val safeArgs = DeviantTrackArgs.fromSavedStateHandle(savedStateHandle)

    private val track = safeArgs.track

    private val _refreshingAction = Channel<Unit>(Channel.CONFLATED).apply {
        trySend(Unit) // init loading
    }

    private val resultState: Flow<Result<List<app.playground.source.of.truth.database.entities.TrackWithDeviation>>> =
        _refreshingAction
            .receiveAsFlow()
            .flatMapLatest { loadTrackDeviantsUseCase(track) }.stateIn(
                viewModelScope,
                WhileViewSubscribed,
                Result.Loading(),
            )

    val deviationsState: StateFlow<List<app.playground.source.of.truth.database.entities.TrackWithDeviation>> =
        resultState.map {
            it.data ?: emptyList()
        }.stateIn(
            viewModelScope,
            WhileViewSubscribed,
            emptyList(),
        )

    val isRefreshing = resultState.map {
        it is Result.Loading
    }.stateIn(
        viewModelScope,
        WhileViewSubscribed,
        true,
    )

    private val _idAction = Channel<String>(capacity = Channel.CONFLATED)
    val idActions = _idAction.receiveAsFlow()

    fun onSwipeRefresh() {
        _refreshingAction.trySend(Unit)
    }

    override fun onClicked(id: String) {
        _idAction.trySend(id)
    }
}
