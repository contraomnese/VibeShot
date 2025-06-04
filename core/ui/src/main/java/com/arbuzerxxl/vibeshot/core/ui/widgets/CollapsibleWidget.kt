package com.arbuzerxxl.vibeshot.core.ui.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.arbuzerxxl.vibeshot.core.design.icon.VibeShotIcons
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotThemePreview
import com.arbuzerxxl.vibeshot.core.design.theme.padding8
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews

@Composable
fun CollapsibleWidget(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    onExpandedChange: () -> Unit,
    content: @Composable () -> Unit,
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = padding8),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
             Image(
                imageVector = if (isExpanded) VibeShotIcons.ArrowDropUp else VibeShotIcons.ArrowDropDown,
                contentDescription = if (isExpanded) "Hide" else "Show",
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground),
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(onClick = onExpandedChange)
                    .padding(horizontal = padding8)
            )
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(
                animationSpec = tween(durationMillis = 600)
            ),
            exit = shrinkVertically(
                animationSpec = tween(durationMillis = 600)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                content()
            }
        }
    }
}

@DevicePreviews
@Composable
private fun CollapsibleWidgetPreview() {
    VibeShotThemePreview {
        CollapsibleWidget(isExpanded = true, onExpandedChange = {}) {
            Text(text = "1234")
        }
    }
}