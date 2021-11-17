package app.playground.ui.gallery

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.playground.R
import app.playground.navigation.TopAppbar
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun Gallery(
    openDrawer: () -> Unit,
) {
    Scaffold(topBar = { TopAppbar(title = R.string.menu_gallery, navigationClick = openDrawer) }) {
        Text(text = "gallery", modifier = Modifier.statusBarsPadding())
    }
}
