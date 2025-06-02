package com.arbuzerxxl.vibeshot.features.interests.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.arbuzerxxl.vibeshot.core.design.theme.itemHeight24
import com.arbuzerxxl.vibeshot.core.design.theme.padding16
import com.arbuzerxxl.vibeshot.core.design.theme.padding20
import com.arbuzerxxl.vibeshot.core.ui.utils.ConnectionBanner
import com.arbuzerxxl.vibeshot.core.ui.widgets.LoadingError
import com.arbuzerxxl.vibeshot.core.ui.widgets.LoadingIndicator
import com.arbuzerxxl.vibeshot.core.ui.widgets.PhotoGrid
import com.arbuzerxxl.vibeshot.domain.models.interest.InterestsResource
import com.arbuzerxxl.vibeshot.features.interests.navigation.InterestsDestination
import com.arbuzerxxl.vibeshot.ui.R
import org.koin.androidx.compose.koinViewModel


@Composable
internal fun InterestsRoute(
    modifier: Modifier = Modifier,
    viewmodel: InterestsViewModel = koinViewModel(),
    onPhotoClicked: (Int, String) -> Unit,
) {

    val items = viewmodel.uiState.collectAsLazyPagingItems()
    val isConnected by viewmodel.isConnected.collectAsState()

    InterestsScreen(
        modifier = modifier,
        items = items,
        onPhotoClicked = onPhotoClicked,
        onRefreshClicked = viewmodel::onRefreshClick,
        isConnected = isConnected
    )
}

@Composable
internal fun InterestsScreen(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<InterestsResource>,
    onPhotoClicked: (Int, String) -> Unit,
    onRefreshClicked: () -> Unit,
    isConnected: Boolean
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {

        PhotoGrid(
            items = items,
            onPhotoClicked = onPhotoClicked,
            parentDestinationName = InterestsDestination::class.java.name,
        )
        if (items.loadState.hasError) {
            LoadingError(textResId = R.string.interests_load_error, onRefreshClick = onRefreshClicked)
        }
        if (!items.loadState.isIdle) {
            LoadingIndicator(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = padding20)
                    .size(itemHeight24)
            )
        }
        ConnectionBanner(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = padding16), isConnected = isConnected)
    }
}
