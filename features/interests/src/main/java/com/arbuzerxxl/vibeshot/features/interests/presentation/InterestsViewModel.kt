package com.arbuzerxxl.vibeshot.features.interests.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arbuzerxxl.vibeshot.domain.repository.InterestsRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface InterestsUiState {

    data object Loading : InterestsUiState

    data class Success(val url: ImmutableList<String>) : InterestsUiState
}

class InterestsViewModel(
    private val interestsRepository: InterestsRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<InterestsUiState>(InterestsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            interestsRepository.getPhotos().collect { items ->
                _uiState.update {
                    InterestsUiState.Success(
                        items.map { it.url }.toImmutableList()
                    )
                }
            }
        }
    }

}