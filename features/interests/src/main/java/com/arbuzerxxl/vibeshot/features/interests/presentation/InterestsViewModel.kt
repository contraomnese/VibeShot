package com.arbuzerxxl.vibeshot.features.interests.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arbuzerxxl.vibeshot.domain.models.InterestsPhotoResource
import com.arbuzerxxl.vibeshot.domain.models.InterestsPhotoResources
import com.arbuzerxxl.vibeshot.domain.usecases.photos.GetInterestsPhotosUseCase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal sealed interface InterestsUiState {

    data object Loading : InterestsUiState

    data class Success(val photos: ImmutableList<InterestsPhotoResource>) : InterestsUiState
}

internal class InterestsViewModel(
    private val getInterestsPhotosUseCase: GetInterestsPhotosUseCase
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
            val photos = getInterestsPhotosUseCase.execute(currentPage)
            initState(photos)
            totalPages = photos.pages
        }
    }

    private fun initState(photos: InterestsPhotoResources) {
        _uiState.update {
            InterestsUiState.Success(
                photos.resources.toImmutableList()
            )
        }
    }

    fun loadMore() {
        if (currentPage < totalPages) {
            viewModelScope.launch {
                withLoading {
                    val photos = getInterestsPhotosUseCase.execute(++currentPage)
                    updateCurrentState(photos)
                }
            }
        }
    }

    private fun updateCurrentState(items: InterestsPhotoResources) {
        _uiState.update {
            (it as InterestsUiState.Success).copy(
                photos = it.photos.plus(items.resources).toImmutableList()
            )
        }
    }
}