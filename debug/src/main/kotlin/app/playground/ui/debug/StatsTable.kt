package app.playground.ui.debug

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
internal fun StatsTable(modifier: Modifier = Modifier, stats: Map<String, String>) {
    Row(modifier = modifier) {
        Column(modifier = Modifier.padding(end = 16.dp)) {
            stats.keys.map {
                Text(
                    text = it,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f),
        ) {
            stats.values.map {
                Text(
                    text = it,
                    modifier = Modifier.padding(bottom = 4.dp),
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    StatsTable(
        modifier = Modifier.padding(16.dp),
        stats = mapOf(
            "Gríðr" to "1",
            "Vimal" to "2",
            "Gojko" to "3",
            "Anastázia" to "4",
            "Madhavi" to "5",
        ),
    )
}
