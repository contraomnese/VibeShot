package com.arbuzerxxl.vibeshot.core.ui.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import com.arbuzerxxl.vibeshot.core.design.icon.VibeShotIcons
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotThemePreview
import com.arbuzerxxl.vibeshot.core.design.theme.padding24
import com.arbuzerxxl.vibeshot.core.design.theme.padding4
import com.arbuzerxxl.vibeshot.core.design.theme.padding40
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews
import com.arbuzerxxl.vibeshot.ui.R

@Composable
fun BoxScope.NetworkDisconnectionBanner(
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        modifier = modifier.alpha(0.8f).align(Alignment.TopEnd).padding(top = padding40, end = padding24),
        visible = true,
        enter = slideInHorizontally { -it },
        exit = slideOutHorizontally { -it }
    ) {
        Box(
            Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .zIndex(1f),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .padding(padding4)
                    .wrapContentWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = VibeShotIcons.SignalCellularOff,
                    contentDescription = stringResource(
                        id = R.string.no_internet_connection_error,
                    ),
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                )
                Icon(
                    imageVector = VibeShotIcons.WifiOff,
                    contentDescription = stringResource(
                        id = R.string.no_internet_connection_error,
                    ),
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                )
            }
        }
    }
}

@DevicePreviews
@Composable
private fun ConnectionBannerPreview() {
    VibeShotThemePreview {
        Box(modifier = Modifier.fillMaxSize()) {
            NetworkDisconnectionBanner()
        }

    }
}