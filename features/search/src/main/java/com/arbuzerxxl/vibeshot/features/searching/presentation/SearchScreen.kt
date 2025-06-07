package com.arbuzerxxl.vibeshot.features.searching.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotThemePreview
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews
import com.arbuzerxxl.vibeshot.core.ui.widgets.PhotoGrid
import com.arbuzerxxl.vibeshot.core.ui.widgets.SearchTextField
import com.arbuzerxxl.vibeshot.domain.models.interest.SearchResource
import com.arbuzerxxl.vibeshot.features.searching.navigation.SearchDestination
import com.arbuzerxxl.vibeshot.ui.R
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun SearchRoute(
    modifier: Modifier = Modifier,
    viewmodel: SearchViewModel = koinViewModel(),
    onPhotoClicked: (Int, String) -> Unit,
) {
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()
    val items = uiState.data.collectAsLazyPagingItems()

    SearchScreen(
        modifier = modifier,
        items = items,
        uiState = uiState,
        onPhotoClicked = onPhotoClicked,
        onSearchTriggered = viewmodel::onSearch,
        onClearSearchData = viewmodel::clearSearchData
    )
}

@Composable
internal fun SearchScreen(
    modifier: Modifier = Modifier,
    uiState: SearchUiState,
    items: LazyPagingItems<SearchResource>,
    onPhotoClicked: (Int, String) -> Unit,
    onSearchTriggered: (String) -> Unit,
    onClearSearchData: () -> Unit,
) {

    var searchQuery by remember { mutableStateOf("") }
    val refreshSearch = remember { { onSearchTriggered(searchQuery) } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        SearchTextField(
            searchQuery = searchQuery,
            onSearchQueryChanged = { value: String -> searchQuery = value },
            onSearchTriggered = onSearchTriggered
        )
        if (items.itemSnapshotList.isNotEmpty()) {
            Text(
                modifier = Modifier
                    .clickable(onClick = onClearSearchData)
                    .clip(CircleShape),
                text = stringResource(R.string.clear),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        PhotoGrid(
            modifier = Modifier.fillMaxSize(),
            items = items,
            onPhotoClickNavigate = onPhotoClicked,
            parentDestinationName = SearchDestination::class.java.name,
            isRefreshing = uiState.isLoading,
            onRefresh = refreshSearch
        )
    }

}

@DevicePreviews
@Composable
private fun SearchScreenPreview() {
    VibeShotThemePreview {
        val state = SearchUiState(
            isLoading = true,
            data = flowOf(),
        )
        SearchScreen(
            uiState = state, items = state.data.collectAsLazyPagingItems(),
            onPhotoClicked = { i: Int, s: String -> },
            onSearchTriggered = {},
            onClearSearchData = {}
        )
    }
}