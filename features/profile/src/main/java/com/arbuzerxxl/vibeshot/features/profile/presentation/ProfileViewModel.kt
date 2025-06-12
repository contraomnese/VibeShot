package com.arbuzerxxl.vibeshot.features.profile.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arbuzerxxl.vibeshot.core.ui.utils.ErrorMonitor
import com.arbuzerxxl.vibeshot.domain.models.auth.AuthState
import com.arbuzerxxl.vibeshot.domain.repository.PhotosRepository
import com.arbuzerxxl.vibeshot.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

@Immutable
internal data class ProfileUiState(
    val isLoading: Boolean = false,
    val username: String? = null,
)

internal class ProfileViewModel(
    private val userDataRepository: UserDataRepository,
    private val photosRepository: PhotosRepository,
    private val errorMonitor: ErrorMonitor
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {

            userDataRepository.userData
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false) }
                    errorMonitor.tryEmit(e.message ?: "Unknown error")
                }
                .collect { data ->
                    _uiState.update { currentState ->
                        when (val authState = data.authState) {
                            is AuthState.Authenticated -> currentState.copy(username = authState.user.username, isLoading = false)
                            is AuthState.Guest -> currentState.copy(username = authState.user.username, isLoading = false)
                            AuthState.Unauthenticated -> currentState.copy(isLoading = false)
                        }
                    }
                }
        }
    }

    fun onClearData() {
        viewModelScope.launch {
            try {
                userDataRepository.clearUserData()
                photosRepository.clearData()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                errorMonitor.tryEmit(e.message ?: "Unknown error")
            }
        }
    }
}