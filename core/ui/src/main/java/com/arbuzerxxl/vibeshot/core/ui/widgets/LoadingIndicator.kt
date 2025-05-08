package com.arbuzerxxl.vibeshot.core.ui.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotThemePreview
import com.arbuzerxxl.vibeshot.core.design.theme.itemHeight2
import com.arbuzerxxl.vibeshot.core.design.theme.itemWidth64
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary,
        trackColor = MaterialTheme.colorScheme.surface,
        strokeWidth = itemHeight2,
    )
}

@DevicePreviews
@Composable
fun LoadingIndicatorPreview(modifier: Modifier = Modifier) {
    VibeShotThemePreview {
        Box(Modifier.fillMaxSize()) {
            LoadingIndicator(Modifier.align(Alignment.Center).width(itemWidth64))
        }
    }
}