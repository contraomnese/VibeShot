package com.arbuzerxxl.vibeshot.features.auth_impl.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arbuzerxxl.vibeshot.domain.usecases.auth.GetAccessTokenUseCase
import com.arbuzerxxl.vibeshot.domain.usecases.auth.GetAuthorizeUrlUseCase
import com.arbuzerxxl.vibeshot.domain.usecases.auth.GetRequestTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface AuthUiState {

    data object Loading : AuthUiState

    @Immutable
    data class RequestTokenSuccess(val requestToken: String) : AuthUiState

    @Immutable
    data class VerifierSuccess(
        val requestToken: String,
        val verifier: String,
    ) : AuthUiState

    @Immutable
    data class AccessTokenSuccess(val accessToken: String) : AuthUiState

    @Immutable
    data class Success(
        val fullName: String,
        val id: String,
        val username: String,
    ) : AuthUiState
}

class AuthViewModel(
    private val getRequestTokenUseCase: GetRequestTokenUseCase,
    private val getAuthorizeUrlUseCase: GetAuthorizeUrlUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private lateinit var secretToken: String

    fun onStart() {
        viewModelScope.launch {
            val requestToken = getRequestTokenUseCase.execute()
            secretToken = requestToken.requestTokenSecret
            _uiState.update {
                AuthUiState.RequestTokenSuccess(
                    requestToken = requestToken.requestToken
                )
            }
        }
    }

    fun onVerifierChange(token: String, verifier: String) {
        viewModelScope.launch {
            _uiState.update {
                AuthUiState.VerifierSuccess(
                    verifier = verifier,
                    requestToken = token
                )
            }
        }
    }

    fun getAuthorizeUrl(requestToken: String): String =
        getAuthorizeUrlUseCase.execute(requestToken)

    fun getAccessToken(
        requestToken: String,
        verifier: String,
    ) {
        viewModelScope.launch {
            val accessToken = getAccessTokenUseCase.execute(
                requestToken = requestToken,
                verifier = verifier,
                secretToken = secretToken
            )
            _uiState.update {
                AuthUiState.AccessTokenSuccess(
                    accessToken = accessToken.accessToken
                )
            }
        }
    }
}