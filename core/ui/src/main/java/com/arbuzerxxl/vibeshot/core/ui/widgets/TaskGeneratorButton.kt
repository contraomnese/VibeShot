package com.arbuzerxxl.vibeshot.core.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arbuzerxxl.vibeshot.core.design.theme.baseButtonHeight
import com.arbuzerxxl.vibeshot.core.design.theme.padding16
import com.arbuzerxxl.vibeshot.ui.R

@Composable
fun ColumnScope.TaskGeneratorButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onRefreshTaskClick: () -> Unit,
    enabled: Boolean
) {
    Row(
        modifier = modifier
            .wrapContentWidth()
            .requiredHeight(baseButtonHeight)
            .align(Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(padding16)
    ) {

        TextButton(
            onClick = onClick,
            colors = ButtonColors(
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Text(
                text = "Generate", style = MaterialTheme.typography.labelLarge
            )
        }
        RefreshButton(
            onClick = onRefreshTaskClick, description = R.string.refresh_task, enabled = enabled
        )
    }
}