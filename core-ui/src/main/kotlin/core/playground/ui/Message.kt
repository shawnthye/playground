package core.playground.ui

import android.content.Context
import androidx.annotation.StringRes
import core.playground.Reason

sealed class Message {
    data class Raw(val message: String) : Message()
    data class Resource(@StringRes val resourceId: Int) : Message()

    fun toString(context: Context): String {
        return when (this) {
            is Raw -> message
            is Resource -> context.getString(resourceId)
        }
    }

    companion object {
        fun forError(t: Throwable): Message {
            return when (t) {
                is Reason.Connection -> Resource(R.string.reason_connection)
                is Reason.HttpError -> Raw(t.message)
                else -> Resource(R.string.reason_unknown)
            }
        }
    }
}
