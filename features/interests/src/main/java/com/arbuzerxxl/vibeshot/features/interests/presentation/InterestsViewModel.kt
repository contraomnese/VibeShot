package com.arbuzerxxl.vibeshot.features.interests.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arbuzerxxl.vibeshot.domain.models.InterestsPhotoResource
import com.arbuzerxxl.vibeshot.domain.repository.InterestsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


internal class InterestsViewModel(
    private val interestsRepository: InterestsRepository
) : ViewModel() {

    val uiState: Flow<PagingData<InterestsPhotoResource>> = interestsRepository
        .data
        .cachedIn(viewModelScope)

    private val _errorState = MutableStateFlow<Throwable?>(null)
    val errorState: StateFlow<Throwable?> = _errorState

}