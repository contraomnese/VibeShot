package com.arbuzerxxl.vibeshot.featires.details.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.arbuzerxxl.vibeshot.domain.usecases.photos.GetInterestsPhotosUseCaseSuspend
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal sealed interface DetailsUiState {

    data object Loading : DetailsUiState

    @Immutable
    data class Success(val photos: Flow<PagingData<DetailsPhoto>>) : DetailsUiState
}

internal class DetailsViewModel(
    private val getInterestsPhotosUseCase: GetInterestsPhotosUseCaseSuspend,
) : ViewModel() {


    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun initialize(index: Int) {
        viewModelScope.launch {
            val photos = getInterestsPhotosUseCase.execute(index = index)
                .map { data ->
                    data.map { photo ->
                        DetailsPhoto(
                            url = photo.sizes.highQualityUrl, id = photo.id, title = photo.title
                        )
                    }
                }
            _uiState.update {
                DetailsUiState.Success(photos)
            }
        }
    }

    private val _errorState = MutableStateFlow<Throwable?>(null)
    val errorState: StateFlow<Throwable?> = _errorState

}

data class DetailsPhoto(val url: String, val id: String, val title: String)

@JvmInline
value class DetailsPhotoIndex(val index: Int)