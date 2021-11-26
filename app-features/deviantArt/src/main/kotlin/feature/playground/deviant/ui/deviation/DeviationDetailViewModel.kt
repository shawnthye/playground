package feature.playground.deviant.ui.deviation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.playground.source.of.truth.database.entities.Deviation
import core.playground.Reason
import core.playground.domain.Result
import core.playground.domain.mapLatestError
import core.playground.ui.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.playground.deviant.domain.LoadDeviantUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DeviationDetailViewModel @Inject constructor(
    loadDeviantUseCase: LoadDeviantUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val safeArgs = DeviationDetailArgs.fromSavedStateHandle(savedStateHandle)

    private val deviantId = safeArgs.id.also { Timber.i("deviation $it opened") }

    private val _swipeRefreshing = Channel<Unit>(Channel.CONFLATED).apply {
        trySend(Unit) // init loading
    }

    private val loadDeviantResult: Flow<Result<Deviation>> = _swipeRefreshing
        .receiveAsFlow()
        .flatMapLatest { loadDeviantUseCase(deviantId) }

    val deviationState: StateFlow<Result<Deviation>> = loadDeviantResult
        .stateIn(
            scope = viewModelScope,
            started = WhileViewSubscribed,
            initialValue = Result.Loading(),
        )

    val isLoading = deviationState.mapLatest { it is Result.Loading }
        .stateIn(
            scope = viewModelScope,
            started = WhileViewSubscribed,
            initialValue = true,
        )

    val snackBarMessageId: Flow<Int> = deviationState.mapLatestError {
        when (it) {
            is Reason.Connection -> core.playground.ui.R.string.reason_connection
            else -> core.playground.ui.R.string.reason_unknown
        }
    }

    fun onSwipeRefresh() {
        _swipeRefreshing.trySend(Unit)
    }
}
