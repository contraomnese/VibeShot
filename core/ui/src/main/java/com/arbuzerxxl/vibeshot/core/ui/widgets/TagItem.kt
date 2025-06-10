package com.arbuzerxxl.vibeshot.core.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotTheme
import com.arbuzerxxl.vibeshot.core.design.theme.cornerRadius8
import com.arbuzerxxl.vibeshot.core.design.theme.itemWidth0_5
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews

@Composable
fun TagItem(
    tag: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .border(
                width = itemWidth0_5,
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(cornerRadius8)
            )
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
                shape = RoundedCornerShape(cornerRadius8)
            )
            .clickable(onClick = onClick)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            text = tag,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Light
            )
        )
    }
}

@DevicePreviews
@Composable
fun TagItemPreview(modifier: Modifier = Modifier) {
    VibeShotTheme {
        TagItem(
            tag = "lion", onClick = {},
        )
    }
}