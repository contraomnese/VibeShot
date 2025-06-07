package com.arbuzerxxl.vibeshot.features.tasks.presentation

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.arbuzerxxl.vibeshot.core.design.theme.cornerSize16
import com.arbuzerxxl.vibeshot.core.design.theme.padding16
import com.arbuzerxxl.vibeshot.core.design.theme.padding24
import com.arbuzerxxl.vibeshot.core.design.theme.padding32
import com.arbuzerxxl.vibeshot.core.design.theme.padding8
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews
import com.arbuzerxxl.vibeshot.core.ui.widgets.FormTextField
import com.arbuzerxxl.vibeshot.core.ui.widgets.LoadingIndicator
import com.arbuzerxxl.vibeshot.core.ui.widgets.TaskBox
import com.arbuzerxxl.vibeshot.core.ui.widgets.TaskGeneratorBoard
import com.arbuzerxxl.vibeshot.core.ui.widgets.TaskGeneratorPanel
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
        onEvent = viewmodel::onEvent,
        onChangeExpandedTaskGeneratorPanel = viewmodel::onExpandTaskGeneratorPanel
    )
}

@Composable
internal fun TasksScreen(
    modifier: Modifier = Modifier,
    uiState: TasksUiState,
    onEvent: (TasksEvent) -> Unit,
    onChangeExpandedTaskGeneratorPanel: () -> Unit,
) {

    Box(
        modifier = modifier
            .padding(padding16)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopStart
    ) {

        when {
            uiState.isLoading -> LoadingIndicator(modifier = Modifier.align(Alignment.Center))
            else -> TasksContent(
                uiState = uiState,
                onEvent = onEvent,
                onChangeExpandedTaskGeneratorPanel = onChangeExpandedTaskGeneratorPanel
            )
        }

    }
}

