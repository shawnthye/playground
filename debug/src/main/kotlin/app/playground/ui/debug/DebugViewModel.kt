package app.playground.ui.debug

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.playground.ui.debug.DebugUiAction.ResetAllSettings
import app.playground.ui.debug.DebugUiAction.SeenDrawer
import app.playground.ui.debug.DebugUiAction.UpdateEnvironment
import app.playground.ui.debug.DebugUiAction.UpdateHttpLogging
import app.playground.ui.debug.data.DebugEnvironment
import app.playground.ui.debug.data.DebugStorage.Defaults
import app.playground.ui.debug.data.HttpLogging
import app.playground.ui.debug.data.deviceStats
import app.playground.ui.debug.domain.GetDebugEnvironmentUseCase
import app.playground.ui.debug.domain.GetHttpLoggingUseCase
import app.playground.ui.debug.domain.GetSeenDrawerUseCase
import app.playground.ui.debug.domain.ResetSettingsUseCase
import app.playground.ui.debug.domain.SeHttpLoggingUseCase
import app.playground.ui.debug.domain.SetDebugEnvironmentUseCase
import app.playground.ui.debug.domain.SetSeenDrawerUseCase
import core.playground.domain.successOr
import core.playground.ui.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class DebugViewModel @Inject constructor(
    private val getSeenDrawerUseCase: GetSeenDrawerUseCase,
    private val setSeenDrawerUseCase: SetSeenDrawerUseCase,
    private val getDebugEnvironmentUseCase: GetDebugEnvironmentUseCase,
    private val setDebugEnvironmentUseCase: SetDebugEnvironmentUseCase,
    private val getHttpLoggingUseCase: GetHttpLoggingUseCase,
    private val setHttpLoggingUseCase: SeHttpLoggingUseCase,
    private val resetSettingsUseCase: ResetSettingsUseCase,
    app: Application,
) : ViewModel() {

    private val loadDataSignal = flowOf(Unit)

    private val actions = Channel<DebugUiAction>(Channel.CONFLATED)

    val applicationName = app.applicationInfo.loadLabel(app.packageManager)
    val deviceStats: Map<String, String> = app.deviceStats

    val seenDrawer: StateFlow<Boolean> = loadDataSignal.flatMapLatest {
        getSeenDrawerUseCase(Unit)
    }.map {
        it.successOr(false)
    }.stateIn(
        viewModelScope, WhileViewSubscribed, true,
    )

    val environment = loadDataSignal.flatMapLatest {
        getDebugEnvironmentUseCase(Unit)
    }.map {
        it.successOr(Defaults.Environment)
    }.stateIn(
        viewModelScope, WhileViewSubscribed, Defaults.Environment,
    )

    val httpLoggingLevel = loadDataSignal.flatMapLatest {
        getHttpLoggingUseCase(Unit)
    }.map {
        it.successOr(Defaults.OkhttpLoggingLevel)
    }.stateIn(
        viewModelScope, WhileViewSubscribed, Defaults.OkhttpLoggingLevel,
    )

    fun seenDrawer() = actions.trySend(SeenDrawer)

    fun updateEnvironment(environment: DebugEnvironment) = actions.trySend(
        UpdateEnvironment(environment),
    )

    fun updateHttpLoggingLevel(level: HttpLogging) = actions.trySend(UpdateHttpLogging(level))

    fun resetDebugSettings() = actions.trySend(ResetAllSettings)

    init {
        actions.receiveAsFlow().onEach {
            when (it) {
                is UpdateHttpLogging -> setHttpLoggingUseCase(it.level)
                is UpdateEnvironment -> setDebugEnvironmentUseCase(it.environment)
                SeenDrawer -> setSeenDrawerUseCase(Unit)
                ResetAllSettings -> resetSettingsUseCase(Unit)
            }
        }.launchIn(viewModelScope)
    }
}

internal sealed class DebugUiAction {
    object SeenDrawer : DebugUiAction()
    data class UpdateEnvironment(val environment: DebugEnvironment) : DebugUiAction()
    data class UpdateHttpLogging(val level: HttpLogging) : DebugUiAction()
    object ResetAllSettings : DebugUiAction()
}
