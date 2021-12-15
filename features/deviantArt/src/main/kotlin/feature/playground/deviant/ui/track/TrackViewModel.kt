package feature.playground.deviant.ui.track

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.palette.graphics.Palette
import app.playground.store.database.entities.Deviation
import app.playground.store.database.entities.TrackWithDeviation
import core.playground.domain.ExperimentalPagingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.playground.deviant.domain.LoadTrackDeviantsUseCase
import feature.playground.deviant.domain.UpdateDeviantUseCase
import feature.playground.deviant.widget.PaletteExtensions.findRippleColor
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalPagingUseCase::class)
@HiltViewModel
class TrackViewModel
@Inject constructor(
    private val updateDeviantUseCase: UpdateDeviantUseCase,
    loadTrackDeviantsUseCase: LoadTrackDeviantsUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), TrackAdapter.OnItemClickListener, TrackAdapter.OnPaletteListener {

    private val safeArgs = DeviantTrackFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val track = safeArgs.track

    val pagingData: Flow<PagingData<TrackWithDeviation>> = loadTrackDeviantsUseCase(track)
        .cachedIn(viewModelScope)

    private val _idAction = Channel<String>(capacity = Channel.CONFLATED)
    val idActions = _idAction.receiveAsFlow()

    override fun onItemClicked(id: String) {
        _idAction.trySend(id)
    }

    override fun onPaletteReady(deviation: Deviation, palette: Palette) {
        viewModelScope.launch {
            palette.findRippleColor()?.also {
                updateDeviantUseCase(deviation.copy(rippleColor = it))
            }
        }
    }
}
