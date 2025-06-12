package com.arbuzerxxl.vibeshot.core.ui.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotThemePreview
import com.arbuzerxxl.vibeshot.core.design.theme.cornerSize16
import com.arbuzerxxl.vibeshot.core.design.theme.itemWidth1
import com.arbuzerxxl.vibeshot.core.design.theme.padding16
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews

@Composable
fun ColumnScope.TaskBox(
    modifier: Modifier = Modifier,
    task: String?,
) {
    var previousTask by remember { mutableStateOf<String?>(null) }
    var shouldAnimate by remember { mutableStateOf(false) }

    LaunchedEffect(task) {
        if (task != null && task != previousTask) {
            shouldAnimate = true
            previousTask = task
        } else {
            shouldAnimate = false
        }
    }

    task?.let {
        Box(
            modifier = Modifier
                .padding(vertical = padding16)
                .border(
                    width = itemWidth1,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(cornerSize16)
                )
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (shouldAnimate) {
                TypingText(
                    modifier = modifier.padding(padding16),
                    text = it,
                    typingSpeed = 80L,
                    textStyle = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                )
            } else {
                Text(
                    modifier = modifier.padding(padding16),
                    text = it,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}

@DevicePreviews
@Composable
fun TaskBoxPreview(modifier: Modifier = Modifier) {
    VibeShotThemePreview {
        Column {
            TaskBox(
                task = "Task"
            )
        }
    }
}