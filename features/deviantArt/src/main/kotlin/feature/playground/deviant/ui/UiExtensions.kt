package feature.playground.deviant.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.core.content.ContextCompat

val Context.selectableItemBackground: Drawable
    get() {
        val value = TypedValue()
        if (theme.resolveAttribute(
                androidx.appcompat.R.attr.selectableItemBackground,
                value,
                true,
            )
        ) {
            return ContextCompat.getDrawable(this, value.resourceId)!!
        }

        if (theme.resolveAttribute(
                android.R.attr.selectableItemBackground,
                value,
                true,
            )
        ) {
            return ContextCompat.getDrawable(this, value.resourceId)!!
        }

        throw IllegalAccessException("Couldn't find selectableItemBackground from your theme")
    }
