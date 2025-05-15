package com.arbuzerxxl.vibeshot.core.ui.widgets

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotThemePreview
import com.arbuzerxxl.vibeshot.core.design.theme.padding8
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews

@Composable
fun TaskTags(
    modifier: Modifier = Modifier,
    title: String,
    items: List<String>,
) {

    var enabled by remember { mutableStateOf(true) }

    Column(modifier = modifier) {
        Text(
            text = title, style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onSurface,
            )
        )
        if (items.isNotEmpty()) {
            val scrollState = rememberScrollState()

            Row(
                modifier = Modifier
                    .horizontalScroll(scrollState)
                    .padding(vertical = padding8),
                horizontalArrangement = Arrangement.spacedBy(padding8)
            ) {
                items.forEach { tag ->
                    TaskTagItem(
                        tag = tag.uppercase(),
                        onClick = {
                            enabled = !enabled
                        },
                        enabled = enabled,
                    )
                }
            }
        }
    }

}


@DevicePreviews
@Composable
private fun TaskTagsPreview() {
    VibeShotThemePreview {
        TaskTags(
            title = "Mood",
            items = listOf(
                "joyful",
                "sad",
                "calm",
                "irritated",
                "anxious",
                "angry",
                "inspired",
                "tired",
                "romantic",
                "neutral"
            )
        )
    }
}