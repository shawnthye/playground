package feature.playground.deviant.ui.track

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.playground.source.of.truth.database.entities.TrackWithDeviation
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.playground.deviant.domain.LoadTrackDeviantsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class TrackViewModel
@Inject constructor(
    loadTrackDeviantsUseCase: LoadTrackDeviantsUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), TrackAdapter.OnItemClickListener {

    private val safeArgs = DeviantTrackFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val track = safeArgs.track

    val pagingData: Flow<PagingData<TrackWithDeviation>> = loadTrackDeviantsUseCase(track)
        .cachedIn(viewModelScope)

    private val _idAction = Channel<String>(capacity = Channel.CONFLATED)
    val idActions = _idAction.receiveAsFlow()

    override fun onItemClicked(id: String) {
        _idAction.trySend(id)
    }
}
