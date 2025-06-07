package com.arbuzerxxl.vibeshot.core.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.arbuzerxxl.vibeshot.core.design.icon.VibeShotIcons
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotThemePreview
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews
import com.arbuzerxxl.vibeshot.ui.R

@Composable
fun ColumnScope.TaskGeneratorPanel(
    modifier: Modifier = Modifier,
    onGenerateTaskClick: () -> Unit,
    onRefreshTaskClick: () -> Unit,
    onCapturePictureClick: () -> Unit,
    onTakePictureClick: () -> Unit,
    onPublishPictureClick: () -> Unit,
    isRefreshTaskButtonEnabled: Boolean,
    isCapturePhotoButtonEnabled: Boolean,
    isTakePhotoButtonEnabled: Boolean,
    isPublishPhotoButtonEnabled: Boolean,
) {
    Row(
        modifier = modifier
            .align(Alignment.CenterHorizontally)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CircleIcon(
            onClick = onGenerateTaskClick,
            description = stringResource(R.string.generate_task),
            icon = VibeShotIcons.GenerateTask,
            title = "Generate",
        )
        CircleIcon(
            onClick = onRefreshTaskClick,
            description = stringResource(R.string.refresh_task),
            icon = VibeShotIcons.Refresh,
            title = "Refresh",
            enabled = isRefreshTaskButtonEnabled
        )
        CircleIcon(
            onClick = onCapturePictureClick,
            description = stringResource(R.string.capture_photo),
            icon = VibeShotIcons.Camera,
            title = "Capture",
            enabled = isCapturePhotoButtonEnabled
        )
        CircleIcon(
            onClick = onTakePictureClick,
            description = stringResource(R.string.take_photo),
            icon = VibeShotIcons.Gallery,
            title = "Gallery",
            enabled = isTakePhotoButtonEnabled
        )
        CircleIcon(
            onClick = onPublishPictureClick,
            description = stringResource(R.string.take_photo),
            icon = VibeShotIcons.Publish,
            title = "Publish",
            enabled = isPublishPhotoButtonEnabled
        )
    }
}

@DevicePreviews
@Composable
fun TaskGeneratorPanelPreview() {
    VibeShotThemePreview {
        Column {
            TaskGeneratorPanel(
                onGenerateTaskClick = {},
                onRefreshTaskClick = {},
                onCapturePictureClick = {},
                onTakePictureClick = {},
                onPublishPictureClick = {},
                isRefreshTaskButtonEnabled = true,
                isCapturePhotoButtonEnabled = false,
                isTakePhotoButtonEnabled = false,
                isPublishPhotoButtonEnabled = false,
            )
        }
    }
}