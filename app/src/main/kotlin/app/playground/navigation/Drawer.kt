package app.playground.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.playground.R
import com.google.accompanist.insets.statusBarsPadding
import core.playground.ui.theme.PlaygroundTheme

@Composable
internal fun Drawer(
    selectedScreen: Screen,
    onNavigationSelected: (destination: Screen) -> Unit,
) {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize(),
    ) {
        MenuItems.map { menuItem ->
            val icon = when (menuItem) {
                is MenuItem.ResourceIcon -> ImageVector.vectorResource(id = menuItem.icon)
                is MenuItem.VectorIcon -> menuItem.icon
            }

            val iconSecondary = (menuItem as? MenuItem.ResourceIcon)
                ?.iconSecondary
                ?.let { ImageVector.vectorResource(id = it) }

            DrawerButton(
                icon = icon,
                subIcon = iconSecondary,
                label = stringResource(id = menuItem.label),
                isSelected = selectedScreen == menuItem.screen,
            ) {
                onNavigationSelected(menuItem.screen)
            }
        }
    }
}

@Composable
private fun DrawerButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    subIcon: ImageVector? = null,
    label: String,
    isSelected: Boolean,
    action: () -> Unit,
) {
    val colors = MaterialTheme.colors
    val textIconColor = if (isSelected) {
        colors.primary
    } else {
        colors.onSurface.copy(alpha = 0.6f)
    }
    val backgroundColor = if (isSelected) {
        colors.primary.copy(alpha = 0.12f)
    } else {
        Color.Transparent
    }

    val surfaceModifier = modifier
        .padding(start = 8.dp, top = 8.dp, end = 8.dp)
        .fillMaxWidth()
    Surface(
        modifier = surfaceModifier,
        color = backgroundColor,
        shape = MaterialTheme.shapes.small,
    ) {
        TextButton(
            onClick = action,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                NavigationIcon(
                    icon = icon,
                    isSelected = isSelected,
                    contentDescription = null, // decorative
                    tintColor = textIconColor,
                )
                Spacer(Modifier.width(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.body2,
                        color = textIconColor,
                        modifier = Modifier.weight(1F),
                    )

                    subIcon?.let {
                        Image(
                            modifier = modifier
                                .width(24.dp)
                                .height(24.dp),
                            alignment = Alignment.CenterEnd,
                            imageVector = it,
                            contentDescription = null,
                            contentScale = ContentScale.Inside,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NavigationIcon(
    icon: ImageVector,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tintColor: Color? = null,
) {
    val imageAlpha = if (isSelected) {
        1f
    } else {
        0.6f
    }

    val iconTintColor = tintColor ?: if (isSelected) {
        MaterialTheme.colors.primary
    } else {
        MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
    }

    Image(
        modifier = modifier
            .width(24.dp)
            .height(24.dp),
        imageVector = icon,
        contentDescription = contentDescription,
        contentScale = ContentScale.Inside,
        colorFilter = ColorFilter.tint(iconTintColor),
        alpha = imageAlpha,
    )
}

private sealed class MenuItem(
    val screen: Screen,
    @StringRes val label: Int,
) {
    class ResourceIcon(
        screen: Screen,
        @StringRes label: Int,
        @DrawableRes val icon: Int,
        @DrawableRes val iconSecondary: Int? = null,
    ) : MenuItem(screen, label)

    class VectorIcon(
        screen: Screen,
        @StringRes label: Int,
        val icon: ImageVector,
    ) : MenuItem(screen, label)
}

private val MenuItems = listOf(
    MenuItem.VectorIcon(
        screen = Screen.Home,
        label = R.string.menu_home,
        icon = Icons.Filled.Home,
    ),
    MenuItem.VectorIcon(
        screen = Screen.Theme,
        label = R.string.menu_theme,
        icon = Icons.Filled.Category,
    ),
    MenuItem.ResourceIcon(
        screen = Screen.ProductHunt,
        label = R.string.menu_product_hunt,
        icon = core.playground.ui.R.drawable.ic_producthunt_24,
    ),
    MenuItem.ResourceIcon(
        screen = Screen.DeviantArt,
        label = R.string.menu_deviantArt,
        icon = R.drawable.ic_deviant_art,
        iconSecondary = R.drawable.ic_baseline_open_in_new_24,
    ),
)

@Preview
@Composable
private fun PreviewDrawer() {
    PlaygroundTheme {
        DrawerButton(
            icon = ImageVector.vectorResource(id = R.drawable.ic_deviant_art),
            subIcon = ImageVector.vectorResource(id = R.drawable.ic_baseline_open_in_new_24),
            label = stringResource(id = R.string.menu_deviantArt),
            isSelected = false,
            action = {},
        )
    }
}
