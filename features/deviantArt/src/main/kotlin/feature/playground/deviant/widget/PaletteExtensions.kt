package feature.playground.deviant.widget

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.RippleDrawable
import androidx.annotation.ColorInt
import androidx.palette.graphics.Palette

object PaletteExtensions {

    @ColorInt
    fun Palette.findRippleColor(): Int? {
        val swatch = vibrantSwatch
            ?: lightVibrantSwatch
            ?: darkVibrantSwatch
            ?: mutedSwatch
            ?: lightMutedSwatch
            ?: darkMutedSwatch
        return swatch?.rgb
    }

    fun Palette.createRipple(borderless: Boolean): RippleDrawable? {
        val color = findRippleColor()
        return color?.createRipple(borderless)
    }
}

fun Int.createRipple(borderless: Boolean) = RippleDrawable(
    ColorStateList.valueOf(this),
    null,
    if (borderless) null else ColorDrawable(Color.WHITE),
)
