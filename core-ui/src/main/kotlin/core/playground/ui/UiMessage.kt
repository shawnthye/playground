package core.playground.ui

import android.content.Context
import androidx.annotation.StringRes
import core.playground.Reason

sealed class UiMessage {
    data class String(val message: kotlin.String) : UiMessage()
    data class Resource(@StringRes val resourceId: Int) : UiMessage()
    class ResourceFormat(@StringRes val resourceId: Int, vararg val formatArgs: Any) : UiMessage()

    fun toString(context: Context): kotlin.String {
        return when (this) {
            is String -> message
            is Resource -> context.getString(resourceId)
            is ResourceFormat -> context.getString(resourceId, formatArgs)
        }
    }

    companion object {
        fun forError(t: Throwable): UiMessage {
            return when (t) {
                is Reason.Connection -> Resource(R.string.reason_connection)
                is Reason.HttpError -> String(t.message)
                else -> Resource(R.string.reason_unknown)
            }
        }
    }
}

private val CONNECTION = UiMessage.Resource(R.string.reason_connection)
private val UNKNOWN = UiMessage.Resource(R.string.reason_unknown)

fun Throwable.asUiMessageOr(transformHttpError: (Reason.HttpError) -> UiMessage): UiMessage {
    return when (this) {
        is Reason.Connection -> CONNECTION
        is Reason.HttpError -> transformHttpError(this)
        else -> UNKNOWN
    }
}
