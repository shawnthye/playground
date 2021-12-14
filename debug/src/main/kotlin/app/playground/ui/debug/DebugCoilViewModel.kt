package app.playground.ui.debug

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.playground.ui.debug.data.CoilLogLevel
import app.playground.ui.debug.data.DebugStorage
import coil.Coil
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.DefaultRequestOptions
import coil.util.CoilUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

internal sealed class CoilAction {
    data class UpdateLogLevel(val logLevel: CoilLogLevel) : CoilAction()
    data class UpdateMemoryPolicy(val policy: CachePolicy) : CoilAction()
    data class UpdateDiskPolicy(val policy: CachePolicy) : CoilAction()
    data class UpdateNetworkPolicy(val policy: CachePolicy) : CoilAction()
    object TrimMemory : CoilAction()
    object Refresh : CoilAction()
    object Reset : CoilAction()
}

@HiltViewModel
internal class DebugCoilViewModel @Inject constructor(
    private val storage: DebugStorage,
    private val app: Application,
) : ViewModel() {

    private val _coilUiStats = MutableStateFlow(CoilUiStats.from(app))
    val coilUiStats = _coilUiStats.asStateFlow()

    private val _actions = Channel<CoilAction>(capacity = Channel.CONFLATED).apply {
        receiveAsFlow().onEach { action ->
            Timber.i("action: $action")
            when (action) {
                is CoilAction.UpdateDiskPolicy -> {
                    _coilUiStats.update { it.copy(diskCachePolicy = action.policy) }
                }
                is CoilAction.UpdateMemoryPolicy -> {
                    _coilUiStats.update { it.copy(memoryCachePolicy = action.policy) }
                }
                is CoilAction.UpdateNetworkPolicy -> {
                    _coilUiStats.update { it.copy(networkCachePolicy = action.policy) }
                }
                is CoilAction.UpdateLogLevel -> storage.coilLogging(action.logLevel)
                CoilAction.Refresh -> _coilUiStats.tryEmit(CoilUiStats.from(app))
                CoilAction.TrimMemory -> {
                    // TODO: move this to IO UseCase
                    app.imageLoader.memoryCache.clear()
                    app.imageLoader.bitmapPool.trimMemory(ComponentCallbacks2.TRIM_MEMORY_COMPLETE)
                    app.imageLoader.bitmapPool.clear()
                    CoilUtils.createDefaultCache(app).directory.deleteRecursively()
                    Runtime.getRuntime().gc()
                    trySend(CoilAction.Refresh)
                }
                CoilAction.Reset -> _coilUiStats.tryEmit(
                    CoilUiStats.from(app).copy(
                        memoryCachePolicy = DefaultRequestOptions.INSTANCE.memoryCachePolicy,
                        diskCachePolicy = DefaultRequestOptions.INSTANCE.diskCachePolicy,
                        networkCachePolicy = DefaultRequestOptions.INSTANCE.networkCachePolicy,
                    ),
                )
            }
        }.launchIn(viewModelScope)
    }

    fun submitAction(action: CoilAction) {
        _actions.trySend(action)
    }

    init {
        _coilUiStats.map { it.memoryCachePolicy }.distinctUntilChanged().onEach {
            Coil.setImageLoader(app.imageLoader.newBuilder().memoryCachePolicy(it).build())
        }.launchIn(viewModelScope)

        _coilUiStats.map { it.diskCachePolicy }.distinctUntilChanged().onEach {
            Coil.setImageLoader(app.imageLoader.newBuilder().diskCachePolicy(it).build())
        }.launchIn(viewModelScope)

        _coilUiStats.map { it.networkCachePolicy }.distinctUntilChanged().onEach {
            Coil.setImageLoader(app.imageLoader.newBuilder().networkCachePolicy(it).build())
        }.launchIn(viewModelScope)

        storage.coilLogging.onEach { level ->
            _coilUiStats.update { it.copy(logLevel = level) }
        }.launchIn(viewModelScope)
    }
}

internal data class CoilUiStats(
    val policies: List<CachePolicy> = CachePolicy.values().asList(),
    val memoryCachePolicy: CachePolicy,
    val diskCachePolicy: CachePolicy,
    val networkCachePolicy: CachePolicy,
    val memorySizeBytes: Int,
    val memoryMaxSizeBytes: Int,
    val memoryPercentage: Float = (1f * memorySizeBytes / memoryMaxSizeBytes) * 100,
    val logLevel: CoilLogLevel = DebugStorage.Defaults.CoilLoggingLevel,
) {
    companion object {

        /**
         * TODO: we might consider below link for memory information
         * https://stackoverflow.com/questions/3170691/how-to-get-current-memory-usage-in-android/19267315#19267315
         */
        fun from(context: Context): CoilUiStats = CoilUiStats(
            memorySizeBytes = context.imageLoader.memoryCache.size,
            memoryMaxSizeBytes = context.imageLoader.memoryCache.maxSize,
            memoryCachePolicy = context.imageLoader.defaults.memoryCachePolicy,
            diskCachePolicy = context.imageLoader.defaults.diskCachePolicy,
            networkCachePolicy = context.imageLoader.defaults.networkCachePolicy,
        )
    }
}
