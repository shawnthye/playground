package core.playground.ui

import android.content.Context
import androidx.annotation.StringRes
import core.playground.Reason

sealed class UiMessage {
    data class String(val message: kotlin.String) : UiMessage()
    data class Resource(@StringRes val resourceId: Int) : UiMessage()
    class ResourceFormat(@StringRes val resourceId: Int, vararg val formatArgs: Any) : UiMessage()
}

fun UiMessage.string(context: Context): String {
    return when (this) {
        is UiMessage.String -> message
        is UiMessage.Resource -> context.getString(resourceId)
        is UiMessage.ResourceFormat -> context.getString(resourceId, formatArgs)
    }
}

private val CONNECTION = UiMessage.Resource(R.string.reason_connection)
private val UNKNOWN = UiMessage.Resource(R.string.reason_unknown)

fun Throwable.asUiMessage() = asUiMessageOr { UNKNOWN }

fun Throwable.asUiMessageOr(
    transformHttpError: (Reason.HttpError) -> UiMessage?,
): UiMessage {
    return when (this) {
        is Reason.Connection -> CONNECTION
        is Reason.HttpError -> transformHttpError(this) ?: UNKNOWN
        else -> UNKNOWN
    }
}
