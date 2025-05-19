package com.arbuzerxxl.vibeshot.data.repository

import com.arbuzerxxl.vibeshot.data.storage.db.photo_tasks.PhotoTasksDatabase
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.MoodResource
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.SeasonResource
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.TasksResource
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.TopicResource
import com.arbuzerxxl.vibeshot.domain.repository.PhotoTasksRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class PhotoTasksRepositoryImpl(
    private val dispatcher: CoroutineDispatcher,
    private val database: PhotoTasksDatabase
): PhotoTasksRepository {

    override suspend fun getTasks(
        mood: String,
        season: String,
        topic: String,
    ): TasksResource = withContext(dispatcher) {
        val tasks = database.tasksDao().getTasks(mood = mood, season = season, topic = topic)
        TasksResource(tasks = tasks.map { it.task })
    }

    override suspend fun getMood(): MoodResource = withContext(dispatcher) {
        val moods = database.tasksCategoryDao().getMoods()
        MoodResource(moods = moods.map { it.title }) }

    override suspend fun getSeason(): SeasonResource = withContext(dispatcher) {
        val seasons = database.tasksCategoryDao().getSeasons()
        SeasonResource(seasons = seasons.map { it.title })
    }

    override suspend fun getTopic(): TopicResource = withContext(dispatcher) {
        val topics = database.tasksCategoryDao().getTopics()
        TopicResource(topics = topics.map { it.title })
    }
}