package com.arbuzerxxl.vibeshot.features.searching.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.arbuzerxxl.vibeshot.core.design.theme.itemHeight24
import com.arbuzerxxl.vibeshot.core.design.theme.padding16
import com.arbuzerxxl.vibeshot.core.design.theme.padding20
import com.arbuzerxxl.vibeshot.core.ui.widgets.LoadingIndicator
import com.arbuzerxxl.vibeshot.core.ui.widgets.PhotoGrid
import com.arbuzerxxl.vibeshot.core.ui.widgets.SearchField
import com.arbuzerxxl.vibeshot.domain.models.interest.SearchResource
import com.arbuzerxxl.vibeshot.features.searching.navigation.SearchDestination
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun SearchRoute(
    modifier: Modifier = Modifier,
    viewmodel: SearchViewModel = koinViewModel(),
    onPhotoClicked: (Int, String) -> Unit,
) {
    val items = viewmodel.uiState.collectAsLazyPagingItems()

    SearchScreen(
        modifier = modifier,
        items = items,
        onPhotoClicked = onPhotoClicked,
        onSearchQueryChange = viewmodel::onSearch
    )

}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        LoadingIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = padding20)
                .size(itemHeight24)
        )
    }
}

@Composable
internal fun SearchScreen(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<SearchResource>,
    onPhotoClicked: (Int, String) -> Unit,
    onSearchQueryChange: (String) -> Unit
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            SearchField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding16),
                onValueChange = onSearchQueryChange
            )

            PhotoGrid(
                items = items,
                onPhotoClicked = onPhotoClicked,
                parentDestinationName = SearchDestination::class.java.name,
            )
        }
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