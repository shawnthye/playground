package app.playground.ui.debug

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.playground.ui.debug.data.CoilLogLevel
import app.playground.ui.debug.data.DebugStorage
import coil.Coil
import coil.ImageLoader
import coil.imageLoader
import coil.request.CachePolicy
import core.playground.ui.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.logging.HttpLoggingInterceptor
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
internal class DebugViewModel @Inject constructor(
    private val storage: DebugStorage,
    private val app: Application,
) : ViewModel() {

    val httpLoggingLevel = storage.httpLoggingLevel.stateIn(
        viewModelScope, WhileViewSubscribed, DebugStorage.Defaults.OkhttpLoggingLevel,
    )

    fun updateHttpLoggingLevel(level: HttpLoggingInterceptor.Level) {
        viewModelScope.launch {
            storage.httpLoggingLevel(level = level)
        }
    }

    private val _coilUiStats = mutableStateOf(CoilUiStats.create(app.imageLoader))
    val coilUiStats by _coilUiStats

    val coilLogging = storage.coilLogging.stateIn(
        viewModelScope, WhileViewSubscribed, DebugStorage.Defaults.CoilLoggingLevel,
    )

    fun coilRefreshStats() {
        _coilUiStats.value = coilUiStats.copy(
            sizeBytes = app.imageLoader.memoryCache.size,
            maxSizeBytes = app.imageLoader.memoryCache.maxSize,
            memoryCachePolicy = app.imageLoader.defaults.memoryCachePolicy,
            diskCachePolicy = app.imageLoader.defaults.diskCachePolicy,
            networkCachePolicy = app.imageLoader.defaults.memoryCachePolicy,
        )
    }

    fun coilUpdateMemoryCachePolicy(policy: CachePolicy) {
        app.imageLoader.shutdown()
        Coil.setImageLoader(app.imageLoader.newBuilder().memoryCachePolicy(policy).build())
        coilRefreshStats()
    }

    fun coilUpdateDiskCachePolicy(policy: CachePolicy) {
        app.imageLoader.shutdown()
        Coil.setImageLoader(app.imageLoader.newBuilder().diskCachePolicy(policy).build())
        coilRefreshStats()
    }

    fun coilUpdateNetworkCachePolicy(policy: CachePolicy) {
        app.imageLoader.shutdown()
        Coil.setImageLoader(app.imageLoader.newBuilder().networkCachePolicy(policy).build())
        coilRefreshStats()
    }

    fun coilSetLogLevel(level: CoilLogLevel) {
        viewModelScope.launch {
            storage.coilLogging(level = level)
        }
    }

    fun coilTrimMemory() {
        app.imageLoader.memoryCache.clear()
        app.imageLoader.bitmapPool.clear()
        app.imageLoader.bitmapPool.trimMemory(ComponentCallbacks2.TRIM_MEMORY_COMPLETE)
        coilRefreshStats()
    }

    val deviceStats: Map<String, String> = app.deviceStats
}

internal data class CoilUiStats(
    val policies: List<CachePolicy> = CachePolicy.values().asList(),
    val memoryCachePolicy: CachePolicy,
    val diskCachePolicy: CachePolicy,
    val networkCachePolicy: CachePolicy,
    val sizeBytes: Int,
    val maxSizeBytes: Int,
) {
    companion object {
        fun create(coil: ImageLoader): CoilUiStats = CoilUiStats(
            sizeBytes = coil.memoryCache.size,
            maxSizeBytes = coil.memoryCache.maxSize,
            memoryCachePolicy = coil.defaults.memoryCachePolicy,
            diskCachePolicy = coil.defaults.diskCachePolicy,
            networkCachePolicy = coil.defaults.networkCachePolicy,
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
