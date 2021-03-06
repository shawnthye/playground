package feature.playground.demos

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsWithImePadding
import core.playground.ui.R
import core.playground.ui.alias.NavigateUp
import core.playground.ui.theme.ThemeIcons

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun DemoAppBar(
    navigateUp: NavigateUp,
    selected: DemoScreen,
    sheetValue: ModalBottomSheetValue,
) {
    CompositionLocalProvider(LocalElevationOverlay provides null) {
        Surface(
            color = MaterialTheme.colors.surface,
            elevation = AppBarDefaults.BottomAppBarElevation,
        ) {
            BottomAppBar(
                modifier = Modifier.navigationBarsWithImePadding(),
                elevation = 0.dp,
                backgroundColor = Color.Transparent,
            ) {
                MenuButton(navigateUp = navigateUp, selected = selected, sheetValue = sheetValue)
            }
            if (!MaterialTheme.colors.isLight) {
                Divider(thickness = 0.6.dp)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MenuButton(
    navigateUp: NavigateUp,
    selected: DemoScreen,
    sheetValue: ModalBottomSheetValue,
) {
    val degrees by animateFloatAsState(
        targetValue = if (sheetValue != ModalBottomSheetValue.Expanded) {
            180f
        } else {
            360f
        },
    )

    Spacer(modifier = Modifier.width(4.dp))
    TextButton(onClick = navigateUp) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.rotate(degrees),
                imageVector = ThemeIcons.ArrowDropDown,
                contentDescription = "Open Menu",
                tint = MaterialTheme.colors.onSurface,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Outlined.Category,
                contentDescription = "Open Menu",
                tint = colorResource(id = R.color.brand),
            )

            AnimatedVisibility(
                visible = sheetValue == ModalBottomSheetValue.Hidden,
                enter = fadeIn(tween(120)) + expandHorizontally(tween(120)),
                exit = fadeOut(tween(120)) + shrinkHorizontally(tween(120)),
            ) {
                Row {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = selected.title, color = MaterialTheme.colors.onSurface)
                }
            }
        }
    }
}
