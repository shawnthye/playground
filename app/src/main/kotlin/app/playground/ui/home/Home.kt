package app.playground.ui.home

import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.statusBarsPadding
import core.playground.ui.rememberFlowWithLifecycle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
) : ViewModel() {

    val count = MutableStateFlow(0)

    fun onClick() {
        viewModelScope.launch {
            count.emit(count.value + 1)
        }
    }
}

@Preview
@Composable
fun Home(viewModel: HomeViewModel = viewModel()) {

    val count by rememberFlowWithLifecycle(viewModel.count).collectAsState(0)

    Scaffold {
        Surface(modifier = Modifier.statusBarsPadding()) {
            TextButton(onClick = { viewModel.onClick() }) {
                Text(text = "home $count")
            }
        }
    }
}
