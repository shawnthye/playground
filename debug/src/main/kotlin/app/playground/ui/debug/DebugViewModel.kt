package app.playground.ui.debug

import android.app.Application
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.playground.ui.debug.data.DebugEnvironment
import app.playground.ui.debug.data.DebugStorage
import app.playground.ui.debug.data.DebugStorage.Defaults
import core.playground.ui.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.logging.HttpLoggingInterceptor
import java.util.Locale
import javax.inject.Inject
import kotlin.system.exitProcess

@HiltViewModel
internal class DebugViewModel @Inject constructor(
    private val storage: DebugStorage,
    private val app: Application,
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

    val environment = storage.environment.stateIn(
        viewModelScope, WhileViewSubscribed, Defaults.Environment,
    )

    fun updateEnvironment(environment: DebugEnvironment) {
        viewModelScope.launch {
            storage.environment(environment)
            app.restart()
        }
    }

    val httpLoggingLevel = storage.httpLoggingLevel.stateIn(
        viewModelScope, WhileViewSubscribed, Defaults.OkhttpLoggingLevel,
    )

    fun updateHttpLoggingLevel(level: HttpLoggingInterceptor.Level) {
        viewModelScope.launch {
            storage.httpLoggingLevel(level = level)
        }
    }

    val deviceStats: Map<String, String> = app.deviceStats

    fun resetDebugSettings() {
        viewModelScope.launch {
            val shouldRestart = environment.value != Defaults.Environment
            storage.clear()
            if (shouldRestart) {
                app.restart()
            }
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

private fun Application.restart() {
    packageManager.getLaunchIntentForPackage(packageName)?.let {
        Intent.makeRestartActivityTask(it.component)
    }?.also {
        startActivity(it)

        /**
         * We should never ever do this in Production :)
         */
        exitProcess(0)
    } ?: Toast.makeText(
        this,
        "Missing android.intent.category.LAUNCHER",
        Toast.LENGTH_LONG,
    ).show()
}
