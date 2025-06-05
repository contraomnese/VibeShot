package com.arbuzerxxl.vibeshot.features.interests.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arbuzerxxl.vibeshot.domain.models.interest.InterestsResource
import com.arbuzerxxl.vibeshot.domain.repository.InterestsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Immutable
internal data class InterestsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: Flow<PagingData<InterestsResource>> = flowOf(),
)

internal class InterestsViewModel(
    private val interestsRepository: InterestsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(InterestsUiState())

    val uiState: StateFlow<InterestsUiState> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = InterestsUiState(isLoading = true)
    )

    init {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val interestData = interestsRepository
                    .data
                    .cachedIn(viewModelScope)

                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        data = interestData
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun onRefreshClick() {
        interestsRepository.load()
    }
}