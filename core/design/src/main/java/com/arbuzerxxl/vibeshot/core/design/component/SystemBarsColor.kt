package com.arbuzerxxl.vibeshot.core.design.component

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun SystemBarsColorDisposableEffect(putInDark: Boolean) {
    val view = LocalView.current
    val currentSetting = isSystemInDarkTheme()
    DisposableEffect(putInDark) {
        val window = (view.context as Activity).window
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = putInDark
        WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = putInDark

        onDispose {
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = currentSetting
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = currentSetting
        }
    }
}