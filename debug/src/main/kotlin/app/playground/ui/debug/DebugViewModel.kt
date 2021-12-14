package app.playground.ui.debug

import android.app.Application
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.playground.ui.debug.data.DebugStorage
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
    app: Application,
) : ViewModel() {

    val applicationName = app.applicationInfo.loadLabel(app.packageManager)

    val seenDrawer = storage.seenDrawer.stateIn(
        viewModelScope, WhileViewSubscribed, true,
    )

    fun seenDrawer() {
        if (!seenDrawer.value) {
            viewModelScope.launch {
                storage.seenDrawer()
            }
        }
    }

    val httpLoggingLevel = storage.httpLoggingLevel.stateIn(
        viewModelScope, WhileViewSubscribed, DebugStorage.Defaults.OkhttpLoggingLevel,
    )

    fun updateHttpLoggingLevel(level: HttpLoggingInterceptor.Level) {
        viewModelScope.launch {
            storage.httpLoggingLevel(level = level)
        }
    }

    val deviceStats: Map<String, String> = app.deviceStats

    fun resetDebugSettings() {
        viewModelScope.launch {
            storage.clear()
        }
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
