package com.arbuzerxxl.vibeshot.features.interests.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arbuzerxxl.vibeshot.core.ui.utils.ErrorMonitor
import com.arbuzerxxl.vibeshot.domain.models.interest.InterestsResource
import com.arbuzerxxl.vibeshot.domain.repository.InterestsRepository
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
internal data class InterestsUiState(
    val isLoading: Boolean = false,
    val data: Flow<PagingData<InterestsResource>> = flowOf(),
)

internal class InterestsViewModel(
    private val interestsRepository: InterestsRepository,
    private val errorMonitor: ErrorMonitor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(InterestsUiState(isLoading = true))

    val uiState: StateFlow<InterestsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val interestData = interestsRepository
                    .data
                    .cachedIn(viewModelScope)

                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        data = interestData
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                errorMonitor.tryEmit(e.message ?: UNKNOWN_ERROR)
            }
        }
    }

    fun onRefreshClick() {
        viewModelScope.launch {
            try {
                interestsRepository.loadData()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                errorMonitor.tryEmit(e.message ?: UNKNOWN_ERROR)
            }
        }

    }
}