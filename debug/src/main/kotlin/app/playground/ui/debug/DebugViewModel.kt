package app.playground.ui.debug

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.playground.ui.debug.data.DebugEnvironment
import app.playground.ui.debug.data.DebugStorage.Defaults
import app.playground.ui.debug.data.HttpEngine
import app.playground.ui.debug.data.HttpLogging
import app.playground.ui.debug.domain.GetBuildStatsUseCase
import app.playground.ui.debug.domain.GetDebugEnvironmentUseCase
import app.playground.ui.debug.domain.GetDeviceStatsUseCase
import app.playground.ui.debug.domain.GetHttpEngineUseCase
import app.playground.ui.debug.domain.GetHttpLoggingUseCase
import app.playground.ui.debug.domain.GetSeenDrawerUseCase
import app.playground.ui.debug.domain.ResetSettingsUseCase
import app.playground.ui.debug.domain.SeHttpLoggingUseCase
import app.playground.ui.debug.domain.SetDebugEnvironmentUseCase
import app.playground.ui.debug.domain.SetHttpEngineUseCase
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
import kotlinx.coroutines.flow.mapLatest
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
    private val getHttpEngineUseCase: GetHttpEngineUseCase,
    private val setHttpEngineUseCase: SetHttpEngineUseCase,
    private val getHttpLoggingUseCase: GetHttpLoggingUseCase,
    private val setHttpLoggingUseCase: SeHttpLoggingUseCase,
    private val resetSettingsUseCase: ResetSettingsUseCase,
    private val getBuildStatsUseCase: GetBuildStatsUseCase,
    private val getDeviceStatsUseCase: GetDeviceStatsUseCase,
    app: Application,
) : ViewModel() {

    private val loadDataSignal = flowOf(Unit)

    private val actions = Channel<DebugUiAction>(Channel.CONFLATED)

    private val _navigationActions = Channel<DebugNavigationAction>(Channel.CONFLATED)
    val navigationActions = _navigationActions.receiveAsFlow()

    val applicationName = app.applicationInfo.loadLabel(app.packageManager)

    val deviceStats = loadDataSignal.mapLatest {
        getDeviceStatsUseCase(Unit).successOr(emptyMap())
    }.stateIn(viewModelScope, WhileViewSubscribed, emptyMap())

    val buildStats = loadDataSignal.mapLatest {
        getBuildStatsUseCase(Unit).successOr(emptyMap())
    }.stateIn(viewModelScope, WhileViewSubscribed, emptyMap())

    val seenDrawer: StateFlow<Boolean> = loadDataSignal.flatMapLatest {
        getSeenDrawerUseCase(Unit).map { it.successOr(false) }
    }.stateIn(viewModelScope, WhileViewSubscribed, true)

    val environment = loadDataSignal.flatMapLatest {
        getDebugEnvironmentUseCase(Unit).map { it.successOr(Defaults.Environment) }
    }.stateIn(viewModelScope, WhileViewSubscribed, Defaults.Environment)

    val httpEngine = loadDataSignal.flatMapLatest {
        getHttpEngineUseCase(Unit).map { it.successOr(Defaults.NetworkHttpEngine) }
    }.stateIn(viewModelScope, WhileViewSubscribed, Defaults.NetworkHttpEngine)

    val httpLoggingLevel = loadDataSignal.flatMapLatest {
        getHttpLoggingUseCase(Unit).map { it.successOr(Defaults.OkhttpLoggingLevel) }
    }.stateIn(viewModelScope, WhileViewSubscribed, Defaults.OkhttpLoggingLevel)

    fun seenDrawer() = actions.trySend(DebugUiAction.SeenDrawer)

    fun updateEnvironment(environment: DebugEnvironment) = actions.trySend(
        DebugUiAction.UpdateEnvironment(environment),
    )

    fun updateHttpEngine(engine: HttpEngine) = actions.trySend(
        DebugUiAction.UpdateHttpEngine(engine),
    )

    fun updateHttpLoggingLevel(level: HttpLogging) = actions.trySend(
        DebugUiAction.UpdateHttpLogging(level),
    )

    fun resetDebugSettings() = actions.trySend(DebugUiAction.ResetAllSettings)

    fun showFeatureFlags() = _navigationActions.trySend(DebugNavigationAction.ShowFeatureFlags)

    init {
        actions.receiveAsFlow().onEach {
            when (it) {
                is DebugUiAction.UpdateEnvironment -> setDebugEnvironmentUseCase(it.environment)
                is DebugUiAction.UpdateHttpEngine -> setHttpEngineUseCase(it.engine)
                is DebugUiAction.UpdateHttpLogging -> setHttpLoggingUseCase(it.level)
                DebugUiAction.SeenDrawer -> setSeenDrawerUseCase(Unit)
                DebugUiAction.ResetAllSettings -> resetSettingsUseCase(Unit)
            }
        }.launchIn(viewModelScope)
    }
}

internal sealed class DebugUiAction {
    object SeenDrawer : DebugUiAction()
    data class UpdateEnvironment(val environment: DebugEnvironment) : DebugUiAction()
    data class UpdateHttpEngine(val engine: HttpEngine) : DebugUiAction()
    data class UpdateHttpLogging(val level: HttpLogging) : DebugUiAction()
    object ResetAllSettings : DebugUiAction()
}

internal sealed class DebugNavigationAction {
    object ShowFeatureFlags : DebugNavigationAction()
}
