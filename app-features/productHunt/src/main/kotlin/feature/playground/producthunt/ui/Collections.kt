package feature.playground.producthunt.ui

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.statusBarsPadding
import core.playground.ui.components.DrawerAppBar
import feature.playground.producthunt.R

@Composable
fun Collections(navigateUp: () -> Unit) {
    Scaffold(
        topBar = {
            DrawerAppBar(
                titleRes = R.string.menu_collections, navigationUp = navigateUp,
            )
        },
    ) {
        Text(text = "Collections", modifier = Modifier.statusBarsPadding())
    }
}