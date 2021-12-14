package core.playground.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.ui.Modifier

/**
 * A better version that ignore double click,
 * Once we have an empty double click, the Android system default utilize some delay
 *
 * So its depend on the system itself,
 * which is good for us without thinking what should be the delay
 *
 * User can also see the full ripple effect because of built-in double tap detector
 */
@OptIn(ExperimentalFoundationApi::class)
fun Modifier.tappable(onTap: () -> Unit) = combinedClickable(
    onDoubleClick = { /* NOOP */ },
    onClick = onTap,
)
