package com.arbuzerxxl.vibeshot.features.start

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotTheme
import com.arbuzerxxl.vibeshot.ui.R
import kotlinx.coroutines.delay

@Composable
internal fun StartRoute(
    modifier: Modifier = Modifier,
    navigateToAuth: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(4000)
        navigateToAuth()
    }

    StartScreen(
        modifier = modifier,
    )
}

@Composable
internal fun StartScreen(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineLarge
            )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun AuthScreenPreview() {
    VibeShotTheme {
        StartScreen()
    }
}