package com.arbuzerxxl.vibeshot.features.tasks.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal sealed interface TasksUiState {

    data object Loading : TasksUiState

    @Immutable
    data class Success(val categories: List<TaskCategory>) : TasksUiState
}

internal class TasksViewModel(
) : ViewModel() {

    private val _uiState = MutableStateFlow<TasksUiState>(TasksUiState.Success(categories = listOf<TaskCategory>(
        TaskCategory(
            title = "Moods",
            items = listOf(
                "joyful", "sad", "calm", "irritated", "anxious", "angry", "inspired", "tired", "romantic", "neutral"
            )
        ),
        TaskCategory(
            title = "Seasons",
            items = listOf("winter", "spring", "summer", "autumn")
        ),
        TaskCategory(
            title = "Topics",
            items = listOf(
                "city", "nature", "interior", "food", "transport", "holiday", "work", "sport", "art", "macro"
            )
        ),
    )))
    val uiState = _uiState.asStateFlow()

}

data class TaskCategory(
    val title: String,
    val items: List<String>
)