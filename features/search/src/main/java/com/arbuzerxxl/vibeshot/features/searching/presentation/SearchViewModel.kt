package com.arbuzerxxl.vibeshot.features.searching.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arbuzerxxl.vibeshot.domain.models.interest.SearchResource
import com.arbuzerxxl.vibeshot.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow


internal class SearchViewModel(
    private val searchRepository: SearchRepository,
) : ViewModel() {

    val uiState: Flow<PagingData<SearchResource>> = searchRepository
        .data
        .cachedIn(viewModelScope)

    fun onSearch(query: String) {
        searchRepository.search(query)
    }
}