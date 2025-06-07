package com.arbuzerxxl.vibeshot.features.tasks.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arbuzerxxl.vibeshot.core.ui.utils.ErrorMonitor
import com.arbuzerxxl.vibeshot.domain.models.app.ApplicationId
import com.arbuzerxxl.vibeshot.domain.models.auth.AuthState
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.TaskCategoryResource
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.TaskResource
import com.arbuzerxxl.vibeshot.domain.repository.PhotoTasksRepository
import com.arbuzerxxl.vibeshot.domain.repository.PhotosRepository
import com.arbuzerxxl.vibeshot.domain.repository.UserDataRepository
import com.arbuzerxxl.vibeshot.domain.usecases.photo_tasks.GetPhotoTasksUseCase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import kotlin.coroutines.cancellation.CancellationException

private const val UNKNOWN_ERROR = "Unknown error"

internal sealed class PhotoTaskIntent {
    data class OnPermissionGrantedWith(val compositionContext: Context) : PhotoTaskIntent()
    data object OnPermissionDenied : PhotoTaskIntent()
    data class OnImageSavedWith(val compositionContext: Context) : PhotoTaskIntent()
    data object OnImageSavingCanceled : PhotoTaskIntent()
    data class OnFinishPickingImageWith(val compositionContext: Context, val imageUrl: Uri?) : PhotoTaskIntent()
}

@Immutable
internal data class TasksUiState(
    val isLoading: Boolean = false,
    val categoriesForTaskGeneration: TaskCategoryResource? = null,
    val onSelectionMood: Int = 0,
    val onSelectionSeason: Int = 0,
    val onSelectionTopic: Int = 0,
    val tasks: ImmutableList<TaskResource>? = null,
    val currentTask: TaskResource? = null,
    val selectedPictureTempFileUrl: Uri? = null,
    val selectedPictureFileUrl: Uri? = null,
    val selectedPicture: ImageBitmap? = null,
    val authState: AuthState.Authenticated? = null,
    val photoUploadStatus: PhotoUploadStatus = PhotoUploadStatus.Idle,
    val isExpandedTaskGeneratorPanel: Boolean = true,
    val publishTitle: String = "",
    val publishDescription: String = "",
) {
    fun selectionMoodTitle(): String = categoriesForTaskGeneration!!.moods[onSelectionMood].title
    fun selectionSeasonTitle(): String = categoriesForTaskGeneration!!.seasons[onSelectionSeason].title
    fun selectionTopicTitle(): String = categoriesForTaskGeneration!!.topics[onSelectionTopic].title
}

@Immutable
internal sealed class TasksEvent {
    data object RefreshTaskClicked : TasksEvent()
    data object GenerateTaskClicked : TasksEvent()
    data object PublishClicked : TasksEvent()
    data class MoodClicked(val title: String) : TasksEvent()
    data class SeasonClicked(val title: String) : TasksEvent()
    data class TopicClicked(val title: String) : TasksEvent()
    data class ReceivePhotoTaskIntent(val intent: PhotoTaskIntent) : TasksEvent()
    data class PublishTitleChange(val title: String) : TasksEvent()
    data class PublishDescriptionChange(val description: String) : TasksEvent()
}

@Immutable
internal sealed interface PhotoUploadStatus {
    data object Failed : PhotoUploadStatus
    data object Success : PhotoUploadStatus
    data object Loading : PhotoUploadStatus
    data object Idle : PhotoUploadStatus
}

