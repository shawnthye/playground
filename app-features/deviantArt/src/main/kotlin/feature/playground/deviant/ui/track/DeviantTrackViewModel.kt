package feature.playground.deviant.ui.track

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.playground.source.of.truth.database.entities.TrackWithDeviation
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.playground.deviant.data.DeviantRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class DeviantTrackViewModel
@Inject constructor(
    // loadTrackDeviantsUseCase: LoadTrackDeviantsUseCase,
    savedStateHandle: SavedStateHandle,
    repository: DeviantRepository,
) : ViewModel(), TrackPagingAdapter.OnClickListener {

    private val safeArgs = DeviantTrackFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val track = safeArgs.track

    private val _refreshingAction = Channel<Unit>(Channel.CONFLATED).apply {
        trySend(Unit) // init loading
    }

    val resultState: Flow<PagingData<TrackWithDeviation>> = _refreshingAction
        .receiveAsFlow()
        .flatMapLatest { repository.observeTrack2(track) }
        .cachedIn(viewModelScope)

    // val deviationsState: StateFlow<List<TrackWithDeviation>> =
    //     resultState.map {
    //         it.data ?: emptyList()
    //     }.stateIn(
    //         viewModelScope,
    //         WhileViewSubscribed,
    //         emptyList(),
    //     )

    // val isRefreshing = resultState.map {
    //     it is Result.Loading
    // }.stateIn(
    //     viewModelScope,
    //     WhileViewSubscribed,
    //     true,
    // )

    private val _idAction = Channel<String>(capacity = Channel.CONFLATED)
    val idActions = _idAction.receiveAsFlow()

    fun onSwipeRefresh() {
        _refreshingAction.trySend(Unit)
    }

    override fun onClicked(id: String) {
        _idAction.trySend(id)
    }
}
