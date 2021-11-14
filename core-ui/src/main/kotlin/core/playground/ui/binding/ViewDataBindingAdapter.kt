package core.playground.ui.binding

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import core.playground.ui.currentWindowMetricsBounds

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

@BindingAdapter("app:onRefreshListener")
fun onRefreshListener(view: SwipeRefreshLayout, listener: SwipeRefreshLayout.OnRefreshListener) {
    view.setOnRefreshListener(listener)
}

@BindingAdapter("app:refreshing")
fun onRefreshListener(view: SwipeRefreshLayout, refreshing: Boolean) {
    view.isRefreshing = refreshing
}
