package com.arbuzerxxl.vibeshot.features.interests.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arbuzerxxl.vibeshot.core.ui.widgets.LoadingIndicator
import com.arbuzerxxl.vibeshot.core.ui.widgets.PhotoCard
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun InterestsRoute(
    modifier: Modifier = Modifier,
    viewmodel: InterestsViewModel = koinViewModel()
) {

    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    InterestsScreen(
        modifier = modifier,
        uiState = uiState,
    )
}

@Composable
internal fun InterestsScreen(
    modifier: Modifier = Modifier,
    uiState: InterestsUiState,
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
        when (uiState) {
            InterestsUiState.Loading,
                -> LoadingIndicator()

            is InterestsUiState.Success,
                -> {
                LazyVerticalStaggeredGrid(
                    modifier = Modifier,
                    state = scrollState,
                    columns = StaggeredGridCells.Fixed(2),
                    verticalItemSpacing = 8.dp,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(8.dp),
                ) {
                    items(uiState.photos, key = { it.highQualityUrl }) { photo ->
                        PhotoCard(
                            urlLowQuality = photo.lowQualityUrl,
                            urlHighQuality = photo.highQualityUrl,
                            height = photo.height,
                            width = photo.width,
                            isScrolling = isScrolling
                        )
                    }
                }
            }
        }
    }
}
