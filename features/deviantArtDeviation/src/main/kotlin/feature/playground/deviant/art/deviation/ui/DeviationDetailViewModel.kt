package feature.playground.deviant.art.deviation.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.playground.store.database.entities.Deviation
import app.playground.store.database.entities.DeviationId
import core.playground.domain.Result
import core.playground.domain.Result.Error
import core.playground.domain.Result.Loading
import core.playground.domain.data
import core.playground.domain.mapOnError
import core.playground.ui.UiMessage
import core.playground.ui.WhileViewSubscribed
import core.playground.ui.asUiMessage
import core.playground.ui.asUiMessageOr
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.playground.deviant.art.deviation.domain.LoadDeviantUseCase
import feature.playground.deviant.art.deviation.ui.DeviationDetailFragment.Companion.ARG_DEVIATION_ID
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class DeviationDetailViewModel @Inject constructor(
    loadDeviantUseCase: LoadDeviantUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), DeviationDetailActionListener {

    private val deviantId = savedStateHandle.get<DeviationId>(ARG_DEVIATION_ID)!!.also {
        Timber.i("deviation $it opened")
    }

    private val _actionRefresh = Channel<Unit>(Channel.CONFLATED).apply {
        trySend(Unit) // init loading
    }

    private val result: Flow<Result<Deviation>> = _actionRefresh.receiveAsFlow()
        .flatMapLatest { loadDeviantUseCase(deviantId) }
        .stateIn(viewModelScope, WhileViewSubscribed, Loading())

    val deviation: StateFlow<Deviation?> = result.mapNotNull { result ->
        result.data
    }.stateIn(scope = viewModelScope, started = WhileViewSubscribed, initialValue = null)

    val isLoading = result.mapLatest {
        it is Loading
    }.stateIn(scope = viewModelScope, started = WhileViewSubscribed, initialValue = false)

    /**
     * This is just for demo purpose that how we can present an empty state error message
     * Since we are using NetworkBoundResult and the data also backed by the Track Fragment screen
     */
    val errorMessage = result.filter {
        it is Error && it.data == null
    }.mapOnError { error ->
        error.asUiMessage()
    }.stateIn(viewModelScope, WhileViewSubscribed, null)

    val snackBarMessageId: Flow<UiMessage> = result.filter {
        /**
         * We don't use snack bar when the content is empty
         * @see [errorMessage]
         */
        it.data != null
    }.mapOnError { error ->
        error.asUiMessageOr {
            UiMessage.String(it.message) // simply return error here here for demo
        }
    }.shareIn(
        viewModelScope,
        WhileViewSubscribed,
        /**
         * because we transform this data from StateFlow,
         * we use [shareIn] with replay 0,
         * so that the SnackBar won't be visible again when orientation, resume, etc happened
         */
        0,
    )

    fun onSwipeRefresh() {
        _actionRefresh.trySend(Unit)
    }

    private val _action = Channel<DeviationDetailAction>(capacity = Channel.CONFLATED)
    val action = _action.receiveAsFlow()

    override fun onAction(action: DeviationDetailAction) {
        _action.trySend(action)
    }
}
