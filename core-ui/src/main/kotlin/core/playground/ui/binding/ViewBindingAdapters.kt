package core.playground.ui.binding

import android.annotation.SuppressLint
import android.graphics.Outline
import android.view.GestureDetector
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.view.ViewOutlineProvider
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.BindingAdapter
import timber.log.Timber

@BindingAdapter("clipToCircle")
fun View.clipToCircle(clipped: Boolean) {
    clipToOutline = clipped
    outlineProvider = if (clipped) CircularOutlineProvider else null
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

/**
 * some View like CollapsingToolbarLayout is consuming window insets by default,
 * so that children has no insets to consume.
 *
 * This is to disable those parent view to consume insets,
 * so that the child views can consume insets and adjust themself
 *
 * see issue https://github.com/material-components/material-components-android/issues/1310
 */
@BindingAdapter("disableWindowInsetsListener")
fun View.disableWindowInsetsListener(disabled: Boolean) {
    if (disabled) {
        ViewCompat.setOnApplyWindowInsetsListener(this, null)
    }
}

@BindingAdapter(
    "paddingLeftSystemWindowInsets",
    "paddingTopSystemWindowInsets",
    "paddingRightSystemWindowInsets",
    "paddingBottomSystemWindowInsets",
    requireAll = false,
)
fun View.applySystemWindowInsetsPadding(
    previousApplyLeft: Boolean,
    previousApplyTop: Boolean,
    previousApplyRight: Boolean,
    previousApplyBottom: Boolean,
    applyLeft: Boolean,
    applyTop: Boolean,
    applyRight: Boolean,
    applyBottom: Boolean,
) {
    if (previousApplyLeft == applyLeft &&
        previousApplyTop == applyTop &&
        previousApplyRight == applyRight &&
        previousApplyBottom == applyBottom
    ) {
        return
    }

    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

        view.setPadding(
            0,
            systemBars.top,
            0,
            0,
        )

        insets
    }
}

@SuppressLint("ClickableViewAccessibility")
@BindingAdapter("onSingleTap")
fun View.setSingleTapListener(action: () -> Unit) {
    isClickable = true
    val detector = GestureDetectorCompat(
        context,
        NoDoubleTapClickListener {
            performClick()
            performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            action()
        },
    )

    setOnTouchListener { _, event ->
        detector.onTouchEvent(event)
        false
    }
}

class NoDoubleTapClickListener(val singleTapped: () -> Unit) :
    GestureDetector.SimpleOnGestureListener() {

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        Timber.i("Double Tap detected, forfeited")
        return false
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        Timber.i("Single Tap detected")
        singleTapped()
        return true
    }
}
