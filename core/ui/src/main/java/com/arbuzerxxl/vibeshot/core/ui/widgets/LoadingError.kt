package com.arbuzerxxl.vibeshot.core.ui.widgets

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.arbuzerxxl.vibeshot.core.design.icon.VibeShotIcons
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotThemePreview
import com.arbuzerxxl.vibeshot.core.design.theme.itemWidth48
import com.arbuzerxxl.vibeshot.core.design.theme.padding32
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews
import com.arbuzerxxl.vibeshot.ui.R

@Composable
fun LoadingError(
    modifier: Modifier = Modifier,
    @StringRes textResId: Int,
    onRefreshClick: () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(itemWidth48),
                imageVector = VibeShotIcons.Error,
                contentDescription = stringResource(
                    id = R.string.loading_error,
                ),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                modifier = modifier
                    .padding(padding32),
                text = stringResource(textResId),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            )
            RefreshButton(
                onClick = onRefreshClick, description = R.string.load_interests
            )
        }
    }
}

@DevicePreviews
@Composable
private fun LoadingErrorPreview(modifier: Modifier = Modifier) {
    VibeShotThemePreview {
        LoadingError(textResId = R.string.interests_load_error, onRefreshClick = { })
    }
}