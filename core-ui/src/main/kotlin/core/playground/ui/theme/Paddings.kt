package core.playground.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val padding = 16.dp

fun Modifier.contentPaddingHeight() = then(height(padding))

fun Modifier.contentHorizontalPadding() = then(padding(PaddingValues(horizontal = padding)))

fun Modifier.contentVerticalPadding() = then(padding(PaddingValues(vertical = padding)))

fun Modifier.contentPadding() = this
    .then(contentHorizontalPadding())
    .then(contentVerticalPadding())
