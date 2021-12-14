package app.playground.ui.debug

import android.text.format.Formatter
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.playground.ui.debug.components.DebugIcon
import app.playground.ui.debug.components.EnumDropdown
import app.playground.ui.debug.components.SubHeader
import app.playground.ui.debug.data.CoilLogLevel
import java.util.Locale

@Composable
internal fun ColumnScope.DebugSettingsCoil(
    model: DebugCoilViewModel = viewModel(),
) {
    val stats by model.coilUiStats.collectAsState()

    val current = Formatter.formatFileSize(LocalContext.current, stats.sizeBytes.toLong())
    val total = Formatter.formatFileSize(LocalContext.current, stats.maxSizeBytes.toLong())
    val percentage = "%.2f".format(
        Locale.ENGLISH,
        (1f * stats.sizeBytes / stats.maxSizeBytes) * 100,
    ).uppercase(Locale.ENGLISH)

    SubHeader(
        title = "Coil - image",
        icon = DebugIcon.ResourceIcon(R.drawable.debug_coil_kt),
    ) { padding ->
        StatRowWithAction(
            modifier = Modifier.padding(padding),
            title = "Memory Usage",
            text = "$current/$total ($percentage%)",
            actionLeft = {
                model.submitAction(CoilAction.Refresh)
            },
            actionRight = {
                model.submitAction(CoilAction.TrimMemory)
            },
        )

        Row(Modifier.padding(padding), horizontalArrangement = Arrangement.SpaceBetween) {
            EnumDropdown(
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .weight(1f),
                label = "Memory",
                options = stats.policies,
                selected = stats.memoryCachePolicy,
            ) {
                model.submitAction(CoilAction.UpdateMemoryPolicy(it))
            }

            Spacer(modifier = Modifier.width(6.dp))

            EnumDropdown(
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .weight(1f),
                label = "Disk",
                options = stats.policies,
                selected = stats.diskCachePolicy,
            ) {
                model.submitAction(CoilAction.UpdateDiskPolicy(it))
            }
        }

        EnumDropdown(
            modifier = Modifier.padding(padding),
            label = "Network",
            options = stats.policies,
            selected = stats.networkCachePolicy,
            enabled = true,
        ) {
            model.submitAction(CoilAction.UpdateNetworkPolicy(it))
        }

        EnumDropdown(
            modifier = Modifier.padding(padding),
            label = "Logging",
            options = CoilLogLevel.values().asList(),
            selected = stats.logLevel,
        ) {
            model.submitAction(CoilAction.UpdateLogLevel(it))
        }
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
            .padding(bottom = 4.dp)
            .align(Alignment.Start)
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.25f),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable {
                        actionLeft()
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.overline,
                        color = MaterialTheme.colors.onSurface,
                    )
                    Text(
                        text = text,
                        modifier = Modifier.alpha(0.6f),
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface,
                    )
                }

                Box(
                    modifier = Modifier.size(
                        LocalViewConfiguration.current.minimumTouchTargetSize,
                    ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Clear Memory",
                    )
                }
            }

            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 8.dp)
                    .width(1.dp),
            )

            Box(
                modifier = Modifier
                    .size(
                        LocalViewConfiguration.current.minimumTouchTargetSize,
                    )
                    .clickable {
                        actionRight()
                    },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.CleaningServices,
                    contentDescription = "Clear Memory",
                )
            }
        }
    }
}
