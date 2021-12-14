package app.playground.ui.debug

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.playground.ui.theme.PlaygroundTheme

@Composable
internal fun ColumnScope.DebugSettingsHeader(applicationName: CharSequence) {
    Row(
        modifier = Modifier
            .align(Alignment.End)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = applicationName.toString(),
                style = MaterialTheme.typography.subtitle1.copy(letterSpacing = 1.5.sp),
            )
            Text(
                text = "Debug Settings",
                style = MaterialTheme.typography.overline.copy(letterSpacing = 1.5.sp),
            )
        }
        Icon(
            imageVector = Icons.Default.BugReport,
            contentDescription = "",
            modifier = Modifier.size(40.dp),
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewHeader() {
    PlaygroundTheme {
        Column(modifier = Modifier.fillMaxWidth()) {
            DebugSettingsHeader("Application Name")
        }
    }
}
