package feature.playground.demos.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
internal fun Shape() {
    ThemeContent {
        ShapeLines(
            label = "MaterialTheme.shapes.small",
            text = "S",
            shape = MaterialTheme.shapes.small,
        )
        ShapeLines(
            label = "MaterialTheme.shapes.medium",
            text = "M",
            shape = MaterialTheme.shapes.medium,
        )
        ShapeLines(
            label = "MaterialTheme.shapes.large",
            text = "L",
            shape = MaterialTheme.shapes.large,
        )
    }
}

@Composable
private fun ColumnScope.ShapeLines(label: String, text: String, shape: Shape) {
    ThemeLines(label = label) {
        Surface(
            shape = shape,
            color = MaterialTheme.colors.primarySurface,
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.onSurface),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(50.dp)
                    .aspectRatio(1.25f),
            ) {
                Text(text = text, modifier = Modifier.padding(bottom = 2.dp))
            }
        }
    }
}
