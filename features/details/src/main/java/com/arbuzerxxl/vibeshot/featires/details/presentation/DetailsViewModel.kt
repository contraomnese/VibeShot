package com.arbuzerxxl.vibeshot.featires.details.presentation

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.map
import com.arbuzerxxl.vibeshot.domain.usecases.photos.GetInterestsPhotosUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

internal sealed interface DetailsUiState {

    data object Loading : DetailsUiState

    data class Success(val photo: DetailsPhoto) : DetailsUiState
}

internal class DetailsViewModel(
    private val getInterestsPhotosUseCase: GetInterestsPhotosUseCase,
    private val id: DetailsPhotoId
) : ViewModel() {


    val uiState: Flow<PagingData<DetailsPhoto>> = getInterestsPhotosUseCase.execute()
        .map { data ->
            data.map { photo ->
                DetailsPhoto(
                    url = photo.sizes.highQualityUrl, id = photo.id, title = photo.title
                )
            }
        }

    private val _errorState = MutableStateFlow<Throwable?>(null)
    val errorState: StateFlow<Throwable?> = _errorState

}

data class DetailsPhoto(val url: String, val id: String, val title: String)

@JvmInline
value class DetailsPhotoId(val id: String)