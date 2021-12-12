package app.playground.ui.debug

import android.app.Application
import android.content.ComponentCallbacks2
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import coil.ImageLoader
import coil.imageLoader
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class DebugViewModel @Inject constructor(
    private val app: Application,
) : ViewModel() {

    private val _coilUiStats = mutableStateOf(CoilUiStats.create(app.imageLoader))
    val coilUiStats by _coilUiStats

    fun coilRefreshStats() {
        _coilUiStats.value = coilUiStats.copy(
            sizeBytes = app.imageLoader.memoryCache.size,
            maxSizeBytes = app.imageLoader.memoryCache.maxSize,
        )
    }

    fun coilTrimMemory() {
        app.imageLoader.memoryCache.clear()
        app.imageLoader.bitmapPool.clear()
        app.imageLoader.bitmapPool.trimMemory(ComponentCallbacks2.TRIM_MEMORY_COMPLETE)
    }
}

internal data class CoilUiStats(val sizeBytes: Int, val maxSizeBytes: Int) {
    companion object {
        fun create(coil: ImageLoader): CoilUiStats = CoilUiStats(
            sizeBytes = coil.memoryCache.size,
            maxSizeBytes = coil.memoryCache.maxSize,
        )
    }
}
