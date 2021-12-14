package app.playground.ui.debug

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.playground.ui.debug.data.CoilLogLevel
import app.playground.ui.debug.data.DebugStorage
import coil.Coil
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.DefaultRequestOptions
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
import java.util.Locale
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
                    app.imageLoader.memoryCache.clear()
                    app.imageLoader.bitmapPool.clear()
                    app.imageLoader.bitmapPool.trimMemory(ComponentCallbacks2.TRIM_MEMORY_COMPLETE)
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
        fun from(context: Context): CoilUiStats = CoilUiStats(
            memorySizeBytes = context.imageLoader.memoryCache.size,
            memoryMaxSizeBytes = context.imageLoader.memoryCache.maxSize,
            memoryCachePolicy = context.imageLoader.defaults.memoryCachePolicy,
            diskCachePolicy = context.imageLoader.defaults.diskCachePolicy,
            networkCachePolicy = context.imageLoader.defaults.networkCachePolicy,
        )
    }
}

private val readableMaker
    get() = Build.MANUFACTURER.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
    }

private val readableModel get() = Build.MODEL.orEmpty().ifBlank { "UNKNOWN" }

private val Resources.readableResolution
    get() = "${displayMetrics.heightPixels}x${displayMetrics.widthPixels}"

private val Resources.densityBucket
    get() = when (displayMetrics.densityDpi) {
        DisplayMetrics.DENSITY_LOW -> "ldpi"
        DisplayMetrics.DENSITY_MEDIUM -> "mdpi"
        DisplayMetrics.DENSITY_HIGH -> "hdpi"
        DisplayMetrics.DENSITY_XHIGH -> "xhdpi"
        DisplayMetrics.DENSITY_XXHIGH -> "xxhdpi"
        DisplayMetrics.DENSITY_XXXHIGH -> "xxxhdpi"
        DisplayMetrics.DENSITY_TV -> "tvdpi"
        else -> "${displayMetrics.densityDpi}"
    }

private val Application.deviceStats: Map<String, String>
    get() = mapOf(
        "Make" to readableMaker,
        "Model" to readableModel,
        "Resolution" to resources.readableResolution,
        "Density" to "${resources.displayMetrics.densityDpi} (${resources.densityBucket})",
        "Release" to Build.VERSION.RELEASE.orEmpty().ifBlank { "UNKNOWN" },
        "API" to "${Build.VERSION.SDK_INT}",
    )
