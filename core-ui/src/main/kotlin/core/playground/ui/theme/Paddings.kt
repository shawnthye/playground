package core.playground.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val Dp.Companion.contentPadding get() = 16.dp

fun Modifier.contentPaddingHeight() = then(height(Dp.contentPadding))

fun Modifier.contentHorizontalPadding() = then(
    padding(PaddingValues(horizontal = Dp.contentPadding)),
)

fun Modifier.contentVerticalPadding() = then(
    padding(PaddingValues(vertical = Dp.contentPadding)),
)

fun Modifier.contentPadding() = this
    .then(contentHorizontalPadding())
    .then(contentVerticalPadding())
