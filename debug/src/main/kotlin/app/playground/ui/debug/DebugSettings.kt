package app.playground.ui.debug

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.playground.ui.debug.components.DebugIcon
import app.playground.ui.debug.components.DebugIcon.VectorIcon
import app.playground.ui.debug.components.EnumDropdown
import app.playground.ui.debug.components.ExtraAction
import app.playground.ui.debug.components.StatsTable
import app.playground.ui.debug.components.SubHeader
import app.playground.ui.debug.data.DebugEnvironment
import app.playground.ui.debug.data.HttpLogging
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.systemBarsPadding
import core.playground.ui.theme.contentHorizontalPadding
import feature.playground.demos.Demos
import feature.playground.deviant.ui.DeviantArt

@Composable
internal fun DebugSettings(
    model: DebugViewModel = viewModel(),
    coilModel: DebugCoilViewModel = viewModel(),
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .verticalScroll(state = rememberScrollState())
            .systemBarsPadding()
            .navigationBarsPadding(),
    ) {
        DebugSettingsHeader(applicationName = model.applicationName)

        DebugEnvironment(model = model)

        DebugNetwork(model = model)

        DebugCoil(model = coilModel)

        BuildStats(model = model)

        DeviceStats(model = model)

        ExtraAction(
            label = "Demos",
            icon = VectorIcon(Icons.Rounded.Category),
            onPress = { Demos.start(context) },
        )

        ExtraAction(
            label = "Deviant Art",
            icon = DebugIcon.ResourceIcon(
                R.drawable.ic_baseline_open_in_new_24,
                tint = colorResource(
                    id = core.playground.ui.R.color.brandDeviantArt,
                ),
            ),
            onPress = { DeviantArt.start(context) },
        )

        ExtraAction(
            label = "Reset",
            icon = VectorIcon(Icons.Filled.RestartAlt),
            onPress = {
                model.resetDebugSettings()
                coilModel.submitAction(CoilUiAction.Reset)
            },
        )
    }
}

@Composable
private fun ColumnScope.DebugEnvironment(model: DebugViewModel) {

    val environment by model.environment.collectAsState()

    EnumDropdown(
        modifier = Modifier
            .contentHorizontalPadding()
            .padding(bottom = 0.dp),
        label = "Environment - Not implemented",
        options = DebugEnvironment.values().asList(),
        selected = environment,
    ) {
        model.updateEnvironment(it)
    }

    OutlinedButton(
        modifier = Modifier
            .contentHorizontalPadding()
            .fillMaxWidth()
            .align(Alignment.Start)
            .padding(bottom = 16.dp),
        onClick = { model.showFeatureFlags() },
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "Feature Flags",
        )
        Icon(imageVector = Icons.Rounded.Flag, contentDescription = "Show Feature Flags")
    }
}

@Composable
private fun ColumnScope.DebugNetwork(model: DebugViewModel) {

    val level by model.httpLoggingLevel.collectAsState()

    SubHeader(title = "Network", icon = VectorIcon(Icons.Outlined.Cloud)) { padding ->
        EnumDropdown(
            modifier = Modifier.padding(padding),
            label = "Logging",
            options = HttpLogging.values().asList(),
            selected = level,
        ) {
            model.updateHttpLoggingLevel(it)
        }
    }
}

@Composable
private fun ColumnScope.BuildStats(model: DebugViewModel) {
    val stats by model.buildStats.collectAsState()
    SubHeader(title = "Build", icon = VectorIcon(Icons.Filled.Construction)) { padding ->
        StatsTable(modifier = Modifier.padding(padding), stats = stats)
    }
}

@Composable
private fun ColumnScope.DeviceStats(model: DebugViewModel) {
    val stats by model.deviceStats.collectAsState()
    SubHeader(title = "Device", icon = VectorIcon(Icons.Filled.Devices)) { padding ->
        StatsTable(modifier = Modifier.padding(padding), stats = stats)
    }
}
