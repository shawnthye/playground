package app.playground.ui.debug

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.text.format.Formatter
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.playground.ui.debug.DebugIcon.ResourceIcon
import app.playground.ui.debug.DebugIcon.VectorIcon
import coil.Coil
import coil.imageLoader
import coil.request.CachePolicy
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.systemBarsPadding
import core.playground.ui.theme.PlaygroundTheme
import java.util.Locale

@Composable
internal fun ColumnScope.DebugDrawerMenu(
    model: DebugViewModel = viewModel(),
) {

    Column(
        modifier = Modifier
            .weight(1f)
            .verticalScroll(state = rememberScrollState())
            .systemBarsPadding()
            .navigationBarsPadding(),
    ) {

        Header()

        SubHeader(title = "Network", icon = VectorIcon(Icons.Outlined.Cloud)) {

        }

        DebugCoil(
            stats = model.coilUiStats,
            refresh = {
                model.coilRefreshStats()
            },
            trimMemory = {
                model.coilTrimMemory()
            },
        )

        SubHeader(title = "Build", icon = VectorIcon(Icons.Filled.Construction)) {
        }

        SubHeader(title = "Device", icon = VectorIcon(Icons.Filled.Devices)) { padding ->
            Row(modifier = Modifier.padding(padding)) {
                Column(modifier = Modifier.padding(end = 16.dp)) {
                    DEVICE_STATS.keys.map {
                        Text(text = it)
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    DEVICE_STATS.values.map {
                        Text(text = it)
                    }
                }
            }
        }
    }
}

val DEVICE_STATS = mapOf(
    "Make" to "Unknown",
    "Model" to "Android SDK build for x86",
    "Resolution" to "1794x1080",
    "Density" to "420pi (420)",
    "Release" to "11",
    "API" to "30",
)

private val COIL_CACHE_POLICIES = CachePolicy.values().asList()

@Composable
private fun ColumnScope.DebugCoil(stats: CoilUiStats, refresh: () -> Unit, trimMemory: () -> Unit) {

    val context = LocalContext.current

    val current = Formatter.formatFileSize(LocalContext.current, stats.sizeBytes.toLong())
    val total = Formatter.formatFileSize(LocalContext.current, stats.maxSizeBytes.toLong())
    val percentage = "%.2f".format(
        Locale.ENGLISH,
        (1f * stats.sizeBytes / stats.maxSizeBytes) * 100,
    )

    SubHeader(
        title = "Coil",
        icon = ResourceIcon(R.drawable.debug_coil_kt),
    ) { padding ->
        val defaults = context.imageLoader.defaults
        EnumDropdown(
            modifier = Modifier.padding(horizontal = 16.dp),
            label = "Memory cache",
            options = COIL_CACHE_POLICIES,
            default = defaults.memoryCachePolicy,
        ) {
            context.imageLoader.shutdown()
            Coil.setImageLoader(context.imageLoader.newBuilder().memoryCachePolicy(it).build())
        }
        EnumDropdown(
            modifier = Modifier.padding(horizontal = 16.dp),
            label = "Disk cache",
            options = COIL_CACHE_POLICIES,
            default = defaults.diskCachePolicy,
        ) {
            context.imageLoader.shutdown()
            Coil.setImageLoader(context.imageLoader.newBuilder().diskCachePolicy(it).build())
        }

        EnumDropdown(
            modifier = Modifier.padding(horizontal = 16.dp),
            label = "Network cache",
            options = COIL_CACHE_POLICIES,
            default = defaults.networkCachePolicy,
            enabled = false,
        ) {
            context.imageLoader.shutdown()
            Coil.setImageLoader(context.imageLoader.newBuilder().networkCachePolicy(it).build())
        }

        StatRowWithAction(
            modifier = Modifier.padding(padding),
            title = "Memory Usage",
            text = "$current/$total ($percentage%)",
            actionLeft = {
                refresh()
            },
            actionRight = {
                trimMemory()
            },
        )

    }
}

@Composable
private fun ColumnScope.StatRowWithAction(
    modifier: Modifier,
    title: String,
    text: String,
    actionLeft: () -> Unit = {},
    actionRight: () -> Unit = {},
) {
    Surface(
        modifier = modifier
            .padding(top = 4.dp)
            .align(Alignment.Start)
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.25f),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 0.dp),
        ) {
            Column(
                Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface,
                )
                Text(
                    text = text,
                    modifier = Modifier.alpha(0.6f),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface,
                )
            }
            IconButton(
                modifier = Modifier.padding(2.dp),
                onClick = { actionLeft() },
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Clear Memory",
                )
            }

            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 8.dp)
                    .width(1.dp),
            )

            IconButton(
                modifier = Modifier.padding(2.dp),
                onClick = { actionRight() },
            ) {
                Icon(
                    imageVector = Icons.Filled.CleaningServices,
                    contentDescription = "Clear Memory",
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.Header() {
    Row(
        modifier = Modifier
            .align(Alignment.Start)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "Debug Settings", style = MaterialTheme.typography.h6)
        Icon(imageVector = Icons.Default.BugReport, contentDescription = "")
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewHeader() {
    PlaygroundTheme {
        Column(modifier = Modifier.fillMaxWidth()) {
            Header()
        }
    }
}

@Composable
private fun ColumnScope.SubHeader(
    title: String,
    icon: DebugIcon,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    content: @Composable ColumnScope.(innerPadding: PaddingValues) -> Unit,
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(contentPadding)
            .padding(top = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(end = 4.dp),
            text = title.uppercase(), style = MaterialTheme.typography.h5,
        )

        val iconModifier = Modifier
            .fillMaxHeight()
            .padding(vertical = 4.dp)
            .aspectRatio(1f)
        when (icon) {
            is ResourceIcon -> Image(
                modifier = iconModifier,
                painter = painterResource(id = R.drawable.debug_coil_kt),
                contentDescription = null,
            )
            is VectorIcon -> Icon(
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
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewSubHeader() {
    PlaygroundTheme {
        Column(modifier = Modifier.fillMaxWidth()) {
            SubHeader(
                title = "Preview",
                icon = VectorIcon(Icons.Outlined.Construction),
            ) { padding ->
                StatRowWithAction(
                    modifier = Modifier.padding(padding),
                    title = "Memory",
                    text = "value",
                )
            }

            SubHeader(
                title = "Preview",
                icon = ResourceIcon(R.drawable.debug_coil_kt),
            ) { padding ->
                StatRowWithAction(
                    modifier = Modifier.padding(padding),
                    title = "Memory",
                    text = "value",
                )
            }
        }
    }
}

private sealed class DebugIcon {
    data class ResourceIcon(@DrawableRes val resId: Int) : DebugIcon()
    data class VectorIcon(val vector: ImageVector) : DebugIcon()
}
