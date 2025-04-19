package com.arbuzerxxl.vibeshot.features.interests.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arbuzerxxl.vibeshot.domain.models.InterestsResources
import com.arbuzerxxl.vibeshot.domain.repository.InterestsRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal sealed interface InterestsUiState {

    data object Loading : InterestsUiState

    data class Success(val photos: ImmutableList<Photo>) : InterestsUiState
}

internal class InterestsViewModel(
    private val interestsRepository: InterestsRepository,
) : ViewModel() {

    private var currentPage = 1
    private var totalPages = 0

    private val _uiState = MutableStateFlow<InterestsUiState>(InterestsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState.asStateFlow()

    private suspend fun <T> withLoading(block: suspend () -> T): T {
        _loadingState.value = true
        return try {
            block()
        } finally {
            _loadingState.value = false
        }
    }

    init {
        viewModelScope.launch {
            interestsRepository.getPhotos(currentPage).collect { items ->
                initState(items)
                totalPages = items.pages
            }
        }
    }

    private fun initState(items: InterestsResources) {
        _uiState.update {
            InterestsUiState.Success(
                items.resources.map {
                    Photo(
                        lowQualityUrl = it.lowQualityImageUrl.url,
                        highQualityUrl = it.highQualityImageUrl.url,
                        height = it.height,
                        width = it.width
                    )
                }.toImmutableList()
            )
        }
    }

    fun loadMore() {
        if (currentPage < totalPages) {
            viewModelScope.launch {
                withLoading {
                    interestsRepository.getPhotos(currentPage++).collect { items ->
                        updateCurrentState(items)
                    }
                }
            }
        }
    }

    private fun updateCurrentState(items: InterestsResources) {
        _uiState.update {
            (it as InterestsUiState.Success).copy(
                photos = it.photos.plus(items.resources.map {
                    Photo(
                        lowQualityUrl = it.lowQualityImageUrl.url,
                        highQualityUrl = it.highQualityImageUrl.url,
                        height = it.height,
                        width = it.width
                    )
                }).toImmutableList()
            )
        }
    }
}

data class Photo(val lowQualityUrl: String, val highQualityUrl: String, val height: Int, val width: Int)