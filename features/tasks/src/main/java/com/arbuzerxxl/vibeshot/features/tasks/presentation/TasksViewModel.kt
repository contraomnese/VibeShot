package com.arbuzerxxl.vibeshot.features.tasks.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.TaskCategoryResource
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.TasksResource
import com.arbuzerxxl.vibeshot.domain.repository.PhotoTasksRepository
import com.arbuzerxxl.vibeshot.domain.usecases.photo_tasks.GetPhotoTasksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal sealed interface TasksUiState {

    data object Loading : TasksUiState

    @Immutable
    data class Success(
        val categories: TaskCategoryResource,
        val mood: String,
        val season: String,
        val topic: String,
        val task: TasksResource?,
    ) : TasksUiState
}

internal class TasksViewModel(
    private val photoTasksRepository: PhotoTasksRepository,
    private val getPhotoTasksUseCase: GetPhotoTasksUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<TasksUiState>(TasksUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                val categories = TaskCategoryResource(
                    mood = photoTasksRepository.getMood(),
                    season = photoTasksRepository.getSeason(),
                    topic = photoTasksRepository.getTopic()
                )
                TasksUiState.Success(
                    categories = categories,
                    mood = categories.mood.moods.first(),
                    season = categories.season.seasons.first(),
                    topic = categories.topic.topics.first(),
                    task = null
                )
            }
        }
    }

    fun onGenerateTaskClick() {
        viewModelScope.launch {
            _uiState.update {
                (it as TasksUiState.Success).copy(
                    task = getPhotoTasksUseCase(
                        mood = it.mood,
                        season = it.season,
                        topic = it.topic
                    )
                )
            }
        }
    }

    fun onMoodClick(title: String) {
        _uiState.update {
            (it as TasksUiState.Success).copy(mood = title)
        }
    }

    fun onSeasonClick(title: String) {
        _uiState.update {
            (it as TasksUiState.Success).copy(season = title)
        }
    }

    fun onTopicClick(title: String) {
        _uiState.update {
            (it as TasksUiState.Success).copy(topic = title)
        }
    }

}



