package com.arbuzerxxl.vibeshot.data.repository

import com.arbuzerxxl.vibeshot.data.storage.api.SettingsStorage
import com.arbuzerxxl.vibeshot.domain.models.UserData
import com.arbuzerxxl.vibeshot.domain.models.ui.DarkThemeConfig
import com.arbuzerxxl.vibeshot.domain.repository.AuthRepository
import com.arbuzerxxl.vibeshot.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class UserDataRepositoryImpl(
    val authRepository: AuthRepository,
    val settingsStorage: SettingsStorage,
) : UserDataRepository {

    override val userData: Flow<UserData> = combine(
        authRepository.authState,
        settingsStorage.settings
    ) { user, settings ->
        UserData(
            user, settings.theme
        )
    }

    override suspend fun setTheme(darkTheme: Boolean) {
        settingsStorage.setTheme(if (darkTheme) DarkThemeConfig.DARK else DarkThemeConfig.LIGHT)
    }

    override suspend fun clearUserData() {
        authRepository.logOut()
        settingsStorage.clearSettings()
    }

}