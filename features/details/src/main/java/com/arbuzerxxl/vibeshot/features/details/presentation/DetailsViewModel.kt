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


internal sealed interface DetailsUiState {

    data object Loading : DetailsUiState

    @Immutable
    data class Success(val photos: Flow<PagingData<DetailsPhoto>>) : DetailsUiState
}

internal class DetailsViewModel(
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
                            DetailsPhoto.preview().copy(id = photo.id, url = photo.sizes.highQualityUrl)
                        }
                    }
                    .cachedIn(viewModelScope)
            }

            is ParentDestination.Search -> emptyFlow()
        }
    }
}

internal data class DetailsPhoto(
    val id: String,
    val url: String,
    val owner: String,
    val title: String,
    val description: String,
    val dateUpload: String,
    val dateTaken: String,
    val views: Int,
    val comments: Int,
    val camera: Camera?,
    val tags: List<String>,
    val license: String,
) {
    companion object {
        fun preview(): DetailsPhoto = DetailsPhoto(
            id = "1234",
            url = "www.ww.com",
            owner = "Gerd Michael Kozik",
            title = "L.A.K.E.",
            description = "All photos copyright 2015-2025 by Gerd Michael Kozik No further use of my photos in any form such as websites, print, commercial or private use. Do not use my photos without my permission !\n\nThank you all for your visits, faves and comments!\n",
            dateUpload = "May 1, 2025",
            dateTaken = "May 3, 2025",
            views = 1345,
            comments = 64,
            // camera details - getExif method
            camera = Camera.preview(),
            // more
            tags = listOf<String>(
                "lion1",
                "twins1",
                "Sister1",
                "lion2",
                "twins2",
                "Sister2",
                "lion3",
                "twins3",
                "Sister3",
            ),
            // additional info
            license = "All rights reserved",
        )
    }
}

internal data class Camera(
    val model: String,
    val lens: String,
    val aperture: String,
    val focalLength: String,
    val iso: String,
    val flash: String,
    val shutterSpeed: String,
    val whiteBalance: String,
) {
    companion object {
        fun preview(): Camera = Camera(
            model = "Sony ILCE-7RM5",
            lens = "50mm f/1.2",
            aperture = "f/16.0",
            focalLength = "50.0 mm",
            iso = "",
            flash = "Off, Did not fire",
            shutterSpeed = "1/320",
            whiteBalance = "Auto",
        )
    }
}