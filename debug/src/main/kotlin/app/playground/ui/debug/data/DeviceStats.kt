package app.playground.ui.debug.data

import android.app.Application
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import java.util.Locale

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

internal val Application.deviceStats: Map<String, String>
    get() = mapOf(
        "Make" to readableMaker,
        "Model" to readableModel,
        "Resolution" to resources.readableResolution,
        "Density" to "${resources.displayMetrics.densityDpi} (${resources.densityBucket})",
        "Release" to Build.VERSION.RELEASE.orEmpty().ifBlank { "UNKNOWN" },
        "API" to "${Build.VERSION.SDK_INT}",
    )
