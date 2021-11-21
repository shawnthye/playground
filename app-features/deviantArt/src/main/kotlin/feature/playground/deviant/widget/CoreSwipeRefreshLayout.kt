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
fun SwipeRefreshLayout.onRefreshListener(listener: SwipeRefreshLayout.OnRefreshListener) {
    setOnRefreshListener(listener)
}

@BindingAdapter("refreshing")
fun SwipeRefreshLayout.refreshing(refreshing: Boolean) {
    isRefreshing = refreshing
}

@BindingAdapter("enabled")
fun SwipeRefreshLayout.enabled(enabled: Boolean) {
    isEnabled = enabled
}
