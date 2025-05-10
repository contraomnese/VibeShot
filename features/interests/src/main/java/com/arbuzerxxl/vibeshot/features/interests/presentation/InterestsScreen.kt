package com.arbuzerxxl.vibeshot.features.interests.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.arbuzerxxl.vibeshot.core.design.theme.itemHeight24
import com.arbuzerxxl.vibeshot.core.design.theme.padding20
import com.arbuzerxxl.vibeshot.core.ui.widgets.LoadingIndicator
import com.arbuzerxxl.vibeshot.core.ui.widgets.PhotoGrid
import com.arbuzerxxl.vibeshot.domain.models.interest.InterestsResource
import com.arbuzerxxl.vibeshot.features.interests.navigation.InterestsDestination
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun InterestsRoute(
    modifier: Modifier = Modifier,
    viewmodel: InterestsViewModel = koinViewModel(),
    onPhotoClicked: (Int, String) -> Unit,
) {
    val items = viewmodel.uiState.collectAsLazyPagingItems()

    InterestsScreen(
        modifier = modifier,
        items = items,
        onPhotoClicked = onPhotoClicked
    )
}

@Composable
internal fun InterestsScreen(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<InterestsResource>,
    onPhotoClicked: (Int, String) -> Unit,
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
        if (!items.loadState.isIdle) {
            LoadingIndicator(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = padding20)
                    .size(itemHeight24)
            )
        }
    }
}
