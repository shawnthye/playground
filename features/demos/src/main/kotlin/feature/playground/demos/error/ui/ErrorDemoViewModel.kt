package feature.playground.demos.error.ui

import androidx.lifecycle.ViewModel
import core.playground.domain.mapLatestError
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.playground.demos.error.domain.OkHttpUseCase
import feature.playground.demos.error.domain.Response204UseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class ErrorDemoViewModel @Inject constructor(
    okHttpUseCase: OkHttpUseCase,
    response204UseCase: Response204UseCase,
) : ViewModel() {

    private val uiAction = Channel<ErrorUiAction>(capacity = Channel.CONFLATED)

    private val result = uiAction.receiveAsFlow().flatMapLatest { action ->
        when (action) {
            ErrorUiAction.Okhttp -> flow { emit(okHttpUseCase(Unit)) }
            ErrorUiAction.Response204 -> response204UseCase(Unit)
        }
    }.map {
        Timber.i("$it")
        it
    }

    val throwable = result.mapLatestError { it }

    fun tryOkHttp() {
        uiAction.trySend(ErrorUiAction.Okhttp)
    }

    fun try204Response() {
        uiAction.trySend(ErrorUiAction.Response204)
    }
}
