package com.arbuzerxxl.vibeshot.features.start

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arbuzerxxl.vibeshot.core.design.icon.VibeShotIcons
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotTheme
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews
import com.arbuzerxxl.vibeshot.features.start.di.startModule
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun StartRoute(
    modifier: Modifier = Modifier
) {
    rememberKoinModules(unloadOnForgotten = true) { listOf(startModule) }

    val startViewModel: StartViewModel = koinViewModel()
    val uiState by startViewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is StartUiState.Success -> StartScreen(
            modifier = modifier,
            uiState = uiState as StartUiState.Success,
            setTheme = startViewModel::setTheme
        )

        else -> Unit
    }
}

@Composable
internal fun StartScreen(
    modifier: Modifier = Modifier,
    uiState: StartUiState.Success,
    setTheme: (Boolean) -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Hello, ${uiState.username}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            uiState.darkMode?.let {
                IconButton(onClick = { setTheme(!uiState.darkMode) }) {
                    if (uiState.darkMode) Icon(imageVector = VibeShotIcons.DarkMode, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground)
                    else Icon(imageVector = VibeShotIcons.LightMode, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground)
                }
            } ?: setTheme(isSystemInDarkTheme())
        }
    }
}

@DevicePreviews
@Composable
private fun AuthScreenPreview() {
    VibeShotTheme {
        StartScreen(uiState = StartUiState.Success(username = "Sergey Belov", darkMode = false), setTheme = {true})
    }
}