package core.playground.ui

import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import androidx.core.view.ViewCompat

fun View.currentWindowMetricsBounds(): Rect {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        return context.getSystemService(WindowManager::class.java).currentWindowMetrics.bounds
    }

    val metrics = DisplayMetrics()
    @Suppress("DEPRECATION")
    ViewCompat.getDisplay(this)?.getMetrics(metrics)
    return Rect(0, 0, metrics.widthPixels, metrics.heightPixels)
}
