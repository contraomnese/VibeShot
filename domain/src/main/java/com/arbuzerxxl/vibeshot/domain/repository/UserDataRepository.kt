package com.arbuzerxxl.vibeshot.domain.repository

import com.arbuzerxxl.vibeshot.domain.models.UserData
import com.arbuzerxxl.vibeshot.domain.models.ui.DarkThemeConfig
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    val userData: Flow<UserData>

    suspend fun setTheme(darkTheme: Boolean)

}