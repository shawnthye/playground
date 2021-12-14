package core.playground.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun Modifier.contentHorizontalPadding() = then(padding(PaddingValues(horizontal = 16.dp)))

fun Modifier.contentVerticalPadding() = then(padding(PaddingValues(vertical = 16.dp)))

fun Modifier.contentPadding() = this
    .then(contentHorizontalPadding())
    .then(contentVerticalPadding())
