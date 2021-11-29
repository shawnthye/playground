package feature.playground.deviant.widget

import android.graphics.Bitmap
import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.annotation.ExperimentalCoilApi
import coil.drawable.ScaleDrawable
import coil.request.ImageRequest
import coil.request.ImageResult
import coil.transition.CrossfadeTransition
import coil.transition.Transition
import coil.transition.TransitionTarget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

private fun Drawable.getBitmap(): Bitmap? {
    if (this is ScaleDrawable) {
        if (child is BitmapDrawable) {
            return (child as BitmapDrawable).bitmap
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P &&
            child is AnimatedImageDrawable
        ) {
            return this.toBitmap()
        }
    }

    if (this is BitmapDrawable) {
        return bitmap
    }

    return null
}

@OptIn(ExperimentalCoilApi::class)
internal fun ImageRequest.Builder.withPalette(
    onGenerated: (Palette?) -> Unit,
) {
    crossfade(false)
    transition(PaletteTransition { onGenerated(it) })
}

@OptIn(ExperimentalCoilApi::class)
private class PaletteTransition(
    crossFade: Boolean = true,
    private val onGenerated: (Palette?) -> Unit,
) : Transition {
    val delegate = if (crossFade) CrossfadeTransition(200) else Transition.NONE

    override suspend fun transition(target: TransitionTarget, result: ImageResult) {
        delegate.transition(target, result)

        val palette = withContext(Dispatchers.IO) {
            result.drawable?.getBitmap()?.let {
                try {
                    Palette.from(it).clearFilters().generate()
                } catch (t: Throwable) {
                    Timber.e(t)
                    null
                }
            }
        }

        onGenerated(palette)
    }
}
