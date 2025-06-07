package com.arbuzerxxl.vibeshot.features.details.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.arbuzerxxl.vibeshot.core.ui.utils.ErrorMonitor
import com.arbuzerxxl.vibeshot.core.ui.utils.NetworkMonitor
import com.arbuzerxxl.vibeshot.core.ui.utils.NetworkStatus
import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoResource
import com.arbuzerxxl.vibeshot.domain.repository.InterestsRepository
import com.arbuzerxxl.vibeshot.domain.repository.PhotosRepository
import com.arbuzerxxl.vibeshot.domain.repository.SearchRepository
import com.arbuzerxxl.vibeshot.features.details.navigation.ParentDestination
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException


@Immutable
internal data class DetailsUiState(
    val isLoading: Boolean = false,
    val photos: Flow<PagingData<DetailsPhoto>> = flowOf(),
    val currentPhoto: PhotoResource? = null,
    val networkStatus: NetworkStatus = NetworkStatus.Disconnected,
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
    private val errorMonitor: ErrorMonitor,
) : ViewModel() {

    private val photos: Flow<PagingData<DetailsPhoto>> by lazy {
        provideFlowByDestination(parentDestination)
    }

    private val _uiState = MutableStateFlow(
        DetailsUiState(photos = photos, isLoading = true)
    )

    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    init {
        updatingNetworkStatus()
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

    fun onUpdateCurrentPhoto(photoInfo: DetailsPhoto) {

        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true
                )
            }
            try {
                val photo = photosRepository.getPhoto(
                    photoId = photoInfo.id,
                    photoUrl = photoInfo.url
                )
                _uiState.update { currentState ->
                    currentState.copy(
                        currentPhoto = photo,
                        isLoading = false
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                errorMonitor.tryEmit(e.message ?: "Unknown error")
            }
        }
    }

    private fun updatingNetworkStatus() {

        viewModelScope.launch {
            networkMonitor.networkStatus
                .catch { e ->
                    errorMonitor.tryEmit(e.message ?: "Unknown error")
                }
                .collect { status ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            networkStatus = status
                        )
                    }
                }
        }
    }
}