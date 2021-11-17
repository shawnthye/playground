package app.playground.ui.gallery

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun Gallery() {
    Scaffold {
        Text(text = "gallery", modifier = Modifier.statusBarsPadding())
    }
}
