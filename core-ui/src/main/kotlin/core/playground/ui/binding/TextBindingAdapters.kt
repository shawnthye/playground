package core.playground.ui.binding

import android.widget.TextView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import core.playground.ui.UiMessage
import core.playground.ui.string
import java.text.DateFormat
import java.util.Date

/** Set text on a [TextView] from a string resource. */
@BindingAdapter("android:text")
fun setText(view: TextView, @StringRes resId: Int) {
    if (resId == 0) {
        view.text = null
    } else {
        view.setText(resId)
    }
}

@BindingAdapter("android:text")
fun setText(view: TextView, uiMessage: UiMessage?) {
    view.text = uiMessage?.string(context = view.context)
}

@BindingAdapter("date")
fun TextView.date(date: Date?) {

    val safeDate = date ?: return run {
        text = null
    }

    text = DateFormat.getDateInstance().format(safeDate)
}
