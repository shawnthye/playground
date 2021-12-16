package feature.playground.demos

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetDefaults
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsHeight

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun BottomSheetMenu(
    modifier: Modifier = Modifier,
    state: ModalBottomSheetState,
    selected: DemoScreen = DemoScreen.Start,
    onMenuSelected: (DemoScreen) -> Unit,
    content: @Composable () -> Unit,
) {
    
    ModalBottomSheetLayout(
        modifier = modifier,
        content = content,
        scrimColor = MaterialTheme.colors.surface.copy(alpha = 0.64f),
        sheetState = state,
        sheetBackgroundColor = Color.Transparent,
        sheetElevation = 0.dp,
        sheetShape = RoundedCornerShape(0.dp),
        sheetContent = {
            Spacer(modifier = Modifier.statusBarsHeight())
            Surface(
                color = MaterialTheme.colors.surface,
                elevation = ModalBottomSheetDefaults.Elevation,
                shape = MaterialTheme.shapes.large.copy(
                    bottomStart = CornerSize(0.dp),
                    bottomEnd = CornerSize(0.dp),
                ),
            ) {
                LazyColumn {
                    item { Spacer(modifier = Modifier.height(12.dp)) }
                    items(DemoScreen.values() + DemoScreen.values() + DemoScreen.values() + DemoScreen.values() + DemoScreen.values()) { screen ->
                        NavigationItem(
                            icon = screen.icon,
                            label = screen.title,
                            isSelected = screen == selected,
                            action = {
                                onMenuSelected(screen)
                            },
                        )
                    }
                    item { Spacer(modifier = Modifier.height(12.dp)) }
                }
            }
        },
    )
}

@Composable
private fun NavigationItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
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
                Text(
                    text = label,
                    style = MaterialTheme.typography.body2,
                    color = textIconColor,
                    modifier = Modifier.weight(1F),
                )
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
