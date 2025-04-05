package com.arbuzerxxl.vibeshot.features.auth.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arbuzerxxl.vibeshot.domain.models.auth.AuthState
import com.arbuzerxxl.vibeshot.domain.models.auth.tokens.RequestToken
import com.arbuzerxxl.vibeshot.domain.repository.AuthRepository
import com.arbuzerxxl.vibeshot.domain.repository.TokenRepository
import com.arbuzerxxl.vibeshot.domain.usecases.auth.ObserveAuthStateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface AuthUiState {

    data object Loading : AuthUiState

    @Immutable
    data class Authorize(val authUrl: String): AuthUiState

    data object Error: AuthUiState

    data object GuestSuccess : AuthUiState

    data object UserSuccess : AuthUiState
}

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val tokenRepository: TokenRepository,
    private val observeAuthStateUseCase: ObserveAuthStateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeAuthStateUseCase().collect { state ->
                when (state) {
                    is AuthState.Authenticated -> _uiState.update { AuthUiState.UserSuccess }
                    is AuthState.Guest -> _uiState.update { AuthUiState.GuestSuccess }
                    AuthState.Unauthenticated -> _uiState.update { AuthUiState.Error }
                }
            }
        }
    }

    fun onSignIn() {
        viewModelScope.launch {
            val authUrl = authRepository.getAuthUrl()
            _uiState.update {
                AuthUiState.Authorize(authUrl = authUrl)
            }
        }
    }

    fun onSignInAsGuest() {
        viewModelScope.launch {
            authRepository.signInAsGuest()
            _uiState.update {
                AuthUiState.GuestSuccess
            }
        }
    }
}