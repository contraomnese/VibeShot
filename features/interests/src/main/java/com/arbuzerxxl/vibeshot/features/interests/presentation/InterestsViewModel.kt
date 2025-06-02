package com.arbuzerxxl.vibeshot.features.interests.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arbuzerxxl.vibeshot.core.ui.utils.NetworkMonitor
import com.arbuzerxxl.vibeshot.domain.models.interest.InterestsResource
import com.arbuzerxxl.vibeshot.domain.repository.InterestsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn


internal class InterestsViewModel(
    private val interestsRepository: InterestsRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    val uiState: Flow<PagingData<InterestsResource>> = interestsRepository
        .data
        .cachedIn(viewModelScope)

    val isConnected = networkMonitor.isConnected
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun onRefreshClick() = interestsRepository.load()

}