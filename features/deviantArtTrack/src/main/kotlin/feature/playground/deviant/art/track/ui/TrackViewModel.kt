package feature.playground.deviant.art.track.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.playground.store.database.entities.DeviationId
import app.playground.store.database.entities.Track
import app.playground.store.database.entities.TrackWithDeviation
import core.playground.domain.ExperimentalPagingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.playground.deviant.art.track.domain.LoadTrackDeviantsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@OptIn(ExperimentalPagingUseCase::class)
@HiltViewModel
class TrackViewModel
@Inject constructor(
    loadTrackDeviantsUseCase: LoadTrackDeviantsUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), TrackAdapter.OnItemClickListener {

    private val track = savedStateHandle.get<Track>("track")!!

    val pagingData: Flow<PagingData<TrackWithDeviation>> = loadTrackDeviantsUseCase(track)
        .cachedIn(viewModelScope)

    private val _idAction = Channel<String>(capacity = Channel.CONFLATED)
    val idActions = _idAction.receiveAsFlow()

    override fun onItemClicked(id: DeviationId) {
        _idAction.trySend(id)
    }
}
