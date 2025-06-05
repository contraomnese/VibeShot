package com.arbuzerxxl.vibeshot.features.details.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.arbuzerxxl.vibeshot.core.ui.utils.NetworkMonitor
import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoResource
import com.arbuzerxxl.vibeshot.domain.repository.InterestsRepository
import com.arbuzerxxl.vibeshot.domain.repository.PhotosRepository
import com.arbuzerxxl.vibeshot.domain.repository.SearchRepository
import com.arbuzerxxl.vibeshot.features.details.navigation.ParentDestination
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@Immutable
internal data class DetailsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val photos: Flow<PagingData<DetailsPhoto>> = flowOf(),
    val currentPhoto: PhotoResource? = null,
    val isNetworkConnected: Boolean = false,
)

internal data class DetailsPhoto(
    val id: String,
    val url: String,
)

internal class DetailsViewModel(
    private val interestsRepository: InterestsRepository,
    private val photosRepository: PhotosRepository,
    private val searchRepository: SearchRepository,
    private val parentDestination: ParentDestination,
    private val networkMonitor: NetworkMonitor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState())

    val uiState: StateFlow<DetailsUiState> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DetailsUiState(isLoading = true)
    )

    init {
        val photos = provideFlowByDestination(parentDestination)
        updatingNetworkStatus()
        _uiState.update { currentState ->
            DetailsUiState(photos = photos, currentPhoto = null)
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

    fun setPhoto(photoInfo: DetailsPhoto) {
        if (uiState.value.isNetworkConnected) {
            viewModelScope.launch {
                _uiState.update { currentState ->
                    currentState.copy(
                        currentPhoto = null,
                        isLoading = true
                    )
                }
                try {
                    val photo = photosRepository.getPhoto(photoId = photoInfo.id, photoUrl = photoInfo.url)
                    _uiState.update { currentState ->
                        currentState.copy(
                            currentPhoto = photo,
                            isLoading = false
                        )
                    }
                } catch (e: Exception) {
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
            }
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