package com.arbuzerxxl.vibeshot.core.ui.widgets


import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun PhotoImagePlaceholder(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .animateContentSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    )
}