@Composable
private fun TasksContent(
    modifier: Modifier = Modifier,
    uiState: TasksUiState,
    onEvent: (TasksEvent) -> Unit,
    onChangeExpandedTaskGeneratorPanel: () -> Unit,
) {
    val currentContext = LocalContext.current

    val intentEvent = remember { { intent: PhotoTaskIntent -> onEvent(TasksEvent.ReceivePhotoTaskIntent(intent)) } }
    val pickImageFromAlbumLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            val intent = PhotoTaskIntent.OnFinishPickingImageWith(
                currentContext,
                uri
            )
            intentEvent(intent)
        }
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
            if (permissionGranted) {
                intentEvent(PhotoTaskIntent.OnPermissionGrantedWith(currentContext))
            } else {
                intentEvent(PhotoTaskIntent.OnPermissionDenied)
            }
        }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isImageSaved ->
        if (isImageSaved) {
            intentEvent(PhotoTaskIntent.OnImageSavedWith(currentContext))
        } else {
            intentEvent(PhotoTaskIntent.OnImageSavingCanceled)
        }
    }

    val moodClickEvent = remember {
        { title: String -> onEvent(TasksEvent.MoodClicked(title)) }
    }
    val seasonClickEvent = remember {
        { title: String -> onEvent(TasksEvent.SeasonClicked(title)) }
    }
    val topicClickEvent = remember {
        { title: String -> onEvent(TasksEvent.TopicClicked(title)) }
    }
    val publishTitleChangeEvent = remember {
        { title: String -> onEvent(TasksEvent.PublishTitleChange(title)) }
    }
    val publishDescriptionChangeEvent = remember {
        { description: String -> onEvent(TasksEvent.PublishDescriptionChange(description)) }
    }

    val onGenerateTaskEvent = remember { { onEvent(TasksEvent.GenerateTaskClicked) } }
    val onRefreshTaskEvent = remember { { onEvent(TasksEvent.RefreshTaskClicked) } }
    val onCapturePhotoEvent = remember { { permissionLauncher.launch(Manifest.permission.CAMERA) } }
    val onTakePhotoFromGalleryEvent = remember { {
        val mediaRequest = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        pickImageFromAlbumLauncher.launch(mediaRequest)
    } }
    val onPublishPhotoEvent = remember { { onEvent(TasksEvent.PublishClicked) } }


    LaunchedEffect(uiState.selectedPictureTempFileUrl) {
        uiState.selectedPictureTempFileUrl?.let {
            cameraLauncher.launch(it)
        }
    }

    val scrollState = rememberScrollState()

    uiState.categoriesForTaskGeneration?.let { categories ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = padding8),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TaskGeneratorBoard(
                categoryResource = categories,
                currentMood = uiState.selectionMoodTitle(),
                currentSeason = uiState.selectionSeasonTitle(),
                currentTopic = uiState.selectionTopicTitle(),
                onMoodClicked = moodClickEvent,
                onSeasonClicked = seasonClickEvent,
                onTopicClicked = topicClickEvent,
                isExpanded = uiState.isExpandedTaskGeneratorPanel,
                onExpandedChange = onChangeExpandedTaskGeneratorPanel
            )
            TaskGeneratorPanel(
                modifier = Modifier.fillMaxWidth(),
                onGenerateTaskClick = onGenerateTaskEvent,
                onRefreshTaskClick = onRefreshTaskEvent,
                onCapturePictureClick = onCapturePhotoEvent,
                onTakePictureClick = onTakePhotoFromGalleryEvent,
                onPublishPictureClick = onPublishPhotoEvent,
                isRefreshTaskButtonEnabled = uiState.tasks != null,
                isCapturePhotoButtonEnabled = uiState.currentTask != null,
                isTakePhotoButtonEnabled = uiState.currentTask != null,
                isPublishPhotoButtonEnabled = uiState.selectedPicture != null,
            )
            uiState.currentTask?.let {
                TaskBox(task = it.task)
            }
            when (uiState.photoUploadStatus) {
                PhotoUploadStatus.Idle -> uiState.selectedPicture?.let {
                    FormTextField(
                        value = uiState.publishTitle,
                        onChange = publishTitleChangeEvent,
                        placeholder = "Title"
                    )
                    FormTextField(
                        value = uiState.publishDescription,
                        onChange = publishDescriptionChangeEvent,
                        placeholder = "Description"
                    )
                    SelectedPictureContent(picture = it)
                }
                PhotoUploadStatus.Loading -> Box(
                    modifier = modifier
                        .size(64.dp)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator()
                }
                PhotoUploadStatus.Failed -> PhotoUploadStatus(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = padding24),
                    iconVector = VibeShotIcons.Error,
                    boxColor = MaterialTheme.colorScheme.error,
                    contentDescription = stringResource(R.string.loading_error),
                )
                PhotoUploadStatus.Success -> PhotoUploadStatus(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = padding24),
                    iconVector = VibeShotIcons.Check,
                    boxColor = MaterialTheme.colorScheme.tertiary,
                    contentDescription = stringResource(R.string.photo_upload_success),
                )
            }

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
private fun SelectedPictureContent(picture: ImageBitmap) {
    Column {
        Image(
            modifier = Modifier
                .clip(RoundedCornerShape(cornerSize16)),
            bitmap = picture,
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
    }
}

@Composable
private fun PhotoUploadStatus(
    modifier: Modifier,
    iconVector: ImageVector,
    contentDescription: String,
    boxColor: Color,
) {

    var showUploadStatus by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showUploadStatus = true
        delay(5000)
        showUploadStatus = false
    }

    val transition = updateTransition(targetState = showUploadStatus, label = "PhotoUploadTransition")

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

    Box(
        modifier = modifier
            .size(64.dp)
            .scale(scale)
            .alpha(alpha)
            .background(color = boxColor, shape = CircleShape)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = iconVector,
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier.size(32.dp)
        )
    }
}

@DevicePreviews
@Composable
private fun TasksScreenEmptyPreview() {
    VibeShotThemePreview {
        TasksScreen(
            uiState = TasksUiState(),
            onEvent = {},
            onChangeExpandedTaskGeneratorPanel = {}
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
                tasks = null,
                onSelectionMood = 1,
                onSelectionSeason = 2,
                onSelectionTopic = 1,
                currentTask = TaskResource("Example task"),
            ),
            onEvent = {},
            onChangeExpandedTaskGeneratorPanel = {}
        )
    }
}

@DevicePreviews
@Composable
private fun TasksScreenUploadStatusSuccessPreview() {
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
                tasks = null,
                currentTask = TaskResource("Example task"),
                isExpandedTaskGeneratorPanel = false,
                photoUploadStatus = PhotoUploadStatus.Loading
            ),
            onEvent = {},
            onChangeExpandedTaskGeneratorPanel = {}
        )
    }
}
