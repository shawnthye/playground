package app.playground.ui.debug

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.playground.ui.debug.components.DebugIcon
import app.playground.ui.debug.components.DebugIcon.VectorIcon
import app.playground.ui.debug.components.EnumDropdown
import app.playground.ui.debug.components.StatsTable
import app.playground.ui.debug.components.SubHeader
import app.playground.ui.debug.data.DebugEnvironment
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.systemBarsPadding
import core.playground.ui.theme.contentHorizontalPadding
import feature.playground.demos.Demos
import feature.playground.deviant.ui.DeviantArt
import okhttp3.logging.HttpLoggingInterceptor

@Composable
internal fun ColumnScope.DebugSettings(
    buildVersionName: String,
    buildVersionCode: Int,
    buildType: String,
    model: DebugViewModel = viewModel(),
    coilModel: DebugCoilViewModel = viewModel(),
    showFeatureFlags: () -> Unit,
) {

    val buildStats = mapOf(
        "Name" to buildVersionName,
        "Code" to "$buildVersionCode",
        "Type" to buildType,
    )

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .align(Alignment.End)
            .verticalScroll(state = rememberScrollState())
            .systemBarsPadding()
            .navigationBarsPadding(),
    ) {
        DebugSettingsHeader(applicationName = model.applicationName)

        EnumDropdown(
            modifier = Modifier
                .contentHorizontalPadding()
                .padding(bottom = 16.dp),
            label = "Environment - Not implemented",
            options = DebugEnvironment.values().asList(),
            selected = DebugEnvironment.PRODUCTION,
        ) {
        }

        DebugNetwork(model = model)
        DebugCoil(model = coilModel)
        BuildStats(stats = buildStats)
        DeviceStats(stats = model.deviceStats)
        ExtraAction(
            label = "Feature Flags",
            onPress = {
                showFeatureFlags()
            },
            icon = VectorIcon(Icons.Filled.Flag),
        )
        ExtraAction(
            label = "Demos",
            onPress = {
                Demos.start(context)
            },
            icon = VectorIcon(Icons.Filled.Dashboard),
        )
        DeviantArtAction()
        ExtraAction(
            label = "Reset",
            onPress = {
                model.resetDebugSettings()
                coilModel.submitAction(CoilAction.Reset)
            },
            icon = VectorIcon(Icons.Filled.RestartAlt),
        )
    }
}

@Composable
private fun ColumnScope.DebugNetwork(model: DebugViewModel) {

    val level by model.httpLoggingLevel.collectAsState()

    SubHeader(title = "Network", icon = VectorIcon(Icons.Outlined.Cloud)) { padding ->
        EnumDropdown(
            modifier = Modifier.padding(padding),
            label = "Logging",
            options = HttpLoggingInterceptor.Level.values().asList(),
            selected = level,
        ) {
            model.updateHttpLoggingLevel(it)
        }
    }
}

@Composable
fun ColumnScope.BuildStats(stats: Map<String, String>) {
    SubHeader(title = "Build", icon = VectorIcon(Icons.Filled.Construction)) { padding ->
        StatsTable(modifier = Modifier.padding(padding), stats = stats)
    }
}

@Composable
fun ColumnScope.DeviceStats(stats: Map<String, String>) {
    SubHeader(title = "Device", icon = VectorIcon(Icons.Filled.Devices)) { padding ->
        StatsTable(modifier = Modifier.padding(padding), stats = stats)
    }
}

@Composable
fun ColumnScope.DeviantArtAction(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    TextButton(
        modifier = modifier
            .align(Alignment.End)
            .padding(horizontal = 16.dp),
        onClick = { DeviantArt.start(context) },
    ) {
        Text(text = "Deviant Art")
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_open_in_new_24),
            contentDescription = null,
            tint = colorResource(id = core.playground.ui.R.color.brandDeviantArt),
        )
    }
}

@Composable
private fun ColumnScope.ExtraAction(label: String, onPress: () -> Unit, icon: DebugIcon?) {
    TextButton(
        modifier = Modifier
            .align(Alignment.End)
            .padding(horizontal = 16.dp),
        onClick = onPress,
    ) {
        Text(text = label)
        if (null != icon) {
            val vector = when (icon) {
                is DebugIcon.ResourceIcon -> ImageVector.vectorResource(id = icon.resId)
                is VectorIcon -> icon.vector
            }

            Icon(
                imageVector = vector,
                contentDescription = null,
            )
        }
    }
}
