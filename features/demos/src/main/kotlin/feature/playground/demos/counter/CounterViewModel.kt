package feature.playground.demos.counter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * This is just for demo, If we doesn't need to inject anything, we shouldn't need [ViewModel]
 */
@HiltViewModel
internal class CounterViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CounterUiState(0))
    val uiState: StateFlow<CounterUiState> = _uiState

    fun onClick() {
        viewModelScope.launch {
            _uiState.emit(CounterUiState(count = _uiState.value.count + 1))
        }
    }
}
