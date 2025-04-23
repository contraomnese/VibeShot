package com.arbuzerxxl.vibeshot.featires.details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal sealed interface DetailsUiState {

    data object Loading : DetailsUiState

    data class Success(val photo: DetailsPhoto) : DetailsUiState
}

internal class DetailsViewModel(private val photo: DetailsPhoto) : ViewModel() {


    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            initState(photo)
        }
    }

    private fun initState(photo: DetailsPhoto) {
        _uiState.update {
            DetailsUiState.Success(photo)
        }
    }
}

data class DetailsPhoto(val url: String)