package feature.playground.demos.theme

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import core.playground.ui.theme.PlaygroundTheme
import kotlinx.coroutines.launch

private sealed class Page(val title: String) {
    object Color : Page("Color")
    object Shape : Page("Shape")
    object Typography : Page("Typography")
    object Components : Page("Components")
}

private val TABS = listOf("Color", "Shape", "Typography", "Components")

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun ThemeDemo() {
    Column {
        val coroutineScope = rememberCoroutineScope()
        val pagerState = rememberPagerState()

        val pages = remember {
            listOf(Page.Color, Page.Shape, Page.Typography, Page.Components)
        }

        ScrollableTabRow(
            backgroundColor = MaterialTheme.colors.surface,
            modifier = Modifier.statusBarsPadding(),
            edgePadding = 0.dp,
            selectedTabIndex = pagerState.currentPage,
            indicator = { position ->
                TabRowDefaults.Indicator(Modifier.pagerTabIndicatorOffset(pagerState, position))
            },
        ) {
            pages.forEachIndexed { index, page ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(text = page.title) },
                )
            }
        }
        HorizontalPager(
            count = TABS.size,
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) { page ->
            Box(modifier = Modifier.fillMaxSize()) {
                when (pages[page]) {
                    Page.Color -> Color()
                    Page.Shape -> Shape()
                    Page.Typography -> Type()
                    Page.Components -> Components()
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewLight() {
    PlaygroundTheme {
        ThemeDemo()
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewNight() {
    PreviewLight()
}
