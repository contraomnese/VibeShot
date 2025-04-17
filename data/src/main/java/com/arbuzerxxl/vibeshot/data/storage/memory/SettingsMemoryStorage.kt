package com.arbuzerxxl.vibeshot.data.storage.memory

import android.R.attr.theme
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.arbuzerxxl.vibeshot.data.storage.api.SettingsStorage
import com.arbuzerxxl.vibeshot.data.storage.models.SettingsEntity
import com.arbuzerxxl.vibeshot.domain.models.ui.DarkThemeConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.settingsDataStore by preferencesDataStore(name = "settings")

class SettingsMemoryStorage(private val context: Context): SettingsStorage {

    private val dataStore = context.settingsDataStore

    private object PreferencesKeys {
        val THEME = stringPreferencesKey("app_theme")
    }

    override suspend fun setTheme(theme: DarkThemeConfig) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME] = theme.name
        }
    }

    override suspend fun clearSettings() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.THEME)
        }
    }

    override val settings: Flow<SettingsEntity> = dataStore.data
        .map { preferences ->
            SettingsEntity(
                theme = preferences[PreferencesKeys.THEME]?.let { themeName ->
                    DarkThemeConfig.valueOf(themeName)
                } ?: DarkThemeConfig.FOLLOW_SYSTEM
            )
        }
}