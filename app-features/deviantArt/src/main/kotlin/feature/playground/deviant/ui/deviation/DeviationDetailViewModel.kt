package feature.playground.deviant.ui.deviation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.playground.entities.DeviationEntity
import core.playground.Reason
import core.playground.domain.Result
import core.playground.domain.mapCachedThrowable
import core.playground.ui.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.playground.deviant.domain.LoadDeviantUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
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

    private val loadDeviantResult: StateFlow<Result<DeviationEntity>> = _swipeRefreshing
        .receiveAsFlow()
        .flatMapLatest { loadDeviantUseCase(deviantId) }.stateIn(
            scope = viewModelScope,
            started = WhileViewSubscribed,
            initialValue = Result.Loading(),
        )

    val deviationState = loadDeviantResult.map { it.data }.stateIn(
        scope = viewModelScope,
        started = WhileViewSubscribed,
        initialValue = null,
    )

    val isLoading = loadDeviantResult.mapLatest { it is Result.Loading }.stateIn(
        scope = viewModelScope,
        started = WhileViewSubscribed,
        initialValue = true,
    )

    val snackbarMessageId: Flow<Int> = loadDeviantResult.mapCachedThrowable().map {
        when (it.throwable) {
            is Reason.Connection -> core.playground.ui.R.string.reason_connection
            else -> core.playground.ui.R.string.reason_unknown
        }
    }

    fun onSwipeRefresh() {
        _swipeRefreshing.trySend(Unit)
    }
}
