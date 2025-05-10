package com.arbuzerxxl.vibeshot.features.start

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arbuzerxxl.vibeshot.core.design.icon.VibeShotIcons
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotThemePreview
import com.arbuzerxxl.vibeshot.core.design.theme.padding24
import com.arbuzerxxl.vibeshot.core.design.theme.padding32
import com.arbuzerxxl.vibeshot.core.design.theme.padding40
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews
import com.arbuzerxxl.vibeshot.core.ui.widgets.BaseButton
import com.arbuzerxxl.vibeshot.ui.R
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun StartRoute(
    modifier: Modifier = Modifier,
    viewModel: StartViewModel = koinViewModel(),
    onNavigateAfterStart: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StartScreen(
        modifier = modifier,
        uiState = uiState,
        setTheme = viewModel::setTheme,
        onStartClicked = onNavigateAfterStart
    )
}

@Composable
internal fun StartScreen(
    modifier: Modifier = Modifier,
    uiState: StartUiState,
    setTheme: (Boolean) -> Unit,
    onStartClicked: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            StartUiState.Loading -> Unit
            is StartUiState.Success -> {
                uiState.darkMode?.let {
                    IconButton(
                        modifier = Modifier.padding(top = padding32, end = padding32).align(Alignment.TopEnd),
                        onClick = { setTheme(!uiState.darkMode) }) {
                        if (!uiState.darkMode) Icon(
                            imageVector = VibeShotIcons.DarkMode,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        else Icon(
                            imageVector = VibeShotIcons.LightMode,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                } ?: setTheme(isSystemInDarkTheme())
                Text(
                    modifier = Modifier.padding(start = padding40, end = padding40).align(Alignment.Center),
                    text = stringResource(id = R.string.start_warning),
                    style = MaterialTheme.typography.labelMedium.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                BaseButton(
                    title = stringResource(id = R.string.get_started_title_button),
                    modifier = Modifier.padding(bottom = padding40, start = padding24, end = padding24).align(Alignment.BottomCenter),
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    onClicked = onStartClicked
                )
            }
        }

    }

}

@DevicePreviews
@Composable
private fun AuthScreenPreview() {
    VibeShotThemePreview {
        StartScreen(
            uiState = StartUiState.Success(darkMode = true),
            setTheme = { true },
            onStartClicked = {})
    }
}