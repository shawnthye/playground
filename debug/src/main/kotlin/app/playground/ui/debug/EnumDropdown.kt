package app.playground.ui.debug

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import timber.log.Timber

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
    var expended by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(options.indexOf(default)) }

    /**
     * TODO: custom view for Dropdown, maybe make it like Chip pattern?
     * the OutlinedTextField has too many problem
     * - we can't totally disable long press, with tries, still buggy,
     *   you can try long press, you will still see something weird
     * - on scrolling, the dropdown show up :facepalm
     * - the enabled can't totally disable pressed, but with simple hack, which is currently doing
     */
    ExposedDropdownMenuBox(
        modifier = modifier.padding(bottom = 8.dp),
        expanded = expended,
        onExpandedChange = { expended = enabled && !expended },
    ) {
        OutlinedTextField(
            modifier = Modifier
                // we set this to disable focus event
                .clickable(enabled = false, onClickLabel = null, role = null, onClick = { })
                .focusProperties { canFocus = false }
                .fillMaxWidth(),
            readOnly = true,
            enabled = enabled,
            value = TextFieldValue(options[selected].readableName, selection = TextRange.Zero),
            label = { Text(text = label) },
            maxLines = 1,
            singleLine = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expended) },
            onValueChange = {
                Timber.i(it.toString())
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                trailingIconColor = MaterialTheme.colors.onSurface,
            ),
        )

        ExposedDropdownMenu(
            modifier = Modifier,
            expanded = expended,
            onDismissRequest = { expended = false },
        ) {
            options.mapIndexed { index, cachePolicy ->
                DropdownMenuItem(
                    modifier = Modifier.exposedDropdownSize(false),
                    onClick = {
                        val changed = selected != index
                        selected = index
                        expended = false
                        if (changed) {
                            onValueChange(options[selected])
                        }
                    },
                ) {
                    Text(text = cachePolicy.readableName)
                }
            }
        }
    }
}
