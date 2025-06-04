package com.arbuzerxxl.vibeshot.features.searching.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arbuzerxxl.vibeshot.core.ui.utils.NetworkMonitor
import com.arbuzerxxl.vibeshot.domain.models.interest.SearchResource
import com.arbuzerxxl.vibeshot.domain.repository.SearchRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@Immutable
internal data class SearchUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: Flow<PagingData<SearchResource>> = flowOf(),
    val isNetworkConnected: Boolean = false,
)

internal class SearchViewModel(
    private val searchRepository: SearchRepository,
    private val networkMonitor: NetworkMonitor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())

    val uiState: StateFlow<SearchUiState> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SearchUiState(isLoading = false)
    )

    init {
        val searchData = searchRepository
            .data
            .cachedIn(viewModelScope)

        updatingNetworkStatus()

        _uiState.update { currentState ->
            currentState.copy(
                isLoading = false,
                data = searchData
            )
        }
    }

    fun onSearch(query: String) {
        try {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }
                searchRepository.search(query)
                delay(2000)
                _uiState.update { it.copy(isLoading = false) }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, error = e.message) }
        }

    }

    fun clearSearchData() {
        viewModelScope.launch {
            searchRepository.clear()
        }
    }

    private fun updatingNetworkStatus() {

        val isConnected = networkMonitor.isConnected
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                true
            )

        viewModelScope.launch {
            isConnected.collect { value ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isNetworkConnected = value
                    )
                }
            }
        }
    }
}