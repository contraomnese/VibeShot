package com.arbuzerxxl.vibeshot.core.ui.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.arbuzerxxl.vibeshot.core.design.icon.VibeShotIcons
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotThemePreview
import com.arbuzerxxl.vibeshot.core.design.theme.itemWidth2
import com.arbuzerxxl.vibeshot.core.design.theme.itemWidth48
import com.arbuzerxxl.vibeshot.core.design.theme.padding4
import com.arbuzerxxl.vibeshot.core.design.theme.padding8
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews

@Composable
fun CircleIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    title: String,
    description: String,
    icon: ImageVector,
    enabled: Boolean = true,
) {

    Column(
        modifier = Modifier.width(itemWidth48),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .clickable(
                    onClick = onClick,
                    enabled = enabled
                )
                .border(
                    width = itemWidth2,
                    color = if (enabled) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onBackground,
                    shape = CircleShape
                )
                .padding(padding8),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = description,
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
        Box {
            Text(
                modifier = Modifier.padding(top = padding4).wrapContentWidth(),
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                ),
                maxLines = 1,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@DevicePreviews
@Composable
private fun CircleIconPreview() {
    VibeShotThemePreview {
        CircleIcon(
            modifier = Modifier.width(itemWidth48),
            onClick = {},
            description = "vocibus",
            title = "Capture",
            icon = VibeShotIcons.Camera,
            enabled = true
        )
    }
}

@DevicePreviews
@Composable
private fun CircleIconDisabledPreview() {
    VibeShotThemePreview {
        CircleIcon(
            onClick = {},
            description = "vocibus",
            title = "Capture",
            icon = VibeShotIcons.Camera,
            enabled = false
        )
    }
}