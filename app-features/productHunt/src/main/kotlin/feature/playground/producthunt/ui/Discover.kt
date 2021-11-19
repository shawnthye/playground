package feature.playground.producthunt.ui

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.statusBarsPadding
import core.playground.ui.components.DrawerAppBar

@Composable
fun Discover(navigateUp: () -> Unit, openPost: (postId: String) -> Unit) {
    Scaffold(
        topBar = {
            DrawerAppBar(
                titleRes = core.playground.ui.R.string.menu_discover, navigationUp = navigateUp,
            )
        },
    ) {
        TextButton(onClick = { openPost("post from discover") }) {
            Text(text = "Discover", modifier = Modifier.statusBarsPadding())
        }
    }
}
