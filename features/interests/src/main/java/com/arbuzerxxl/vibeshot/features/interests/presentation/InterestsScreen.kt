package com.arbuzerxxl.vibeshot.features.interests.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.arbuzerxxl.vibeshot.core.ui.widgets.LoadingIndicator
import com.arbuzerxxl.vibeshot.core.ui.widgets.PhotoCard
import com.arbuzerxxl.vibeshot.domain.models.InterestsPhotoResource
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun InterestsRoute(
    modifier: Modifier = Modifier,
    viewmodel: InterestsViewModel = koinViewModel(),
    onPhotoClicked: (String) -> Unit,
) {
    val uiState = viewmodel.pagingDataFlow.collectAsLazyPagingItems()

    InterestsScreen(
        modifier = modifier,
        uiState = uiState,
        onPhotoClicked = onPhotoClicked
    )
}

@Composable
internal fun InterestsScreen(
    modifier: Modifier = Modifier,
    uiState: LazyPagingItems<InterestsPhotoResource>,
    onPhotoClicked: (String) -> Unit,
) {

    val scrollState = rememberLazyStaggeredGridState()
    var isScrolling by remember { mutableStateOf(false) }

    LaunchedEffect(scrollState.isScrollInProgress) {
        isScrolling = scrollState.isScrollInProgress
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {

        if (uiState.loadState.refresh == LoadState.Loading) {
            LoadingIndicator()
        }

        LazyVerticalStaggeredGrid(
            modifier = Modifier,
            state = scrollState,
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp),
        ) {
            items(
                count = uiState.itemCount,
                key = uiState.itemKey { it.sizes.url },
                contentType = uiState.itemContentType { "Interests photo" }
            ) { index ->
                val photo = uiState[index]
                photo?.let {
                    PhotoCard(
                        modifier = modifier
                            .clickable(
                                onClick = { onPhotoClicked(photo.sizes.url) }
                            ),
                        urlLowQuality = photo.sizes.lowQualityUrl,
                        urlHighQuality = photo.sizes.url,
                        height = photo.sizes.height,
                        width = photo.sizes.width,
                        isScrolling = isScrolling
                    )
                }
            }
        }
        if (uiState.loadState.append == LoadState.Loading) {
            LoadingIndicator(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
                    .size(20.dp)
            )
        }
    }
}
