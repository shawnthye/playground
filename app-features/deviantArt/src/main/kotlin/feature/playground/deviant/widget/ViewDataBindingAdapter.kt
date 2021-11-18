package feature.playground.deviant.widget

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter

@BindingAdapter("goneUnless")
fun goneUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
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
