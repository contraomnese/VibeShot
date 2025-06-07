package com.arbuzerxxl.vibeshot.core.design.theme

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


private val DarkColorScheme = darkColorScheme(
    primary = CoralPink,
    onPrimary = Color.Black,
    primaryContainer = RichCoral,
    onPrimaryContainer = Color.White,

    secondary = Turquoise,
    onSecondary = Color.Black,
    secondaryContainer = DarkTurquoise,
    onSecondaryContainer = Color.White,

    background = DarkToneInk,
    onBackground = Color.White,
    surface = CarbonFiber,
    onSurface = Color.White,
    surfaceVariant = Carbon,
    onSurfaceVariant = TangledWeb,

    error = FluorescentRed,
    onError = Color.Black,
    errorContainer = MutedRed,
    onErrorContainer = Color.White,

    tertiary = ScreenGlow,
    onTertiary = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = CoralPink,
    onPrimary = Color.White,
    primaryContainer = CoralLight,
    onPrimaryContainer = DarkBurgundy,

    secondary = Turquoise,
    onSecondary = Color.White,
    secondaryContainer = TurquoiseLight,
    onSecondaryContainer = StellarExplorer,

    background = Brilliance,
    onBackground = EerieBlack,
    surface = Color.White,
    onSurface = EerieBlack,
    surfaceVariant = GrayLight,
    onSurfaceVariant = Gray,

    error = MutedRed,
    onError = Color.White,
    errorContainer = ForgottenPink,
    onErrorContainer = Maroon,

    tertiary = Green,
    onTertiary = Color.White
)

@Composable
fun VibeShotTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && supportsDynamicTheming() -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = vibeTypography,
        content = content
    )
}

@Composable
fun VibeShotThemePreview(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = vibeTypographyPreview,
        content = content
    )
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S