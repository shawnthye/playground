package app.playground.ui.debug.components

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

internal sealed class DebugIcon(open val tint: Color?) {
    data class ResourceIcon(
        @DrawableRes val resId: Int,
        override val tint: Color? = null,
    ) : DebugIcon(tint)

    data class VectorIcon(
        val vector: ImageVector,
        override val tint: Color? = null,
    ) : DebugIcon(tint)
}
