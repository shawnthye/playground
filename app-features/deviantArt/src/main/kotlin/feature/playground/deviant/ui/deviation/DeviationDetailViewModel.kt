package feature.playground.deviant.ui.deviation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.playground.source.of.truth.database.entities.Deviation
import core.playground.domain.Result
import core.playground.domain.data
import core.playground.domain.mapLatestError
import core.playground.ui.UiMessage
import core.playground.ui.WhileViewSubscribed
import core.playground.ui.asUiMessageOr
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.playground.deviant.domain.LoadDeviantUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DeviationDetailViewModel @Inject constructor(
    loadDeviantUseCase: LoadDeviantUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val safeArgs = DeviationDetailFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val deviantId = safeArgs.id.also { Timber.i("deviation $it opened") }

    private val _actionRefresh = Channel<Unit>(Channel.CONFLATED).apply {
        trySend(Unit) // init loading
    }

    private val loadDeviantResult: Flow<Result<Deviation>> = _actionRefresh
        .receiveAsFlow()
        .flatMapLatest { loadDeviantUseCase(deviantId) }

    private val deviationResult: StateFlow<Result<Deviation>> = loadDeviantResult
        .stateIn(
            scope = viewModelScope,
            started = WhileViewSubscribed,
            initialValue = Result.Loading(),
        )

    val deviation: StateFlow<Deviation?> = deviationResult.mapNotNull { result ->
        result.data
    }.stateIn(scope = viewModelScope, started = WhileViewSubscribed, initialValue = null)

    val isLoading = deviationResult.map {
        it is Result.Loading
    }.stateIn(scope = viewModelScope, started = WhileViewSubscribed, initialValue = true)

    val snackBarMessageId: Flow<UiMessage> = deviationResult.mapLatestError { error ->
        error.asUiMessageOr {
            UiMessage.String(it.message) // simply return error here here for demo
        }
    }.shareIn(viewModelScope, WhileViewSubscribed, 0)

    fun onSwipeRefresh() {
        _actionRefresh.trySend(Unit)
    }
}
