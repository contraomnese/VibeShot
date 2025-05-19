package com.arbuzerxxl.vibeshot.domain.repository

import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.MoodResource
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.SeasonResource
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.TasksResource
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.TopicResource

interface PhotoTasksRepository {

    suspend fun getTasks(mood: String, season: String, topic: String): TasksResource
    suspend fun getMood(): MoodResource
    suspend fun getSeason(): SeasonResource
    suspend fun getTopic(): TopicResource
}