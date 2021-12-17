package feature.playground.demos.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Snackbar
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ExperimentalAnimatedInsets
import core.playground.ui.theme.ThemeIcons
import core.playground.ui.theme.contentHorizontalPadding
import core.playground.ui.theme.contentPaddingHeight
import core.playground.ui.theme.onSurfaceEmphasisHighType
import core.playground.ui.theme.onSurfaceEmphasisMedium
import feature.playground.demos.R

private val NOOP: () -> Unit = { /* NOOP */ }

@OptIn(ExperimentalAnimatedInsets::class)
@Composable
internal fun Components() {
    ThemeContent {
        Spacer(modifier = Modifier.contentPaddingHeight())
        Buttons()
        FABs()
        Cards()
        TextFields()
        Switches()
        RadioButtons()
        Checkboxes()
        SnackBars()
    }
}

@Composable
private fun Buttons() {
    Component("Button", horizontalArrangement = Arrangement.SpaceBetween) {
        Button(onClick = NOOP) { ButtonText() }
        TextButton(onClick = NOOP) { ButtonText() }
        OutlinedButton(onClick = NOOP) { ButtonText() }
    }
}

@Composable
fun FABs() {
    Component("FAB", horizontalArrangement = Arrangement.SpaceBetween) {
        ExtendedFloatingActionButton(onClick = NOOP, text = ButtonText, icon = IconAdd)
        FloatingActionButton(onClick = NOOP, content = IconAdd)
        FloatingActionButton(onClick = NOOP, Modifier.size(40.dp), content = IconAdd)
    }
}

@Composable
private fun Cards() {
    Component("Card") {
        Card {
            Column {
                Image(
                    painter = painterResource(id = R.drawable.sample_image),
                    contentDescription = null,
                )
                Column(modifier = Modifier.contentHorizontalPadding()) {
                    Text(
                        text = "OVERLINE",
                        style = MaterialTheme.typography.overline,
                        color = MaterialTheme.colors.onSurfaceEmphasisMedium,
                        modifier = Modifier.padding(vertical = 16.dp),
                    )
                    Text(
                        text = "Headline 6",
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onSurfaceEmphasisHighType,
                    )
                    Text(
                        text = stringResource(id = R.string.lorem_ipsum),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurfaceEmphasisMedium,
                        modifier = Modifier.padding(vertical = 8.dp),
                    )
                }
                Row(modifier = Modifier.padding(12.dp)) {
                    TextButton(onClick = NOOP) {
                        Text(text = "Action 1")
                    }
                    TextButton(onClick = NOOP) {
                        Text(text = "Action 2")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TextFields() {
    Component(title = "Text Field") {
        var value by rememberSaveable { mutableStateOf("") }
        var outlineValue by rememberSaveable { mutableStateOf("") }
        Column {
            OutlinedTextField(
                value = outlineValue,
                onValueChange = { outlineValue = it },
                label = { Text("Label") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = value,
                onValueChange = { value = it },
                label = { Text("Label") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun Switches() {
    Component(title = "Switch") {
        var defaultOff by rememberSaveable { mutableStateOf(false) }
        Switch(checked = defaultOff, onCheckedChange = { defaultOff = it })
        Spacer(modifier = Modifier.width(8.dp))
        var defaultOn by rememberSaveable { mutableStateOf(true) }
        Switch(checked = defaultOn, onCheckedChange = { defaultOn = it })
    }
}

@Composable
private fun RadioButtons() {
    Component(title = "Radio Button") {
        val groups = remember { 0..1 }
        var selected by remember { mutableStateOf(groups.first) }
        groups.forEach {
            RadioButton(selected = selected == it, onClick = { selected = it })
        }
    }
}

@Composable
private fun Checkboxes() {
    Component(title = "Checkbox") {
        val ids = remember { 0..1 }
        val checkState = remember { mutableStateMapOf<Int, Boolean>() }
        ids.forEach { id ->
            Checkbox(
                checked = checkState.getOrDefault(id, false),
                onCheckedChange = { checkState[id] = it },
            )
        }
    }
}

@Composable
private fun SnackBars() {
    Component(title = "SnackBar") {
        Snackbar(
            action = {
                TextButton(
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colors.surface,
                    ),
                    onClick = { },
                    content = { Text(text = "Action") },
                )
            },
        ) {
            Text(text = "Marked as favorite")
        }
    }
}

@Composable
private fun Component(
    title: String,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable RowScope.() -> Unit,
) {

    Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.overline,
        modifier = Modifier
            .padding(bottom = 8.dp)
            .contentHorizontalPadding(),
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .contentHorizontalPadding(),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content()
    }
}

private val ButtonText: @Composable () -> Unit = { Text(text = "Button") }
private val IconAdd: @Composable () -> Unit = {
    Icon(imageVector = ThemeIcons.Add, contentDescription = null)
}
