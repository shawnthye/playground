package feature.playground.demos.theme

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import core.playground.ui.theme.onSurfaceEmphasisHighType

@Composable
internal fun Type() {
    ThemeLayout {
        TypeLines(
            label = "MaterialTheme.typography.h1",
            text = "H1",
            style = MaterialTheme.typography.h1,
        )
        TypeLines(
            label = "MaterialTheme.typography.h2",
            text = "H2",
            style = MaterialTheme.typography.h2,
        )
        TypeLines(
            label = "MaterialTheme.typography.h3",
            text = "H3",
            style = MaterialTheme.typography.h3,
        )
        TypeLines(
            label = "MaterialTheme.typography.h4",
            text = "H4",
            style = MaterialTheme.typography.h4,
        )
        TypeLines(
            label = "MaterialTheme.typography.h5",
            text = "H5",
            style = MaterialTheme.typography.h5,
        )
        TypeLines(
            label = "MaterialTheme.typography.h6",
            text = "H6",
            style = MaterialTheme.typography.h6,
        )
        TypeLines(
            label = "MaterialTheme.typography.subtitle1",
            text = "Subtitle 1",
            style = MaterialTheme.typography.subtitle1,
        )
        TypeLines(
            label = "MaterialTheme.typography.subtitle2",
            text = "Subtitle 2",
            style = MaterialTheme.typography.subtitle2,
        )
        TypeLines(
            label = "MaterialTheme.typography.body1",
            text = "Body 1",
            style = MaterialTheme.typography.body1,
        )
        TypeLines(
            label = "MaterialTheme.typography.body2",
            text = "Body 2",
            style = MaterialTheme.typography.body2,
        )
        TypeLines(
            label = "MaterialTheme.typography.button",
            text = "BUTTON",
            style = MaterialTheme.typography.button,
        )
        TypeLines(
            label = "MaterialTheme.typography.caption",
            text = "Caption",
            style = MaterialTheme.typography.caption,
        )
        TypeLines(
            label = "MaterialTheme.typography.overline",
            text = "OVERLINE",
            style = MaterialTheme.typography.overline,
        )
    }
}

@Composable
private fun ColumnScope.TypeLines(label: String, text: String, style: TextStyle) {
    ThemeLines(label = label) {
        Text(
            text = text,
            style = style,
            color = MaterialTheme.colors.onSurfaceEmphasisHighType,
        )
    }
}
