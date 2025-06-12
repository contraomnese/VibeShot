package com.arbuzerxxl.vibeshot.features.searching.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arbuzerxxl.vibeshot.core.ui.utils.ErrorMonitor
import com.arbuzerxxl.vibeshot.domain.models.interest.SearchResource
import com.arbuzerxxl.vibeshot.domain.repository.SearchRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

private const val UNKNOWN_ERROR = "Unknown error"

@Immutable
internal data class SearchUiState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val data: Flow<PagingData<SearchResource>> = flowOf(),
)

internal class SearchViewModel(
    private val searchRepository: SearchRepository,
    private val errorMonitor: ErrorMonitor,
    private val searchTag: String? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState(isLoading = true))

    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val searchData = searchRepository
                    .data
                    .cachedIn(viewModelScope)

                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        data = searchData
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                errorMonitor.tryEmit(e.message ?: UNKNOWN_ERROR)
            }
        }
        searchTag?.let {
            onSearchByTag(it)
        }
    }

    fun onSearchByText(text: String) {

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                searchRepository.searchByText(text)
                delay(2000)
                _uiState.update { it.copy(isLoading = false) }
            }
            catch (e: CancellationException) {
                throw e
            }
            catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                errorMonitor.tryEmit(e.message ?: UNKNOWN_ERROR)
            }
        }

    }

    private fun onSearchByTag(tag: String) {

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, searchQuery = tag) }
                searchRepository.searchByTag(tag)
                delay(2000)
                _uiState.update { it.copy(isLoading = false) }
            }
            catch (e: CancellationException) {
                throw e
            }
            catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                errorMonitor.tryEmit(e.message ?: UNKNOWN_ERROR)
            }
        }

    }

    fun clearSearchData() {
        viewModelScope.launch {
            try {
                searchRepository.clearData()
            }
            catch (e: CancellationException) {
                throw e
            }catch (e: Exception) {
                errorMonitor.tryEmit(e.message ?: UNKNOWN_ERROR)
            }
        }
    }

    fun onSearchQueryChanged(newSearchQuery: String) {
        _uiState.update { it.copy(searchQuery = newSearchQuery) }
    }
}