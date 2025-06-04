package com.arbuzerxxl.vibeshot.core.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotThemePreview
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews
import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoItem
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : PhotoItem> PhotoGrid(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<T>,
    onPhotoClickNavigate: (Int, String) -> Unit,
    parentDestinationName: String,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {

    val scrollState = rememberLazyStaggeredGridState()
    var isScrolling by remember { mutableStateOf(false) }
    val refreshState = rememberPullToRefreshState()

    LaunchedEffect(scrollState.isScrollInProgress) {
        isScrolling = scrollState.isScrollInProgress
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        PullToRefreshBox(
            modifier = modifier,
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            state = refreshState,
            indicator = {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = isRefreshing,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    state = refreshState
                )
            },
        ) {
            LazyVerticalStaggeredGrid(
                modifier = Modifier,
                state = scrollState,
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp),
            ) {
                items(
                    count = items.itemCount,
                    contentType = items.itemContentType { "Interests photo" }
                ) { index ->
                    val photo = items[index]
                    photo?.let {
                        PhotoCard(
                            modifier = modifier
                                .clickable(
                                    onClick = { onPhotoClickNavigate(index, parentDestinationName) }
                                ),
                            urlLowQuality = photo.sizes.lowQualityUrl,
                            urlHighQuality = photo.sizes.highQualityUrl,
                            height = photo.sizes.height,
                            width = photo.sizes.width,
                            isScrolling = isScrolling
                        )
                    } ?: PhotoCardPlaceHolder()
                }
            }
        }
    }
}

@DevicePreviews
@Composable
fun PhotoGridPreview() {
    VibeShotThemePreview {
        PhotoGrid(
            items = flowOf<PagingData<PhotoItem>>().collectAsLazyPagingItems(),
            onPhotoClickNavigate = { i: Int, string: String -> },
            parentDestinationName = "Tricia Saunders", isRefreshing = false, onRefresh = {}
        )
    }
}