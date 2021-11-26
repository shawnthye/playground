package core.playground.ui.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.DateFormat
import java.util.Date

@BindingAdapter("date")
fun TextView.date(date: Date?) {

    val safeDate = date ?: return run {
        text = null
    }

    text = DateFormat.getDateInstance().format(safeDate)
}
