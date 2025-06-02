package com.arbuzerxxl.vibeshot.features.tasks.presentation

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
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
import com.arbuzerxxl.vibeshot.core.design.theme.cornerSize16
import com.arbuzerxxl.vibeshot.core.design.theme.itemWidth1
import com.arbuzerxxl.vibeshot.core.design.theme.padding16
import com.arbuzerxxl.vibeshot.core.design.theme.padding24
import com.arbuzerxxl.vibeshot.core.design.theme.padding32
import com.arbuzerxxl.vibeshot.core.design.theme.padding8
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews
import com.arbuzerxxl.vibeshot.core.ui.widgets.BaseButton
import com.arbuzerxxl.vibeshot.core.ui.widgets.CollapsibleWidget
import com.arbuzerxxl.vibeshot.core.ui.widgets.LoadingIndicator
import com.arbuzerxxl.vibeshot.core.ui.widgets.RefreshButton
import com.arbuzerxxl.vibeshot.core.ui.widgets.TaskTags
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.MoodResource
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.SeasonResource
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.TaskCategoryResource
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.TaskResource
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.TopicResource
import com.arbuzerxxl.vibeshot.ui.R
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

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
        onReceive = viewmodel::onReceive,
        onPublishClicked = viewmodel::onPublishClick,
        clearError = viewmodel::clearError,
        clearNotification = viewmodel::clearNotification
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
    onReceive: (PhotoTaskIntent) -> Unit,
    onPublishClicked: () -> Unit,
    clearError: () -> Unit,
    clearNotification: () -> Unit,
) {

    val context = LocalContext.current

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, uiState.error, Toast.LENGTH_SHORT).show()
            clearError()
        }
    }

    LaunchedEffect(uiState.notification) {
        uiState.notification?.let {
            Toast.makeText(context, uiState.notification, Toast.LENGTH_LONG).show()
            clearNotification()
        }
    }

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
            onReceive = onReceive,
            onPublishClicked = onPublishClicked
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
    onReceive: (PhotoTaskIntent) -> Unit,
    onPublishClicked: () -> Unit,
) {

    val onRefreshTaskClicked = remember { { onRefreshTaskClick() } }
    var expanded by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()

    uiState.categoriesForTaskGeneration?.let { categories ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = padding8)
        ) {
            CollapsibleWidget(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                TaskTags(
                    titleId = R.string.select_mood,
                    items = categories.moods.map { it.title },
                    currentItem = uiState.selectionMoodTitle(),
                    onClick = onMoodClick
                )
                TaskTags(
                    titleId = R.string.select_season,
                    items = categories.seasons.map { it.title },
                    currentItem = uiState.selectionSeasonTitle(),
                    onClick = onSeasonClick
                )
                TaskTags(
                    titleId = R.string.select_topic,
                    items = categories.topics.map { it.title },
                    currentItem = uiState.selectionTopicTitle(),
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
                RefreshButton(
                    onClick = onRefreshTaskClicked, description = R.string.refresh_task, enabled = uiState.tasks != null
                )
            }
            uiState.currentTask?.let {
                Box(
                    modifier = Modifier
                        .padding(vertical = padding16)
                        .border(
                            width = itemWidth1,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RoundedCornerShape(cornerSize16)
                        )
                        .fillMaxWidth(), contentAlignment = Alignment.Center
                ) {
                    TaskContent(message = it.task)
                }
                CameraContent(
                    uiState = uiState,
                    onReceive = onReceive,
                    onPublishClicked = onPublishClicked
                )
            }
            PhotoUploadStatus(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = padding24),
                photoId = uiState.photoId,
                expandCallback = { expanded = !expanded }
            )
        }
    } ?: TasksContentEmpty()
}

@Composable
private fun TasksContentEmpty(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            modifier = modifier
                .align(Alignment.Center)
                .padding(padding32),
            text = stringResource(R.string.categories_empty), style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
        )
    }
}

