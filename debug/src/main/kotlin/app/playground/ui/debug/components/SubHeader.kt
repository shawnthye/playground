package app.playground.ui.debug.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.playground.ui.debug.R
import core.playground.ui.theme.PlaygroundTheme

internal sealed class DebugIcon {
    data class ResourceIcon(@DrawableRes val resId: Int) : DebugIcon()
    data class VectorIcon(val vector: ImageVector) : DebugIcon()
}

@Composable
internal fun ColumnScope.SubHeader(
    title: String,
    icon: DebugIcon,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    content: @Composable ColumnScope.(innerPadding: PaddingValues) -> Unit,
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(end = 4.dp),
            text = title.uppercase(), style = MaterialTheme.typography.h6,
        )

        val iconModifier = Modifier
            .fillMaxHeight()
            .padding(vertical = 4.dp)
            .aspectRatio(1f)
        when (icon) {
            is DebugIcon.ResourceIcon -> Image(
                modifier = iconModifier,
                painter = painterResource(id = R.drawable.debug_coil_kt),
                contentDescription = null,
            )
            is DebugIcon.VectorIcon -> Icon(
                modifier = iconModifier,
                imageVector = icon.vector,
                contentDescription = null,
            )
        }
    }
    Divider(
        modifier = Modifier
            .padding(contentPadding)
            .padding(top = 2.dp, bottom = 4.dp),
        color = MaterialTheme.colors.onSurface,
        thickness = 2.dp,
    )
    content(contentPadding)
    Spacer(modifier = Modifier.height(12.dp))
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewSubHeader() {
    PlaygroundTheme {
        Column(modifier = Modifier.fillMaxWidth()) {
            SubHeader(
                title = "Preview",
                icon = DebugIcon.VectorIcon(Icons.Outlined.Construction),
            ) {
            }

            SubHeader(
                title = "Preview",
                icon = DebugIcon.ResourceIcon(R.drawable.debug_coil_kt),
            ) {
            }
        }
    }
}
