package app.playground.ui.debug.components

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

internal sealed class DebugIcon {
    abstract val tint: Color?

    data class ResourceIcon(
        @DrawableRes val resId: Int,
        override val tint: Color? = null,
    ) : DebugIcon()

    data class VectorIcon(
        val vector: ImageVector,
        override val tint: Color? = null,
    ) : DebugIcon()
}
