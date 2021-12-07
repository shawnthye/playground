package feature.playground.product.hunt.posts.ui

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.statusBarsPadding
import core.playground.ui.components.DrawerAppBar

@Composable
fun Posts(navigateUp: () -> Unit) {
    Scaffold(
        topBar = {
            DrawerAppBar(
                titleRes = core.playground.ui.R.string.menu_posts, navigationUp = navigateUp,
            )
        },
    ) {
        Text(text = "Collections", modifier = Modifier.statusBarsPadding())
    }
}