@Composable
private fun TaskContent(modifier: Modifier = Modifier, message: String) {
    Text(
        modifier = modifier
            .padding(padding16),
        text = message, style = MaterialTheme.typography.labelMedium.copy(
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
    onReceive: (PhotoTaskIntent) -> Unit,
    onPublishClicked: () -> Unit,
) {

    val currentContext = LocalContext.current

    val pickImageFromAlbumLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            onReceive(
                PhotoTaskIntent.OnFinishPickingImageWith(
                    currentContext,
                    uri
                )
            )
        }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isImageSaved ->
        if (isImageSaved) {
            onReceive(PhotoTaskIntent.OnImageSavedWith(currentContext))
        } else {
            onReceive(PhotoTaskIntent.OnImageSavingCanceled)
        }
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
            if (permissionGranted) {
                onReceive(PhotoTaskIntent.OnPermissionGrantedWith(currentContext))
            } else {
                onReceive(PhotoTaskIntent.OnPermissionDenied)
            }
        }

    LaunchedEffect(uiState.selectedPictureTempFileUrl) {
        uiState.selectedPictureTempFileUrl?.let {
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
    uiState.selectedPicture?.let {
        SelectedPictureContent(picture = it, onPublishClicked = onPublishClicked)
    }
}

@Composable
private fun SelectedPictureContent(picture: ImageBitmap, onPublishClicked: () -> Unit) {
    Column {
        Image(
            modifier = Modifier
                .padding(padding16)
                .clip(RoundedCornerShape(cornerSize16)),
            bitmap = picture,
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
        BaseButton(
            modifier = Modifier
                .padding(vertical = padding16)
                .fillMaxWidth(),
            onClicked = onPublishClicked,
            title = "Publish"
        )
    }
}

@Composable
private fun PhotoUploadStatus(modifier: Modifier, photoId: String?, expandCallback: () -> Unit) {

    var showSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(photoId) {
        if (photoId != null) {
            showSuccess = true
            delay(3000)
            showSuccess = false
            delay(2000)
            expandCallback()
        }
    }

    val transition = updateTransition(targetState = showSuccess, label = "PhotoUploadTransition")

    val scale by transition.animateFloat(
        label = "ScaleAnimation",
        transitionSpec = {
            tween(durationMillis = 500, easing = FastOutSlowInEasing)
        }
    ) { isVisible -> if (isVisible) 1f else 0f }

    val alpha by transition.animateFloat(
        label = "AlphaAnimation",
        transitionSpec = {
            tween(durationMillis = 500)
        }
    ) { isVisible -> if (isVisible) 1f else 0f }

    if (showSuccess || transition.currentState) {
        Box(
            modifier = modifier
                .size(64.dp)
                .scale(scale)
                .alpha(alpha)
                .background(Color(0xFF4CAF50), shape = CircleShape)
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = VibeShotIcons.Check,
                contentDescription = stringResource(R.string.photo_upload_success),
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@DevicePreviews
@Composable
private fun TasksScreenEmptyPreview() {
    VibeShotThemePreview {
        TasksScreen(
            uiState = TasksUiState(
                categoriesForTaskGeneration = null,
                tasks = null,
                currentTask = null,
                photoId = ""
            ),
            onGenerateTaskClick = {},
            onMoodClick = {},
            onSeasonClick = {},
            onTopicClick = {},
            onRefreshTaskClick = {},
            onReceive = {},
            onPublishClicked = {},
            clearNotification = {},
            clearError = {}
        )
    }
}


@DevicePreviews
@Composable
private fun TasksScreenPreview() {
    VibeShotThemePreview {

        val moodResource = List(6) { MoodResource("example mood") }.toImmutableList()
        val seasonResource = List(4) { SeasonResource("example season") }.toImmutableList()
        val topicResource = List(8) { TopicResource("example topic") }.toImmutableList()

        TasksScreen(
            uiState = TasksUiState(
                categoriesForTaskGeneration = TaskCategoryResource(
                    moods = moodResource,
                    seasons = seasonResource,
                    topics = topicResource
                ),
                tasks = null, onSelectionMood = 1, onSelectionSeason = 2, onSelectionTopic = 1,
                currentTask = TaskResource("Example task"), photoId = "1234"
            ),
            onGenerateTaskClick = {},
            onMoodClick = {},
            onSeasonClick = {},
            onTopicClick = {},
            onRefreshTaskClick = {},
            onReceive = {},
            onPublishClicked = {},
            clearNotification = {},
            clearError = {}
        )
    }
}
