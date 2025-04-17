package com.arbuzerxxl.vibeshot.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arbuzerxxl.vibeshot.domain.models.auth.AuthState
import com.arbuzerxxl.vibeshot.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal sealed interface ProfileUiState {

    data object Loading : ProfileUiState

    data class Success(val username: String) : ProfileUiState
}

internal class ProfileViewModel(
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userDataRepository.userData.collect { data ->
                _uiState.update { when (val authState = data.authState) {
                    is AuthState.Authenticated -> ProfileUiState.Success(username = authState.user.username)
                    is AuthState.Guest -> ProfileUiState.Success(username = authState.user.username)
                    AuthState.Unauthenticated -> ProfileUiState.Loading
                } }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userDataRepository.clearUserData()
        }
    }
}