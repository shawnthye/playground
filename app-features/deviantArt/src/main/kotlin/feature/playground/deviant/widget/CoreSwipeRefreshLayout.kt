package feature.playground.deviant.widget

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class CoreSwipeRefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : SwipeRefreshLayout(context, attrs) {
    init {
        setProgressViewOffset(true, 0, progressViewEndOffset - (progressCircleDiameter / 2))

        val value = TypedValue()

        if (context.theme.resolveAttribute(
                com.google.android.material.R.attr.colorSurface,
                value,
                true,
            )
        ) {
            setColorSchemeColors(value.data)
        }

        if (context.theme.resolveAttribute(
                com.google.android.material.R.attr.colorPrimary,
                value,
                true,
            )
        ) {
            setProgressBackgroundColorSchemeColor(value.data)
        }
    }
}

@BindingAdapter("onRefreshListener")
fun SwipeRefreshLayout.onRefreshListener(listener: SwipeRefreshLayout.OnRefreshListener) {
    setOnRefreshListener(listener)
}

@BindingAdapter("refreshing")
fun SwipeRefreshLayout.refreshing(refreshing: Boolean) {
    if (!refreshing) {
        // set a delay for canceling the indicator, cause of network is too fast
        postDelayed({ isRefreshing = refreshing }, 800)
    } else {
        isRefreshing = refreshing
    }
}

@BindingAdapter("enabled")
fun SwipeRefreshLayout.enabled(enabled: Boolean) {
    isEnabled = enabled
}
