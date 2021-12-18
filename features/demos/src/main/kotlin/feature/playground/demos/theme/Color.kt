package feature.playground.demos.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import core.playground.ui.theme.ThemeIcons
import core.playground.ui.theme.onSurfaceEmphasisMedium

@Composable
internal fun Color() {
    ThemeLayout {
        Item(
            label = "MaterialTheme.colors.primary",
            color = MaterialTheme.colors.primary,
        )
        Item(
            label = "MaterialTheme.colors.primaryVariant",
            color = MaterialTheme.colors.primaryVariant,
        )
        Item(
            label = "MaterialTheme.colors.primarySurface",
            color = MaterialTheme.colors.primarySurface,
        )
        Item(
            label = "MaterialTheme.colors.secondary",
            color = MaterialTheme.colors.secondary,
        )
        Item(
            label = "MaterialTheme.colors.secondaryVariant",
            color = MaterialTheme.colors.secondaryVariant,
        )
        Item(
            label = "MaterialTheme.colors.surface",
            color = MaterialTheme.colors.surface,
        )
        Item(
            label = "MaterialTheme.colors.background",
            color = MaterialTheme.colors.background,
        )
        Item(
            label = "MaterialTheme.colors.error",
            color = MaterialTheme.colors.error,
        )
        Item(
            label = "MaterialTheme.colors.onPrimary",
            color = MaterialTheme.colors.onPrimary,
        )
        Item(
            label = "MaterialTheme.colors.onSecondary",
            color = MaterialTheme.colors.onSecondary,
        )
        Item(
            label = "MaterialTheme.colors.onBackground",
            color = MaterialTheme.colors.onBackground,
        )
        Item(
            label = "MaterialTheme.colors.onSurface",
            color = MaterialTheme.colors.onSurface,
        )
        Item(
            label = "MaterialTheme.colors.onError",
            color = MaterialTheme.colors.onError,
        )
    }
}

@Composable
private fun ColumnScope.Item(label: String, color: Color) {
    ThemeLines(label = label) {
        Surface(
            shape = CircleShape,
            color = color,
            modifier = Modifier.size(24.dp),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colors.onSurfaceEmphasisMedium,
            ),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = ThemeIcons.Circle,
                    contentDescription = label,
                    tint = MaterialTheme.colors.contentColorFor(color),
                    modifier = Modifier.size(8.dp),
                )
            }
        }
    }
}
