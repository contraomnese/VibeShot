package com.arbuzerxxl.vibeshot.features.interests.presentation

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.arbuzerxxl.vibeshot.domain.models.InterestsPhotoResource
import com.arbuzerxxl.vibeshot.domain.usecases.photos.GetInterestsPhotosUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


internal class InterestsViewModel(
    private val getInterestsPhotosUseCase: GetInterestsPhotosUseCase
) : ViewModel() {


    val uiState: Flow<PagingData<InterestsPhotoResource>> = getInterestsPhotosUseCase.execute()


    private val _errorState = MutableStateFlow<Throwable?>(null)
    val errorState: StateFlow<Throwable?> = _errorState

}