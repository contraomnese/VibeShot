package com.arbuzerxxl.vibeshot.domain.repository

import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.MoodResource
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.SeasonResource
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.TaskResource
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.TopicResource

interface PhotoTasksRepository {

    suspend fun getTasks(mood: String, season: String, topic: String): List<TaskResource>
    suspend fun getMood(): List<MoodResource>
    suspend fun getSeason(): List<SeasonResource>
    suspend fun getTopic(): List<TopicResource>
}