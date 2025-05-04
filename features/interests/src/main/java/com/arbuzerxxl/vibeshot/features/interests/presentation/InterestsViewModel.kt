package com.arbuzerxxl.vibeshot.features.interests.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arbuzerxxl.vibeshot.domain.models.interest.InterestsResource
import com.arbuzerxxl.vibeshot.domain.repository.InterestsRepository
import kotlinx.coroutines.flow.Flow


internal class InterestsViewModel(
    private val interestsRepository: InterestsRepository
) : ViewModel() {

    val uiState: Flow<PagingData<InterestsResource>> = interestsRepository
        .data
        .cachedIn(viewModelScope)

}