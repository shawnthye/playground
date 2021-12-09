package feature.playground.demos.counter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.playground.core.interactors.LoadDeviantTrackInteractor
import app.playground.store.database.entities.Track
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * This is just for demo, If we doesn't need to inject anything, we shouldn't need [ViewModel]
 */
@HiltViewModel
internal class CounterViewModel @Inject constructor(
    private val deviantTrackInteractor: LoadDeviantTrackInteractor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CounterUiState(0))
    val uiState: StateFlow<CounterUiState> = _uiState.asStateFlow()

    fun onClick() {
        viewModelScope.launch {
            deviantTrackInteractor(Track.HOT)
            _uiState.update { it.copy(count = it.count + 1) }
        }
    }
}
