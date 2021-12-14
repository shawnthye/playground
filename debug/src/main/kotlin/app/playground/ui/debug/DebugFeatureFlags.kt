package app.playground.ui.debug

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.statusBarsHeight

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DebugFeatureFlags() {
    LazyColumn {
        item {
            Spacer(modifier = Modifier.statusBarsHeight())
        }
        items(50) {
            ListItem(
                text = { Text("Coming soon $it") },
                icon = {
                    Icon(
                        Icons.Outlined.Flag,
                        contentDescription = "Localized description",
                    )
                },
            )
        }
    }
}
