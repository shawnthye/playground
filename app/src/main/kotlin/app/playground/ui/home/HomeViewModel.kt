package app.playground.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(0))
    val uiState: StateFlow<HomeUiState> = _uiState

    fun onClick() {
        viewModelScope.launch {
            _uiState.emit(HomeUiState(count = _uiState.value.count + 1))
        }
    }
}
