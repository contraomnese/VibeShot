package com.arbuzerxxl.vibeshot.data.storage.api


import com.arbuzerxxl.vibeshot.data.storage.models.SettingsEntity
import com.arbuzerxxl.vibeshot.domain.models.ui.DarkThemeConfig
import kotlinx.coroutines.flow.Flow

interface SettingsStorage {

    val settings: Flow<SettingsEntity>
    suspend fun setTheme(theme: DarkThemeConfig)
    suspend fun clearSettings()
}