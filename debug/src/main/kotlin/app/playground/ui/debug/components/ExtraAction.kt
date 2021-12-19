package app.playground.ui.debug.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
internal fun ColumnScope.ExtraAction(label: String, onPress: () -> Unit, icon: DebugIcon?) {
    TextButton(
        modifier = Modifier
            .align(Alignment.End)
            .padding(horizontal = 16.dp),
        onClick = onPress,
    ) {
        Text(text = label)
        if (null != icon) {
            val painter = when (icon) {
                is DebugIcon.ResourceIcon -> painterResource(icon.resId)
                is DebugIcon.VectorIcon -> rememberVectorPainter(icon.vector)
            }

            Icon(
                painter = painter,
                contentDescription = null,
                tint = when (val tint = icon.tint) {
                    null -> LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                    else -> tint
                },
                modifier = Modifier.padding(start = 4.dp),
            )
        }
    }
}
