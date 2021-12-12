package app.playground.ui.debug

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import okhttp3.logging.HttpLoggingInterceptor

private typealias OnValueChange<T> = (T) -> Unit

private val Enum<*>.readableName get() = name.replace("_", " ")

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun <E : Enum<*>> EnumDropdown(
    modifier: Modifier = Modifier,
    label: String,
    options: List<E>,
    default: E,
    enabled: Boolean = true,
    onValueChange: OnValueChange<E> = {},
) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(options.indexOf(default)) }
    var width by remember { mutableStateOf(0) }

    val density = LocalDensity.current

    Column {
        Box(
            modifier = modifier
                .padding(bottom = 4.dp)
                .onGloballyPositioned {
                    width = it.size.width
                },
        ) {

            OutlinedButton(
                onClick = {
                    expanded = true
                },
                enabled = enabled,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = label, style = MaterialTheme.typography.overline)
                    Text(
                        text = options[selected].readableName,
                        style = MaterialTheme.typography.caption,
                    )
                }
                Icon(
                    Icons.Filled.ArrowDropDown,
                    "Trailing icon for exposed dropdown menu",
                    Modifier.rotate(
                        if (expanded)
                            180f
                        else
                            360f,
                    ),
                )
            }

            DropdownMenu(
                expanded = expanded, onDismissRequest = { expanded = false },
                modifier = Modifier.width(with(density) { width.toDp() }),
            ) {
                options.mapIndexed { index, option ->
                    DropdownMenuItem(
                        onClick = {
                            val changed = selected != index
                            selected = index
                            expanded = false
                            if (changed) {
                                onValueChange(options[selected])
                            }
                        },
                    ) {
                        Text(
                            text = option.readableName,
                            style = MaterialTheme.typography.caption,
                            fontWeight = if (index != selected) {
                                FontWeight.Light
                            } else {
                                FontWeight.SemiBold
                            },
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewDropDown() {
    Column {
        EnumDropdown(
            label = "Logging",
            options = HttpLoggingInterceptor.Level.values().asList(),
            default = HttpLoggingInterceptor.Level.BODY,
        )
    }
}
