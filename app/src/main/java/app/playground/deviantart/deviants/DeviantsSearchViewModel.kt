package app.playground.deviantart.deviants

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.playground.deviantart.DeviantArtApi
import app.playground.deviantart.model.Deviation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DeviantsSearchViewModel : ViewModel() {

    private val api = DeviantArtApi.get()

    private val _deviationState = MutableStateFlow<Deviation?>(null)
    val deviationState: StateFlow<Deviation?> = _deviationState

    init {
        viewModelScope.launch {
            api.popular(null).also {
                _deviationState.emit(it.results.first())
            }
        }
    }
}
