package com.arbuzerxxl.vibeshot.features.searching.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.arbuzerxxl.vibeshot.core.design.theme.itemHeight24
import com.arbuzerxxl.vibeshot.core.design.theme.padding20
import com.arbuzerxxl.vibeshot.core.ui.widgets.LoadingIndicator
import com.arbuzerxxl.vibeshot.core.ui.widgets.PhotoGrid
import com.arbuzerxxl.vibeshot.core.ui.widgets.SearchTextField
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
        onSearchTriggered = viewmodel::onSearch
    )

}

@Composable
internal fun SearchScreen(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<SearchResource>,
    onPhotoClicked: (Int, String) -> Unit,
    onSearchTriggered: (String) -> Unit
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        var searchQuery by remember { mutableStateOf("") }

        Column {
            SearchTextField(
                searchQuery = searchQuery,
                onSearchQueryChanged = { value: String -> searchQuery = value },
                onSearchTriggered = onSearchTriggered
            )

            PhotoGrid(
                items = items,
                onPhotoClicked = onPhotoClicked,
                parentDestinationName = SearchDestination::class.java.name,
            )
        }
        if (!items.loadState.isIdle && searchQuery.isNotEmpty()) {
            LoadingIndicator(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = padding20)
                    .size(itemHeight24)
            )
        }
    }
}