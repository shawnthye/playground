package core.playground.ui.binding

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import androidx.databinding.BindingAdapter

@BindingAdapter("clipToCircle")
fun View.clipToCircle(clip: Boolean) {
    clipToOutline = clip
    outlineProvider = if (clip) CircularOutlineProvider else null
}

object CircularOutlineProvider : ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {
        outline.setOval(
            view.paddingLeft,
            view.paddingTop,
            view.width - view.paddingRight,
            view.height - view.paddingBottom,
        )
    }
}
