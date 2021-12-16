package feature.playground.producthunt.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import core.playground.ui.components.TopAppBar
import core.playground.ui.theme.contentPadding
import feature.playground.producthunt.R

@Composable
fun Collections() {
    Scaffold(
        topBar = { TopAppBar(titleRes = R.string.menu_collections) },
    ) { innerPadding ->
        Surface(modifier = Modifier
            .padding(innerPadding)
            .contentPadding()) {
            Text(text = "Collections")
        }
    }
}
