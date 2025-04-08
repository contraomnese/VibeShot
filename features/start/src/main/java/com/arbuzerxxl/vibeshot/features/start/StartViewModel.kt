package com.arbuzerxxl.vibeshot.features.start

import android.R.attr.theme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arbuzerxxl.vibeshot.domain.models.auth.AuthState
import com.arbuzerxxl.vibeshot.domain.models.ui.DarkThemeConfig
import com.arbuzerxxl.vibeshot.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface StartUiState {

    data object Loading : StartUiState

    data class Success(val username: String, val darkMode: Boolean? = null) : StartUiState
}

class StartViewModel(
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<StartUiState>(StartUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userDataRepository.userData.collect { data ->
                _uiState.update { when (val authState = data.authState) {
                    is AuthState.Authenticated -> StartUiState.Success(username = authState.user.username, darkMode = (data.theme == DarkThemeConfig.DARK))
                    is AuthState.Guest -> StartUiState.Success(username = authState.user.username, darkMode = (data.theme == DarkThemeConfig.DARK))
                    AuthState.Unauthenticated -> StartUiState.Loading
                } }

            }
        }
    }

    fun setTheme(dark: Boolean) {
        viewModelScope.launch {
            userDataRepository.setTheme(dark)
        }
    }
}