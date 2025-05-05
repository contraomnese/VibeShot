package com.arbuzerxxl.vibeshot.core.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import coil3.compose.AsyncImage
import coil3.request.ImageRequest.Builder
import coil3.request.crossfade
import com.arbuzerxxl.vibeshot.core.design.theme.itemHeight24
import com.arbuzerxxl.vibeshot.core.design.theme.padding8

@Composable
fun OwnerItem(
    modifier: Modifier = Modifier,
    owner: String,
    url: String
) {

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .size(itemHeight24)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = Builder(context)
                .data(url)
                .crossfade(true)
                .build(),
            contentDescription = "Owner icon",
        )
    }
    Text(
        modifier = Modifier.padding(start = padding8),
        text = owner,
        style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Light
        ),
        color = MaterialTheme.colorScheme.onSurface,
    )
}

