package com.arbuzerxxl.vibeshot.features.details.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoResource
import com.arbuzerxxl.vibeshot.domain.repository.InterestsRepository
import com.arbuzerxxl.vibeshot.domain.repository.PhotosRepository
import com.arbuzerxxl.vibeshot.domain.repository.SearchRepository
import com.arbuzerxxl.vibeshot.features.details.navigation.ParentDestination
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


internal sealed interface DetailsUiState {

    data object Loading : DetailsUiState

    @Immutable
    data class Success(val photos: Flow<PagingData<DetailsPhoto>>, val currentPhoto: PhotoResource?) : DetailsUiState
}

internal class DetailsViewModel(
    private val interestsRepository: InterestsRepository,
    private val photosRepository: PhotosRepository,
    private val searchRepository: SearchRepository,
    private val parentDestination: ParentDestination,
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        val photos = provideFlowByDestination(parentDestination)
        _uiState.update {
            DetailsUiState.Success(photos = photos, currentPhoto = null)
        }
    }

    private fun provideFlowByDestination(parentDestination: ParentDestination): Flow<PagingData<DetailsPhoto>> {
        return when (parentDestination) {
            is ParentDestination.Interests -> {
                interestsRepository.data
                    .map { data ->
                        data.map { photo ->
                            DetailsPhoto(id = photo.id, url = photo.sizes.highQualityUrl)
                        }
                    }
                    .cachedIn(viewModelScope)
            }

            is ParentDestination.Search -> {
                searchRepository.data
                    .map { data ->
                        data.map { photo ->
                            DetailsPhoto(id = photo.id, url = photo.sizes.highQualityUrl)
                        }
                    }
                    .cachedIn(viewModelScope)
            }
        }
    }

    fun setPhoto(photo: DetailsPhoto) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                (currentState as DetailsUiState.Success).copy(
                    currentPhoto = null
                )
            }
            val photo = photosRepository.getPhoto(photoId = photo.id, photoUrl = photo.url)
            _uiState.update { currentState ->
                (currentState as DetailsUiState.Success).copy(
                    currentPhoto = photo
                )
            }
        }
    }
}

internal data class DetailsPhoto(
    val id: String,
    val url: String,
)