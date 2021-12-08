package core.playground.ui.binding

import android.annotation.SuppressLint
import android.graphics.Outline
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.GestureDetector
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.view.ViewOutlineProvider
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.BindingAdapter
import timber.log.Timber

@BindingAdapter("goneUnless")
fun goneUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("goneOnNothing")
fun goneOnNothing(view: View, thing: Any?) {
    val visible = when (thing) {
        is String -> thing.isNotBlank()
        else -> thing != null
    }

    view.visibility = if (visible) View.VISIBLE else View.GONE
}

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

@BindingAdapter(
    value = ["layout_constraintDimensionRatioWidth", "layout_constraintDimensionRatioHeight"],
    requireAll = true,
)
fun constraintDimensionRatio(
    view: View,
    layout_constraintDimensionRatioWidth: Int,
    layout_constraintDimensionRatioHeight: Int,
) {
    val params = view.layoutParams as? ConstraintLayout.LayoutParams
        ?: throw IllegalArgumentException("view's parent must be Constraint Layout")

    params.dimensionRatio =
        "$layout_constraintDimensionRatioWidth:$layout_constraintDimensionRatioHeight"

    view.layoutParams = params
}

/**
 * Note: the limitation of this is we can't get the parent layout width,
 * this calculate base on the entire screen size
 */
@BindingAdapter("layout_constraintMaxHeight_percent")
fun constraintDimensionRatio(view: View, layout_constraintMaxHeight_percent: Double) {
    val params = view.layoutParams as? ConstraintLayout.LayoutParams
        ?: throw IllegalArgumentException("view's parent must be Constraint Layout")

    val screenHeight = view.currentWindowMetricsBounds().height()

    params.matchConstraintMaxHeight = (screenHeight * layout_constraintMaxHeight_percent).toInt()
}

private fun View.currentWindowMetricsBounds(): Rect {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        return context.getSystemService(WindowManager::class.java).currentWindowMetrics.bounds
    }

    val metrics = DisplayMetrics()
    @Suppress("DEPRECATION")
    ViewCompat.getDisplay(this)?.getMetrics(metrics)
    return Rect(0, 0, metrics.widthPixels, metrics.heightPixels)
}
