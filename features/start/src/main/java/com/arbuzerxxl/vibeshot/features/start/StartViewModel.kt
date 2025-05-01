package com.arbuzerxxl.vibeshot.features.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arbuzerxxl.vibeshot.domain.models.ui.DarkThemeConfig
import com.arbuzerxxl.vibeshot.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal sealed interface StartUiState {

    data object Loading : StartUiState

    data class Success(val darkMode: Boolean? = null) : StartUiState
}

internal class StartViewModel(
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<StartUiState>(StartUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userDataRepository.userData.collect { data ->
                _uiState.update {
                    StartUiState.Success(darkMode = if (data.theme == DarkThemeConfig.FOLLOW_SYSTEM) null else (data.theme == DarkThemeConfig.DARK))
                }
            }
        }
    }

    fun setTheme(dark: Boolean) {
        viewModelScope.launch {
            userDataRepository.setTheme(dark)
        }
    }
}