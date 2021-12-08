package feature.playground.demos.error.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import core.playground.ui.alias.NavigateUp
import core.playground.ui.components.DrawerAppBar
import core.playground.ui.theme.PlaygroundTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull

private val NOOP: () -> Unit = { /* NOOP */ }

@Composable
fun ErrorDemo(
    navigateUp: NavigateUp,
) {
    ErrorDemo(navigateUp = navigateUp, viewModel = hiltViewModel())
}

@Composable
internal fun ErrorDemo(
    navigateUp: NavigateUp,
    viewModel: ErrorDemoViewModel,
) {

    var throwable by viewModel.throwable.rememberAsDialogState()

    if (throwable != null) {
        ErrorDialog(throwable = throwable) {
            throwable = null
        }
    }

    Scaffold(
        topBar = {
            DrawerAppBar(
                titleRes = core.playground.ui.R.string.menu_error_demo,
                navigationUp = navigateUp,
            )
        },
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

                Component {
                    Button(onClick = { viewModel.tryOkHttp() }) { Text(text = "OkHttp") }
                }

                Component {
                    Button(onClick = { viewModel.try204Response() }) {
                        Text(text = "Flow<Response<Unit>> for 204")
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorDialog(throwable: Throwable?, onDismiss: () -> Unit) {
    val cause = throwable ?: return

    val innerCause = cause.cause

    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text(text = stringResource(id = android.R.string.ok))
            }
        },
        text = {
            Column {
                ErrorDialogSection(
                    title = "Cause",
                    content = "${cause::class.qualifiedName}",
                )
                ErrorDialogSection(
                    title = "Message",
                    content = "${cause.message}",
                )
                ErrorDialogSection(
                    title = "Inner Cause",
                    content = "${innerCause?.javaClass?.canonicalName}",
                )
                ErrorDialogSection(
                    title = "Inner Message",
                    content = "${innerCause?.message}",
                )
            }
        },
    )
}

@Composable
private fun ErrorDialogSection(title: String, content: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.body1,
    )
    Text(
        text = content,
        style = MaterialTheme.typography.body2,
        modifier = Modifier.padding(bottom = 8.dp),
    )
}

/**
 * Not sure if this is the right thing to do, experimental :) *
 *
 * ViewModel.throwable replace with State<Throwable> instead?
 */
@Stable
@Composable
private fun Flow<Throwable?>.rememberAsDialogState(): MutableState<Throwable?> {

    val throwable = remember { mutableStateOf<Throwable?>(null) }

    LaunchedEffect(this) {
        mapNotNull {
            it
        }.collect {
            throwable.value = it
        }
    }

    return throwable
}

@Composable
private fun Component(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
    ) {
        content()
    }
}

@Preview
@Composable
private fun PreviewLight() {
    PlaygroundTheme {
        ErrorDemo(navigateUp = NOOP)
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewNight() {
    PreviewLight()
}
