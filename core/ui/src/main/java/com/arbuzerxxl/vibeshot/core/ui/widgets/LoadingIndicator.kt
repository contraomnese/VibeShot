package com.arbuzerxxl.vibeshot.core.ui.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotThemePreview
import com.arbuzerxxl.vibeshot.core.design.theme.itemWidth3
import com.arbuzerxxl.vibeshot.core.design.theme.itemWidth64
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
        strokeWidth = itemWidth3,
        strokeCap = StrokeCap.Square
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