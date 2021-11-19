package core.playground.ui.components

import androidx.annotation.StringRes
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.ElevationOverlay
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun DrawerAppBar(
    @StringRes titleRes: Int = -1,
    navigationUp: (() -> Unit)? = null,
) {
    AppBar(
        iconVector = Icons.Filled.Menu,
        titleRes = titleRes,
        navigationUp = navigationUp,
    )
}

@Composable
fun AppBar(
    iconVector: ImageVector = Icons.Filled.ArrowBack,
    @StringRes titleRes: Int = -1,
    navigationUp: (() -> Unit)? = null,
    elevation: Dp = AppBarDefaults.TopAppBarElevation,
    actions: @Composable (() -> Unit)? = null,
) {
    AppBar(
        title = stringResource(id = titleRes),
        navigationUp = navigationUp,
        elevation = elevation,
        actions = actions,
        iconVector = iconVector,
    )
}

@Composable
fun AppBar(
    iconVector: ImageVector = Icons.Filled.ArrowBack,
    title: String? = null,
    navigationUp: (() -> Unit)? = null,
    elevation: Dp = AppBarDefaults.TopAppBarElevation,
    actions: @Composable (() -> Unit)? = null,
) {
    /**
     * [ElevationOverlay] is auto apply in Night Mode.
     * But we wanted a more paper like experience even in Night Mode
     * see also https://developer.android.com/jetpack/compose/themes/material#elevation-overlays
     */
    CompositionLocalProvider(LocalElevationOverlay provides null) {
        Surface(
            color = MaterialTheme.colors.background,
            elevation = elevation,
        ) {
            TopAppBar(
                title = {
                    Text(text = title ?: "")
                },
                navigationIcon = {
                    IconButton(onClick = { navigationUp?.invoke() }) {
                        Icon(imageVector = iconVector, contentDescription = "")
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
}
