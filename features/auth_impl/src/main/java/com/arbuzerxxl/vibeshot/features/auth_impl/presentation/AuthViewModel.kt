package com.arbuzerxxl.vibeshot.features.auth_impl.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.arbuzerxxl.vibeshot.domain.models.AccessToken
import com.arbuzerxxl.vibeshot.domain.models.RequestTokenDomain
import com.arbuzerxxl.vibeshot.domain.models.Verifier
import com.arbuzerxxl.vibeshot.domain.usecases.auth.GetRequestTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface AuthUiState {
    data object Loading : AuthUiState

    @Immutable
    data class Success(
        val requestToken: RequestTokenDomain,
//        val verifier: Verifier,
//        val accessToken: AccessToken
    ) : AuthUiState
}

class AuthViewModel(
    private val getRequestTokenUseCase: GetRequestTokenUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun onStart() {
        viewModelScope.launch {
            val requestToken = getRequestTokenUseCase.execute()
            _uiState.update {
                AuthUiState.Success(
                    requestToken = requestToken,
                )
            }
        }
    }

    class Factory(
        private val getRequestTokenUseCase: GetRequestTokenUseCase
    ) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AuthViewModel(
                getRequestTokenUseCase = getRequestTokenUseCase
            ) as T
    }

}