package com.arbuzerxxl.vibeshot.core.ui.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.arbuzerxxl.vibeshot.core.design.icon.VibeShotIcons
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotThemePreview
import com.arbuzerxxl.vibeshot.core.design.theme.cornerSize16
import com.arbuzerxxl.vibeshot.core.design.theme.padding12
import com.arbuzerxxl.vibeshot.core.design.theme.padding16
import com.arbuzerxxl.vibeshot.core.design.theme.padding8
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews
import com.arbuzerxxl.vibeshot.ui.R

@Composable
fun ConnectionBanner(
    modifier: Modifier,
) {
    AnimatedVisibility(
        modifier = modifier.alpha(0.8f),
        visible = true,
        enter = slideInVertically { -it },
        exit = slideOutVertically { -it }
    ) {
        Box(
            Modifier
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(cornerSize16))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .fillMaxWidth()
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
                    imageVector = VibeShotIcons.InternetDisabled,
                    contentDescription = stringResource(
                        id = R.string.no_internet_connection_error,
                    ),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    modifier = Modifier.padding(start = padding8),
                    text = stringResource(R.string.no_internet_connection_error),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
    }
}

@DevicePreviews
@Composable
private fun ConnectionBannerPreview(modifier: Modifier = Modifier) {
    VibeShotThemePreview {
        Box(modifier = Modifier.fillMaxSize()) {
            ConnectionBanner(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = padding16),
            )
        }

    }
}