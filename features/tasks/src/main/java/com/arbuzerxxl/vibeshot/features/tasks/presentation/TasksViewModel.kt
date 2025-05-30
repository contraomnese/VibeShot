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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

internal sealed class PhotoTaskIntent {
    data class OnPermissionGrantedWith(val compositionContext: Context) : PhotoTaskIntent()
    data object OnPermissionDenied : PhotoTaskIntent()
    data class OnImageSavedWith(val compositionContext: Context) : PhotoTaskIntent()
    data object OnImageSavingCanceled : PhotoTaskIntent()
    data class OnFinishPickingImageWith(val compositionContext: Context, val imageUrl: Uri?) : PhotoTaskIntent()
}

@Immutable
data class TasksUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val notification: String? = null,
    val categories: TaskCategoryResource = TaskCategoryResource.EMPTY,
    val mood: String = "",
    val season: String = "",
    val topic: String = "",
    val tasks: ImmutableList<TaskResource>? = null,
    val task: TaskResource? = null,
    val tempFileUrl: Uri? = null,
    val selectedPictureFileUrl: Uri? = null,
    val selectedPicture: ImageBitmap? = null,
    val authState: AuthState.Authenticated? = null,
    val photoId: String? = null,
)

internal class TasksViewModel(
    private val photoTasksRepository: PhotoTasksRepository,
    private val photosRepository: PhotosRepository,
    private val userDataRepository: UserDataRepository,
    private val getPhotoTasksUseCase: GetPhotoTasksUseCase,
    private val applicationId: ApplicationId,
) : ViewModel() {

    private val _uiState = MutableStateFlow<TasksUiState>(TasksUiState(isLoading = true))

    val uiState: StateFlow<TasksUiState> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TasksUiState(isLoading = true)
    )

    init {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val moodDeferred = async { photoTasksRepository.getMood() }
                val seasonDeferred = async { photoTasksRepository.getSeason() }
                val topicDeferred = async { photoTasksRepository.getTopic() }

                val (categories, authState) = coroutineScope {
                    val categoriesResource = async {
                        TaskCategoryResource(
                            mood = moodDeferred.await(),
                            season = seasonDeferred.await(),
                            topic = topicDeferred.await()
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
                                categories = categories,
                                mood = categories.mood.moods.first(),
                                season = categories.season.seasons.first(),
                                topic = categories.topic.topics.first(),
                                tasks = null
                            )
                        }

                        is AuthState.Guest, is AuthState.Unauthenticated -> {
                            currentState.copy(
                                authState = null,
                                isLoading = false,
                                categories = categories,
                                mood = categories.mood.moods.first(),
                                season = categories.season.seasons.first(),
                                topic = categories.topic.topics.first(),
                                tasks = null
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun onGenerateTaskClick() {
        viewModelScope.launch {
            val tasks = getPhotoTasksUseCase(
                mood = uiState.value.mood,
                season = uiState.value.season,
                topic = uiState.value.topic
            )
            _uiState.update { currentState ->
                currentState.copy(
                    tasks = tasks.toPersistentList(),
                    task = tasks.random()
                )
            }
        }
    }

    fun onMoodClick(title: String) {
        if (title != uiState.value.mood) {
            _uiState.update { currentState ->
                currentState.copy(mood = title, tasks = null)
            }
        }
    }

    fun onSeasonClick(title: String) {
        if (title != uiState.value.season) {
            _uiState.update { currentState ->
                currentState.copy(season = title, tasks = null)
            }
        }
    }

    fun onTopicClick(title: String) {
        if (title != uiState.value.topic) {
            _uiState.update { currentState ->
                currentState.copy(topic = title, tasks = null)
            }
        }
    }

    fun onRefreshTaskClick() {
        _uiState.update { currentState ->
            currentState.copy(
                task = currentState.tasks?.random(),
                selectedPicture = null,
                selectedPictureFileUrl = null
            )
        }
    }

    fun onPublishClick() {
        uiState.value.authState?.let { authState ->
            uiState.value.selectedPictureFileUrl?.let { url ->
                viewModelScope.launch {
                    try {
                        val photoId = photosRepository.uploadPhoto(
                            token = authState.user.token!!.accessToken,
                            tokenSecret = authState.user.token!!.accessTokenSecret,
                            photoUrl = url,
                            title = uiState.value.task?.task ?: throw IllegalStateException("Can't upload image without title!")
                        )
                        _uiState.update { currentState ->
                            currentState.copy(
                                photoId = photoId,
                                task = null,
                                selectedPictureFileUrl = null,
                                selectedPicture = null
                            )
                        }
                    }
                    catch (e: Exception) {
                        _uiState.update { it.copy(error = e.message) }
                    }
                }
            }
        }
    }

    fun onReceive(photoTaskIntent: PhotoTaskIntent) = viewModelScope.launch {
        when (photoTaskIntent) {
            is PhotoTaskIntent.OnPermissionGrantedWith -> {
                _uiState.update { currentState ->
                    currentState.copy(tempFileUrl = getTempFileUri(photoTaskIntent))
                }
            }

            is PhotoTaskIntent.OnPermissionDenied,
            -> updateNotification(notification = "User did not grant permission to use the camera")

            is PhotoTaskIntent.OnFinishPickingImageWith -> {
                if (photoTaskIntent.imageUrl != null) {
                    val selectedPicture = getSelectedPicture(photoTaskIntent = photoTaskIntent)
                    updateSelectedPicture(image = selectedPicture, selectedPictureFileUrl = photoTaskIntent.imageUrl)
                } else {
                    updateNotification(notification = "No any picture was selected")
                }
            }

            is PhotoTaskIntent.OnImageSavedWith -> {
                _uiState.value.tempFileUrl?.let { uri ->
                    val source = ImageDecoder.createSource(photoTaskIntent.compositionContext.contentResolver, uri)
                    updateSelectedPicture(image = ImageDecoder.decodeBitmap(source), selectedPictureFileUrl = uri)
                }
            }

            is PhotoTaskIntent.OnImageSavingCanceled -> {
                _uiState.value = _uiState.value.copy(tempFileUrl = null)
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

    private fun updateNotification(notification: String) {
        _uiState.update { currentState ->
            currentState.copy(notification = notification)
        }
    }

    private fun updateSelectedPicture(image: Bitmap?, selectedPictureFileUrl: Uri) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedPicture = image?.asImageBitmap(),
                selectedPictureFileUrl = selectedPictureFileUrl,
                tempFileUrl = null,
            )
        }
    }

    fun clearError() {
        _uiState.update { currentState ->
            currentState.copy(
                error = null
            )
        }
    }

    fun clearNotification() {
        _uiState.update { currentState ->
            currentState.copy(
                notification = null
            )
        }
    }
}



