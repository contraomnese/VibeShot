package com.arbuzerxxl.vibeshot.features.interests.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arbuzerxxl.vibeshot.core.ui.widgets.LoadingIndicator
import com.arbuzerxxl.vibeshot.features.interests.di.interestsModule
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun InterestsRoute(
    modifier: Modifier = Modifier,
) {
    rememberKoinModules(unloadOnForgotten = true) { listOf(interestsModule) }

    val viewmodel: InterestsViewModel = koinViewModel()
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    InterestsScreen(
        modifier = modifier,
        uiState = uiState,
        )
}

@Composable
internal fun InterestsScreen(
    modifier: Modifier = Modifier,
    uiState: InterestsUiState,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            InterestsUiState.Loading -> LoadingIndicator()
            is InterestsUiState.Success -> Text(text = "Interests loaded")
        }

    }
}