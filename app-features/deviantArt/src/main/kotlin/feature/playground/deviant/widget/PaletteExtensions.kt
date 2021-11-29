package feature.playground.deviant.widget

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.RippleDrawable
import androidx.palette.graphics.Palette

object PaletteExtensions {

    private fun Palette.toRippleColorStateList(): ColorStateList? {
        val swatch = vibrantSwatch
            ?: lightVibrantSwatch
            ?: darkVibrantSwatch
            ?: mutedSwatch
            ?: lightMutedSwatch
            ?: darkMutedSwatch

        return swatch?.rgb?.let { ColorStateList.valueOf(it) }
    }

    fun Palette.createRipple(borderless: Boolean): RippleDrawable? {

        val color = toRippleColorStateList()

        return color?.let {
            RippleDrawable(it, null, if (borderless) null else ColorDrawable(Color.WHITE))
        }
    }
}
