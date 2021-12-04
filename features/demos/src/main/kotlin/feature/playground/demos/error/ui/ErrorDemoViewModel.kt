package feature.playground.demos.error.ui

import androidx.lifecycle.ViewModel
import core.playground.domain.mapLatestError
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.playground.demos.error.domain.OkHttpUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
internal class ErrorDemoViewModel @Inject constructor(
    okHttpUseCase: OkHttpUseCase,
) : ViewModel() {

    private val uiAction = Channel<ErrorUiAction>(capacity = Channel.CONFLATED)

    private val result = uiAction.receiveAsFlow().mapLatest { action ->
        when (action) {
            ErrorUiAction.Google -> okHttpUseCase(Unit)
        }
    }

    val throwable = result.mapLatestError { it }

    fun tryOkHttp() {
        uiAction.trySend(ErrorUiAction.Google)
    }
}
