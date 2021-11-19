package feature.playground.producthunt.ui

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.statusBarsPadding
import core.playground.ui.components.DrawerAppBar

@Composable
fun Discover(navigateUp: () -> Unit) {
    Scaffold(
        topBar = {
            DrawerAppBar(
                titleRes = core.playground.ui.R.string.menu_discover, navigationUp = navigateUp,
            )
        },
    ) {
        Text(text = "Discover", modifier = Modifier.statusBarsPadding())
    }
}
