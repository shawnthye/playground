package feature.playground.deviant.widget

import android.content.Context
import android.util.AttributeSet
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class CoreSwipeRefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : SwipeRefreshLayout(context, attrs)

@BindingAdapter("onRefreshListener")
fun onRefreshListener(view: SwipeRefreshLayout, listener: SwipeRefreshLayout.OnRefreshListener) {
    view.setOnRefreshListener(listener)
}

@BindingAdapter("refreshing")
fun refreshing(view: SwipeRefreshLayout, refreshing: Boolean) {
    view.post {
        // view.setRefreshing()
        view.isRefreshing = refreshing
    }
}

@BindingAdapter("enabled")
fun enabled(view: SwipeRefreshLayout, enabled: Boolean) {
    view.isEnabled = enabled
}
