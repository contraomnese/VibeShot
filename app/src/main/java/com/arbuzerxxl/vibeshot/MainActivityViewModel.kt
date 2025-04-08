package com.arbuzerxxl.vibeshot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arbuzerxxl.vibeshot.MainActivityUiState.Loading
import com.arbuzerxxl.vibeshot.MainActivityUiState.Success
import com.arbuzerxxl.vibeshot.domain.models.UserData
import com.arbuzerxxl.vibeshot.domain.models.auth.AuthState
import com.arbuzerxxl.vibeshot.domain.models.ui.DarkThemeConfig
import com.arbuzerxxl.vibeshot.domain.repository.UserDataRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    val uiState: StateFlow<MainActivityUiState> = userDataRepository.userData
        .map { Success(it) }
        .stateIn(
            scope = viewModelScope,
            initialValue = Loading,
            started = SharingStarted.WhileSubscribed(5_000),
        )

    private val _keepSplashScreen = MutableStateFlow(true)
    val keepSplashScreen: StateFlow<Boolean> = _keepSplashScreen.asStateFlow()

    fun disableSplashScreen() {
        viewModelScope.launch {
            delay(2000)
            _keepSplashScreen.update {
                false
            }
        }
    }
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState {
        override fun shouldSkipAuth(): Boolean = false
        override fun shouldUseDarkTheme(isSystemDarkTheme: Boolean) = isSystemDarkTheme
    }

    data class Success(val userData: UserData) : MainActivityUiState {

        override fun shouldSkipAuth(): Boolean = when (userData.authState) {
            is AuthState.Authenticated, is AuthState.Guest -> true
            AuthState.Unauthenticated -> false
        }

        override fun shouldUseDarkTheme(isSystemDarkTheme: Boolean) = when (userData.theme) {
            DarkThemeConfig.FOLLOW_SYSTEM -> isSystemDarkTheme
            DarkThemeConfig.LIGHT -> false
            DarkThemeConfig.DARK -> true
        }
    }

    fun shouldKeepSplashScreen(): Boolean = this is Loading

    fun shouldSkipAuth(): Boolean
    fun shouldUseDarkTheme(isSystemDarkTheme: Boolean): Boolean
}