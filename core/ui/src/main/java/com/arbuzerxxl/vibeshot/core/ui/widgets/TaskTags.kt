package com.arbuzerxxl.vibeshot.core.ui.widgets

import androidx.annotation.StringRes
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotThemePreview
import com.arbuzerxxl.vibeshot.core.design.theme.padding8
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews
import com.arbuzerxxl.vibeshot.ui.R

@Composable
fun TaskTags(
    modifier: Modifier = Modifier,
    @StringRes titleId: Int,
    items: List<String>,
    currentItem: String,
    onClick: (String) -> Unit
) {

    Column(modifier = modifier) {
        Text(
            text = stringResource(titleId), style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onSurface,
            )
        )
        if (items.isNotEmpty()) {
            val scrollState = rememberScrollState()
            var currentItem by remember { mutableStateOf(currentItem) }

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
                            currentItem = tag
                            onClick(tag)
                        },
                        selected = tag == currentItem,
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
            ),
            titleId = R.string.select_mood,
            currentItem = "",
            onClick = {}
        )
    }
}