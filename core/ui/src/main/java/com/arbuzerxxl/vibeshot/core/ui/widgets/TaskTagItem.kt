package com.arbuzerxxl.vibeshot.core.ui.widgets


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotThemePreview
import com.arbuzerxxl.vibeshot.core.design.theme.cornerRadius6
import com.arbuzerxxl.vibeshot.core.design.theme.cornerRadius8
import com.arbuzerxxl.vibeshot.core.design.theme.itemHeight160
import com.arbuzerxxl.vibeshot.core.design.theme.itemWidth80
import com.arbuzerxxl.vibeshot.core.design.theme.padding4
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews

@Composable
fun TaskTagItem(
    tag: String,
    selected: Boolean,
    onClick: () -> Unit,
) {

    val onClick = remember { { onClick() } }

    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(cornerRadius8))
            .clickable(onClick = onClick)
            .height(itemHeight160)
            .width(itemWidth80),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContentColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = RoundedCornerShape(cornerRadius8),
        border = BorderStroke(
            width = 2.dp,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        Box(
            modifier = Modifier
                .padding(padding4)
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(cornerRadius6)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(horizontal = padding4),
                text = tag,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Light,
                    fontSize = 10.sp
                ),
                maxLines = 1,
                softWrap = true
            )
        }
    }
}

@DevicePreviews
@Composable
private fun TaskTagItemPreview() {
    VibeShotThemePreview {
        TaskTagItem(
            tag = "Winter",
            selected = true,
            onClick = {}
        )
    }
}