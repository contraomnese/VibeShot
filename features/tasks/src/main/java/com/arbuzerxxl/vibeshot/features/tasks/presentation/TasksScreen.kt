package com.arbuzerxxl.vibeshot.features.tasks.presentation

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arbuzerxxl.vibeshot.core.design.icon.VibeShotIcons
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotThemePreview
import com.arbuzerxxl.vibeshot.core.design.theme.baseButtonHeight
import com.arbuzerxxl.vibeshot.core.design.theme.padding16
import com.arbuzerxxl.vibeshot.core.design.theme.padding8
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews
import com.arbuzerxxl.vibeshot.core.ui.widgets.BaseButton
import com.arbuzerxxl.vibeshot.core.ui.widgets.CollapsibleWidget
import com.arbuzerxxl.vibeshot.core.ui.widgets.LoadingIndicator
import com.arbuzerxxl.vibeshot.core.ui.widgets.TaskTags
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.MoodResource
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.SeasonResource
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.TaskCategoryResource
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.TaskResource
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.TopicResource
import com.arbuzerxxl.vibeshot.ui.R
import kotlinx.collections.immutable.persistentListOf
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun TasksRoute(
    modifier: Modifier = Modifier,
    viewmodel: TasksViewModel = koinViewModel(),
) {

    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    TasksScreen(
        modifier = modifier,
        uiState = uiState,
        onMoodClick = viewmodel::onMoodClick,
        onSeasonClick = viewmodel::onSeasonClick,
        onTopicClick = viewmodel::onTopicClick,
        onGenerateTaskClick = viewmodel::onGenerateTaskClick,
        onRefreshTaskClick = viewmodel::onRefreshTaskClick,
        onReceive = viewmodel::onReceive
    )
}

@Composable
internal fun TasksScreen(
    modifier: Modifier = Modifier,
    uiState: TasksUiState,
    onMoodClick: (String) -> Unit,
    onSeasonClick: (String) -> Unit,
    onTopicClick: (String) -> Unit,
    onGenerateTaskClick: () -> Unit,
    onRefreshTaskClick: () -> Unit,
    onReceive: (Intent) -> Unit,
) {

    Box(
        modifier = modifier
            .padding(padding16)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopStart
    ) {
        if (uiState.isLoading) LoadingIndicator(modifier = Modifier.align(Alignment.Center))
        else TaskContent(
            uiState = uiState,
            onGenerateTaskClick = onGenerateTaskClick,
            onMoodClick = onMoodClick,
            onSeasonClick = onSeasonClick,
            onTopicClick = onTopicClick,
            onRefreshTaskClick = onRefreshTaskClick,
            onReceive = onReceive
        )
    }
}

@Composable
private fun TaskContent(
    modifier: Modifier = Modifier,
    uiState: TasksUiState,
    onMoodClick: (String) -> Unit,
    onSeasonClick: (String) -> Unit,
    onTopicClick: (String) -> Unit,
    onGenerateTaskClick: () -> Unit,
    onRefreshTaskClick: () -> Unit,
    onReceive: (Intent) -> Unit,
) {

    val onRefreshTaskClick = remember { { onRefreshTaskClick() } }
    var expanded by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = padding8)
    ) {

        CollapsibleWidget(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            TaskTags(
                titleId = R.string.select_mood,
                items = uiState.categories.mood.moods,
                currentItem = uiState.mood,
                onClick = onMoodClick
            )
            TaskTags(
                titleId = R.string.select_season,
                items = uiState.categories.season.seasons,
                currentItem = uiState.season,
                onClick = onSeasonClick
            )
            TaskTags(
                titleId = R.string.select_topic,
                items = uiState.categories.topic.topics,
                currentItem = uiState.topic,
                onClick = onTopicClick
            )
        }


        Row(
            modifier = modifier
                .wrapContentWidth()
                .requiredHeight(baseButtonHeight)
                .align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(padding16)
        ) {

            TextButton(
                onClick = {
                    onGenerateTaskClick()
                    expanded = !expanded
                },
                enabled = uiState.tasks == null,
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

            Image(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(
                        onClick = onRefreshTaskClick,
                        enabled = uiState.tasks != null
                    ),
                imageVector = VibeShotIcons.Refresh,
                contentDescription = stringResource(R.string.refresh_task),
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface)
            )
        }

        uiState.task?.let {
            TaskContent(modifier = Modifier.align(Alignment.CenterHorizontally), task = it.task)
            CameraContent(
                uiState = uiState,
                onReceive = onReceive
            )
        }
    }
}

@Composable
private fun TaskContent(modifier: Modifier = Modifier, task: String) {
    Text(
        modifier = modifier
            .padding(vertical = padding16),
        text = task, style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )
    )
}

@Composable
private fun CameraContent(
    modifier: Modifier = Modifier,
    uiState: TasksUiState,
    onReceive: (Intent) -> Unit,
) {

    val currentContext = LocalContext.current

    val pickImageFromAlbumLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                onReceive(Intent.OnFinishPickingImagesWith(currentContext, listOf(uri)))
            }
        }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isImageSaved ->
        if (isImageSaved) {
            onReceive(Intent.OnImageSavedWith(currentContext))
        } else {
            onReceive(Intent.OnImageSavingCanceled)
        }
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
            if (permissionGranted) {
                onReceive(Intent.OnPermissionGrantedWith(currentContext))
            } else {
                onReceive(Intent.OnPermissionDenied)
            }
        }

    LaunchedEffect(key1 = uiState.tempFileUrl) {
        uiState.tempFileUrl?.let {
            cameraLauncher.launch(it)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BaseButton(
            modifier = Modifier.weight(1f),
            onClicked = {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }, title = "Take a photo"
        )
        Spacer(modifier = Modifier.width(16.dp))
        BaseButton(
            modifier = Modifier.weight(1f),
            onClicked = {
                val mediaRequest = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                pickImageFromAlbumLauncher.launch(mediaRequest)
            }, title = "Pick a picture"
        )
    }
}

@DevicePreviews
@Composable
private fun TasksScreenPreview() {
    VibeShotThemePreview {
        TasksScreen(
            uiState = TasksUiState(
                categories = TaskCategoryResource(
                    mood = MoodResource(persistentListOf()),
                    season = SeasonResource(persistentListOf()),
                    topic = TopicResource(persistentListOf())
                ),
                tasks = null, mood = "sad", season = "winter", topic = "nature",
                task = TaskResource("Example task")
            ),
            onGenerateTaskClick = {},
            onMoodClick = {},
            onSeasonClick = {},
            onTopicClick = {},
            onRefreshTaskClick = {},
            onReceive = {}
        )
    }
}