internal class TasksViewModel(
    private val photoTasksRepository: PhotoTasksRepository,
    private val photosRepository: PhotosRepository,
    private val userDataRepository: UserDataRepository,
    private val getPhotoTasksUseCase: GetPhotoTasksUseCase,
    private val applicationId: ApplicationId,
    private val errorMonitor: ErrorMonitor,
) : ViewModel() {

    private val _uiState = MutableStateFlow<TasksUiState>(TasksUiState(isLoading = true))

    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val moodDeferred = async { photoTasksRepository.getMood() }
                val seasonDeferred = async { photoTasksRepository.getSeason() }
                val topicDeferred = async { photoTasksRepository.getTopic() }

                val (categories, authState) = coroutineScope {
                    val categoriesResource = async {
                        TaskCategoryResource(
                            moods = moodDeferred.await(),
                            seasons = seasonDeferred.await(),
                            topics = topicDeferred.await()
                        )
                    }

                    val authStateFlow = async {
                        userDataRepository.userData
                            .map { it.authState }
                            .first()
                    }

                    Pair(categoriesResource.await(), authStateFlow.await())
                }

                _uiState.update { currentState ->
                    when (authState) {
                        is AuthState.Authenticated -> {
                            currentState.copy(
                                authState = authState,
                                isLoading = false,
                                categoriesForTaskGeneration = categories,
                                tasks = null
                            )
                        }

                        is AuthState.Guest, is AuthState.Unauthenticated -> {
                            currentState.copy(
                                authState = null,
                                isLoading = false,
                                categoriesForTaskGeneration = categories,
                                tasks = null
                            )
                        }
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                errorMonitor.tryEmit(e.message ?: UNKNOWN_ERROR)
            }
        }
    }

    fun onEvent(event: TasksEvent) {
        when (event) {
            is TasksEvent.GenerateTaskClicked -> onGenerateTaskClick()
            is TasksEvent.RefreshTaskClicked -> onRefreshTaskClick()
            is TasksEvent.PublishClicked -> onPublishClick()
            is TasksEvent.MoodClicked -> onMoodClick(title = event.title)
            is TasksEvent.SeasonClicked -> onSeasonClick(title = event.title)
            is TasksEvent.TopicClicked -> onTopicClick(title = event.title)
            is TasksEvent.ReceivePhotoTaskIntent -> onPhotoSelectReceive(intent = event.intent)
            is TasksEvent.PublishDescriptionChange -> onPublishDescriptionChange(publishDescription = event.description)
            is TasksEvent.PublishTitleChange -> onPublishTitleChange(publishTitle = event.title)
        }
    }

    private fun onGenerateTaskClick() {
        viewModelScope.launch {
            try {
                val tasks = getPhotoTasksUseCase(
                    mood = uiState.value.selectionMoodTitle(),
                    season = uiState.value.selectionSeasonTitle(),
                    topic = uiState.value.selectionTopicTitle()
                )
                _uiState.update { currentState ->
                    currentState.copy(
                        tasks = tasks.toPersistentList(),
                        currentTask = tasks.random(),
                        isExpandedTaskGeneratorPanel = false,
                        photoUploadStatus = PhotoUploadStatus.Idle
                    )
                }
            } catch (e: Exception) {
                errorMonitor.tryEmit(e.message ?: UNKNOWN_ERROR)
            }
        }
    }

    private fun onMoodClick(title: String) {
        if (title != uiState.value.selectionMoodTitle()) {
            _uiState.update { currentState ->
                currentState.copy(
                    onSelectionMood = currentState.categoriesForTaskGeneration!!.moods.indexOfFirst { it.title == title },
                    tasks = null
                )
            }
        }
    }

    private fun onSeasonClick(title: String) {
        if (title != uiState.value.selectionSeasonTitle()) {
            _uiState.update { currentState ->
                currentState.copy(
                    onSelectionSeason = currentState.categoriesForTaskGeneration!!.seasons.indexOfFirst { it.title == title },
                    tasks = null
                )
            }
        }
    }

    private fun onTopicClick(title: String) {
        if (title != uiState.value.selectionTopicTitle()) {
            _uiState.update { currentState ->
                currentState.copy(
                    onSelectionTopic = currentState.categoriesForTaskGeneration!!.topics.indexOfFirst { it.title == title },
                    tasks = null
                )
            }
        }
    }

    private fun onRefreshTaskClick() {
        _uiState.update { currentState ->
            currentState.copy(
                currentTask = currentState.tasks?.random(),
                selectedPicture = null,
                selectedPictureFileUrl = null,
                photoUploadStatus = PhotoUploadStatus.Idle
            )
        }
    }

    private fun onPublishClick() {
        uiState.value.authState?.let { authState ->
            uiState.value.selectedPictureFileUrl?.let { url ->
                viewModelScope.launch {
                    try {
                        _uiState.update { currentState ->
                            currentState.copy(
                                photoUploadStatus = PhotoUploadStatus.Loading,
                                isExpandedTaskGeneratorPanel = false
                            )
                        }
                        val photoId = photosRepository.uploadPhoto(
                            token = authState.user.token!!.accessToken,
                            tokenSecret = authState.user.token!!.accessTokenSecret,
                            photoUrl = url,
                            title = uiState.value.publishTitle.ifBlank { "It was a ${uiState.value.selectionMoodTitle()}, ${uiState.value.selectionSeasonTitle()}, about ${uiState.value.selectionTopicTitle()}" },
                            description = uiState.value.publishDescription.ifBlank { uiState.value.currentTask!!.task }
                        )
                        if (photoId != null) {
                            _uiState.update { currentState ->
                                currentState.copy(
                                    currentTask = null,
                                    selectedPictureFileUrl = null,
                                    selectedPicture = null,
                                    photoUploadStatus = PhotoUploadStatus.Success,
                                )
                            }
                        }
                    } catch (e: Exception) {
                        sendError(error = "Failed to publish photo.\nPlease try again.")
                        _uiState.update { currentState ->
                            currentState.copy(
                                photoUploadStatus = PhotoUploadStatus.Failed
                            )
                        }
                    } finally {
                        delay(5000)
                        _uiState.update { currentState ->
                            currentState.copy(photoUploadStatus = PhotoUploadStatus.Idle)
                        }
                    }
                }
            }
        }
    }

    private fun onPhotoSelectReceive(intent: PhotoTaskIntent) = viewModelScope.launch {

        when (intent) {

            is PhotoTaskIntent.OnPermissionGrantedWith -> {
                _uiState.update { currentState ->
                    currentState.copy(selectedPictureTempFileUrl = getTempFileUri(intent))
                }
            }

            is PhotoTaskIntent.OnPermissionDenied,
            -> sendError(error = "User did not grant permission to use the camera")

            is PhotoTaskIntent.OnFinishPickingImageWith -> {
                if (intent.imageUrl != null) {
                    val selectedPicture = getSelectedPicture(photoTaskIntent = intent)
                    updateSelectedPicture(image = selectedPicture, selectedPictureFileUrl = intent.imageUrl)
                } else {
                    sendError(error = "No any picture was selected")
                }
            }

            is PhotoTaskIntent.OnImageSavedWith -> {
                _uiState.value.selectedPictureTempFileUrl?.let { uri ->
                    val source = ImageDecoder.createSource(intent.compositionContext.contentResolver, uri)
                    updateSelectedPicture(image = ImageDecoder.decodeBitmap(source), selectedPictureFileUrl = uri)
                }
            }

            is PhotoTaskIntent.OnImageSavingCanceled -> {
                _uiState.value = _uiState.value.copy(selectedPictureTempFileUrl = null)
            }
        }
    }

    private fun getTempFileUri(photoTaskIntent: PhotoTaskIntent.OnPermissionGrantedWith): Uri {
        val tempFile = File.createTempFile(
            "temp_image_file_", /* prefix */
            ".jpg", /* suffix */
            photoTaskIntent.compositionContext.cacheDir  /* cache directory */
        )

        val uri = FileProvider.getUriForFile(
            photoTaskIntent.compositionContext,
            "${applicationId.id}.provider",
            tempFile
        )
        return uri
    }

    private fun getSelectedPicture(photoTaskIntent: PhotoTaskIntent.OnFinishPickingImageWith): Bitmap? {
        return photoTaskIntent.imageUrl?.let {
            val selectedPicture = photoTaskIntent.imageUrl
            val inputStream = photoTaskIntent.compositionContext.contentResolver.openInputStream(selectedPicture)
            val bytes = inputStream?.readBytes()
            inputStream?.close()

            bytes?.let {
                val bitmapOptions = BitmapFactory.Options()
                bitmapOptions.inMutable = true
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size, bitmapOptions)
            }
        }
    }

    private fun updateSelectedPicture(image: Bitmap?, selectedPictureFileUrl: Uri) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedPicture = image?.asImageBitmap(),
                selectedPictureFileUrl = selectedPictureFileUrl,
                selectedPictureTempFileUrl = null
            )
        }
    }

    private fun sendError(error: String) {
        viewModelScope.launch {
            errorMonitor.tryEmit(error)
        }
    }

    fun onExpandTaskGeneratorPanel() {
        _uiState.update { currentState ->
            currentState.copy(
                isExpandedTaskGeneratorPanel = !currentState.isExpandedTaskGeneratorPanel
            )
        }
    }

    private fun onPublishTitleChange(publishTitle: String) {
        _uiState.update { currentState ->
            currentState.copy(
                publishTitle = publishTitle
            )
        }
    }

    private fun onPublishDescriptionChange(publishDescription: String) {
        _uiState.update { currentState ->
            currentState.copy(
                publishDescription = publishDescription
            )
        }
    }
}



