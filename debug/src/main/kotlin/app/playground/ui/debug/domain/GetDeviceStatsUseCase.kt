package app.playground.ui.debug.domain

import android.app.Application
import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Build
import android.view.Display
import androidx.core.content.getSystemService
import androidx.core.view.DisplayCompat
import app.playground.ui.debug.R
import core.playground.MainImmediateDispatcher
import core.playground.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import java.util.Locale
import javax.inject.Inject

internal class GetDeviceStatsUseCase @Inject constructor(
    private val app: Application,
    @MainImmediateDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Unit, Map<String, String>>(dispatcher) {

    override suspend fun execute(params: Unit): Map<String, String> = app.deviceStats
}

private val readableMaker
    get() = Build.MANUFACTURER.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
    }

private val readableModel get() = Build.MODEL.orEmpty().ifBlank { "UNKNOWN" }

private val Context.readableResolution: String
    get() {
        val manager = getSystemService<DisplayManager>()

        val display = manager?.getDisplay(Display.DEFAULT_DISPLAY) ?: return "UNKNOWN"

        val mode = DisplayCompat.getMode(this, display)

        return "${mode.physicalHeight}x${mode.physicalWidth}"
    }

private val Application.densityBucket get() = getString(R.string.debug_density_bucket_qualifier)
private val Application.densityScale get() = getString(R.string.debug_density_bucket_scale)

internal val Application.deviceStats: Map<String, String>
    get() = mapOf(
        "Make" to readableMaker,
        "Model" to readableModel,
        "Resolution" to readableResolution,
        "Density" to "${resources.displayMetrics.densityDpi} ($densityBucket) ($densityScale)",
        "Release" to Build.VERSION.RELEASE.orEmpty().ifBlank { "UNKNOWN" },
        "API" to "${Build.VERSION.SDK_INT}",
    )
