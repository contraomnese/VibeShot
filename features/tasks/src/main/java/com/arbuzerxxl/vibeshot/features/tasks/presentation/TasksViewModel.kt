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
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.TaskCategoryResource
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.TaskResource
import com.arbuzerxxl.vibeshot.domain.repository.PhotoTasksRepository
import com.arbuzerxxl.vibeshot.domain.usecases.photo_tasks.GetPhotoTasksUseCase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File


@Immutable
data class TasksUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val categories: TaskCategoryResource = TaskCategoryResource.EMPTY,
    val mood: String = "",
    val season: String = "",
    val topic: String = "",
    val tasks: ImmutableList<TaskResource>? = null,
    val task: TaskResource? = null,
    val tempFileUrl: Uri? = null,
    val selectedPictures: List<ImageBitmap> = emptyList(),
)

internal class TasksViewModel(
    private val photoTasksRepository: PhotoTasksRepository,
    private val getPhotoTasksUseCase: GetPhotoTasksUseCase,
    private val applicationId: ApplicationId
) : ViewModel() {

    private val _uiState = MutableStateFlow<TasksUiState>(TasksUiState(isLoading = true))

    val uiState: StateFlow<TasksUiState> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TasksUiState(isLoading = true)
    )

    init {
        try {
            _uiState.update { it.copy(isLoading = true) }

            viewModelScope.launch {
                val moodDeferred = async { photoTasksRepository.getMood() }
                val seasonDeferred = async { photoTasksRepository.getSeason() }
                val topicDeferred = async { photoTasksRepository.getTopic() }
                val categories = TaskCategoryResource(
                    mood = moodDeferred.await(),
                    season = seasonDeferred.await(),
                    topic = topicDeferred.await()
                )
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        categories = categories,
                        mood = categories.mood.moods.first(),
                        season = categories.season.seasons.first(),
                        topic = categories.topic.topics.first(),
                        tasks = null
                    )
                }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, error = e.message) }
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
            currentState.copy(task = currentState.tasks?.random())
        }
    }

    fun onReceive(intent: Intent) = viewModelScope.launch {
        when(intent) {
            is Intent.OnPermissionGrantedWith -> {
                val tempFile = File.createTempFile(
                    "temp_image_file_", /* prefix */
                    ".jpg", /* suffix */
                    intent.compositionContext.cacheDir  /* cache directory */
                )

                val uri = FileProvider.getUriForFile(intent.compositionContext,
                    "${applicationId.id}.provider",
                    tempFile
                )

                _uiState.update { currentState ->
                    currentState.copy(tempFileUrl = uri)
                }
            }

            is Intent.OnPermissionDenied -> {
                println("User did not grant permission to use the camera")
            }

            is Intent.OnFinishPickingImagesWith -> {
                if (intent.imageUrls.isNotEmpty()) {
                    val newImages = mutableListOf<ImageBitmap>()
                    for (eachImageUrl in intent.imageUrls) {
                        val inputStream = intent.compositionContext.contentResolver.openInputStream(eachImageUrl)
                        val bytes = inputStream?.readBytes()
                        inputStream?.close()

                        if (bytes != null) {
                            val bitmapOptions = BitmapFactory.Options()
                            bitmapOptions.inMutable = true
                            val bitmap: Bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, bitmapOptions)
                            newImages.add(bitmap.asImageBitmap())
                        } else {
                            println("The image that was picked could not be read from the device at this url: $eachImageUrl")
                        }
                    }

                    _uiState.update { currentState ->
                        currentState.copy(
                            selectedPictures = (currentState.selectedPictures + newImages),
                            tempFileUrl = null
                        )
                    }
                } else {
                    // user did not pick anything
                }
            }

            is Intent.OnImageSavedWith -> {
                val tempImageUrl = _uiState.value.tempFileUrl
                if (tempImageUrl != null) {
                    val source = ImageDecoder.createSource(intent.compositionContext.contentResolver, tempImageUrl)

                    val currentPictures = _uiState.value.selectedPictures.toMutableList()
                    currentPictures.add(ImageDecoder.decodeBitmap(source).asImageBitmap())

                    _uiState.value = _uiState.value.copy(
                        tempFileUrl = null,
                        selectedPictures = currentPictures)
                }
            }

            is Intent.OnImageSavingCanceled -> {
                _uiState.value = _uiState.value.copy(tempFileUrl = null)
            }
        }
    }
}

sealed class Intent {
    data class OnPermissionGrantedWith(val compositionContext: Context): Intent()
    data object OnPermissionDenied: Intent()
    data class OnImageSavedWith (val compositionContext: Context): Intent()
    data object OnImageSavingCanceled: Intent()
    data class OnFinishPickingImagesWith(val compositionContext: Context, val imageUrls: List<Uri>): Intent()
}



