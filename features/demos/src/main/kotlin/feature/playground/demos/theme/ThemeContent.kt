package feature.playground.demos.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.playground.ui.theme.contentHorizontalPadding
import core.playground.ui.theme.contentPaddingHeight

@Composable
internal fun ThemeContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .contentHorizontalPadding()
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
    ) {
        Spacer(modifier = Modifier.contentPaddingHeight())
        content()
    }
}

@Composable
internal fun ColumnScope.ThemeLines(label: String, content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .align(Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.weight(1f),
        )
        content()
    }
}
