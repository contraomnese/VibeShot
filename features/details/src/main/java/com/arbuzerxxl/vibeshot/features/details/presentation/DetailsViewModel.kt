package com.arbuzerxxl.vibeshot.features.details.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.arbuzerxxl.vibeshot.domain.repository.InterestsRepository
import com.arbuzerxxl.vibeshot.features.details.navigation.ParentDestination
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update


sealed interface DetailsUiState {

    data object Loading : DetailsUiState

    @Immutable
    data class Success(val photos: Flow<PagingData<DetailsPhoto>>) : DetailsUiState
}

class DetailsViewModel(
    private val interestsRepository: InterestsRepository,
    private val parentDestination: ParentDestination,
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        val photos = provideFlowByDestination(parentDestination)
        _uiState.update {
            DetailsUiState.Success(photos)
        }
    }

    private fun provideFlowByDestination(parentDestination: ParentDestination): Flow<PagingData<DetailsPhoto>> {
        return when (parentDestination) {
            is ParentDestination.Interests -> {
                interestsRepository.data
                    .map { data ->
                        data.map { photo ->
                            DetailsPhoto(
                                url = photo.sizes.highQualityUrl, id = photo.id, title = photo.title
                            )
                        }
                    }
                    .cachedIn(viewModelScope)
            }
            is ParentDestination.Search -> emptyFlow()
        }
    }
}

data class DetailsPhoto(val url: String, val id: String, val title: String)
