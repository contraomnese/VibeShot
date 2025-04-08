package com.arbuzerxxl.vibeshot.domain.models

import com.arbuzerxxl.vibeshot.domain.models.auth.AuthState
import com.arbuzerxxl.vibeshot.domain.models.ui.DarkThemeConfig

data class UserData(
    val authState: AuthState,
    val theme: DarkThemeConfig
)

