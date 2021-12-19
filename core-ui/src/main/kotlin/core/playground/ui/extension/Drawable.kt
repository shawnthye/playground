package core.playground.ui.extension

import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import coil.drawable.MovieDrawable
import coil.drawable.ScaleDrawable

fun Drawable.asGif(): Animatable? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        if (this is AnimatedImageDrawable) {
            return this
        } else if (this is ScaleDrawable && child is AnimatedImageDrawable) {
            return this
        }
    } else {
        if (this is MovieDrawable) {
            return this
        } else if (this is ScaleDrawable && child is MovieDrawable) {
            return this
        }
    }

    return null
}
