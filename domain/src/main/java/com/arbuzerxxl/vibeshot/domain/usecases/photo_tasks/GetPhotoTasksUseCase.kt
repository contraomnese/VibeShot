package com.arbuzerxxl.vibeshot.domain.usecases.photo_tasks

import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.TasksResource
import com.arbuzerxxl.vibeshot.domain.repository.PhotoTasksRepository

class GetPhotoTasksUseCase(
    private val repository: PhotoTasksRepository,
) {
    suspend operator fun invoke(
        mood: String,
        season: String,
        topic: String,
    ): TasksResource = repository.getTasks(mood = mood, season = season, topic = topic)
}