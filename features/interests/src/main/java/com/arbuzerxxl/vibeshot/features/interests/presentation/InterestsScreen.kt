package com.arbuzerxxl.vibeshot.features.interests.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.arbuzerxxl.vibeshot.core.ui.widgets.PhotoGrid
import com.arbuzerxxl.vibeshot.domain.models.interest.InterestsResource
import com.arbuzerxxl.vibeshot.features.interests.navigation.InterestsDestination
import org.koin.androidx.compose.koinViewModel


@Composable
internal fun InterestsRoute(
    modifier: Modifier = Modifier,
    viewmodel: InterestsViewModel = koinViewModel(),
    onPhotoClickNavigate: (Int, String) -> Unit,
) {
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()
    val items = uiState.data.collectAsLazyPagingItems()

    InterestsScreen(
        modifier = modifier,
        items = items,
        onPhotoClickNavigate = onPhotoClickNavigate,
        onRefresh = viewmodel::onRefreshClick,
    )
}

@Composable
internal fun InterestsScreen(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<InterestsResource>,
    onPhotoClickNavigate: (Int, String) -> Unit,
    onRefresh: () -> Unit,
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        PhotoGrid(
            modifier = Modifier.fillMaxSize(),
            items = items,
            onPhotoClickNavigate = onPhotoClickNavigate,
            parentDestinationName = InterestsDestination::class.java.name,
            isRefreshing = items.loadState.refresh == LoadState.Loading,
            onRefresh = onRefresh
        )
    }
}
