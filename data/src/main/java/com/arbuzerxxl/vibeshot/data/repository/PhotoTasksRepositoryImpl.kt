package com.arbuzerxxl.vibeshot.data.repository

import com.arbuzerxxl.vibeshot.data.exceptions.RequestMoodInitializeException
import com.arbuzerxxl.vibeshot.data.exceptions.RequestPhotoTaskFetchException
import com.arbuzerxxl.vibeshot.data.exceptions.RequestSeasonInitializeException
import com.arbuzerxxl.vibeshot.data.exceptions.RequestTopicInitializeException
import com.arbuzerxxl.vibeshot.data.storage.db.photo_tasks.PhotoTasksDatabase
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.MoodResource
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.SeasonResource
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.TaskResource
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.TopicResource
import com.arbuzerxxl.vibeshot.domain.repository.PhotoTasksRepository
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class PhotoTasksRepositoryImpl(
    private val dispatcher: CoroutineDispatcher,
    private val database: PhotoTasksDatabase,
) : PhotoTasksRepository {

    override suspend fun getTasks(
        mood: String, season: String, topic: String,
    ): List<TaskResource> = withContext(dispatcher) {
        try {
            val tasks = database.tasksDao().getTasks(mood = mood, season = season, topic = topic)
            tasks.map { TaskResource(it.task) }
        } catch (cause: Throwable) {
            throw RequestPhotoTaskFetchException(cause)
        }
    }

    override suspend fun getMood(): List<MoodResource> = withContext(dispatcher) {
        try {
            val moods = database.tasksCategoryDao().getMoods()
            moods.map { MoodResource(it.title) }.toPersistentList()
        } catch (cause: Throwable) {
            throw RequestMoodInitializeException(cause)
        }
    }


    override suspend fun getSeason(): List<SeasonResource> = withContext(dispatcher) {
        try {
            val seasons = database.tasksCategoryDao().getSeasons()
            seasons.map { SeasonResource(it.title) }.toPersistentList()
        } catch (cause: Throwable) {
            throw RequestSeasonInitializeException(cause)
        }

    }

    override suspend fun getTopic(): List<TopicResource> = withContext(dispatcher) {
        try {
            val topics = database.tasksCategoryDao().getTopics()
            topics.map { TopicResource(it.title) }.toPersistentList()
        } catch (cause: Throwable) {
            throw RequestTopicInitializeException(cause)
        }
    }
}