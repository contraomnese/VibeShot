package com.arbuzerxxl.vibeshot.core.ui.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.arbuzerxxl.vibeshot.core.design.icon.VibeShotIcons
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotThemePreview
import com.arbuzerxxl.vibeshot.core.design.theme.cornerSize16
import com.arbuzerxxl.vibeshot.core.design.theme.itemHeight24
import com.arbuzerxxl.vibeshot.core.design.theme.padding12
import com.arbuzerxxl.vibeshot.core.design.theme.padding16
import com.arbuzerxxl.vibeshot.core.design.theme.padding24
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews
import kotlinx.coroutines.delay

@Composable
fun BoxScope.ErrorBanner(
    modifier: Modifier = Modifier,
    message: String,
    visible: Boolean = false
) {

    var showBanner by remember { mutableStateOf(visible) }

    LaunchedEffect(message) {
        showBanner = true
        delay(3000)
        showBanner = false
    }

    AnimatedVisibility(
        modifier = modifier.align(Alignment.TopCenter),
        visible = showBanner,
        enter = slideInVertically { -it },
        exit = slideOutVertically { -it }
    ) {
        Box(
            modifier
                .padding(start = padding24, end = padding24, bottom = padding16)
                .clip(RoundedCornerShape(cornerSize16))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .fillMaxWidth()
                .alpha(0.8f)
                .zIndex(1f),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .padding(padding12)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(itemHeight24),
                    imageVector = VibeShotIcons.Error,
                    contentDescription = message,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    modifier = Modifier.padding(horizontal = padding16),
                    text = message,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    maxLines = 2,
                    softWrap = true,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
    }
}

@DevicePreviews
@Composable
fun ErrorBannerPreview(modifier: Modifier = Modifier) {
    VibeShotThemePreview {
        Box(modifier = Modifier.fillMaxSize()) {
            ErrorBanner(
                modifier = Modifier.align(Alignment.BottomCenter),
                message = "Message error",
                visible = true
            )
        }
    }
}

@DevicePreviews
@Composable
fun ErrorBannerWithLongErrorPreview(modifier: Modifier = Modifier) {
    VibeShotThemePreview {
        Box(modifier = Modifier.fillMaxSize()) {
            ErrorBanner(
                modifier = Modifier
                    .align(Alignment.BottomCenter),
                message = "Message error long long long long long long",
                visible = true
            )
        }
    }
}