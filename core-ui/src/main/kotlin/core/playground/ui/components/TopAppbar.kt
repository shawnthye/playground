package core.playground.ui.components

import androidx.annotation.StringRes
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun TopAppbar(
    @StringRes title: Int = -1,
    navigationClick: (() -> Unit)? = null,
    elevation: Dp = AppBarDefaults.TopAppBarElevation,
    actions: @Composable (() -> Unit)? = null,
) {
    TopAppbar(
        title = stringResource(id = title),
        navigationUp = navigationClick,
        elevation = elevation,
        actions = actions,
    )
}

@Composable
fun TopAppbar(
    title: String? = null,
    navigationUp: (() -> Unit)? = null,
    elevation: Dp = AppBarDefaults.TopAppBarElevation,
    actions: @Composable (() -> Unit)? = null,
) {
    Surface(
        color = MaterialTheme.colors.surface,
        elevation = elevation,
    ) {
        TopAppBar(
            title = {
                Text(text = title ?: "")
            },
            navigationIcon = {
                IconButton(onClick = { navigationUp?.invoke() }) {
                    Icon(imageVector = Icons.Filled.Menu, contentDescription = "")
                }
            },
            actions = {
                actions?.invoke()
            },
            backgroundColor = Color.Transparent,
            modifier = Modifier.statusBarsPadding(),
            elevation = 0.dp,
        )
    }
}
