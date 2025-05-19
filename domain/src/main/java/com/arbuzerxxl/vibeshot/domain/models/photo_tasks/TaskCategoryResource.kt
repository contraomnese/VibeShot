package com.arbuzerxxl.vibeshot.domain.models.photo_tasks

data class TaskCategoryResource(
    val mood: MoodResource,
    val season: SeasonResource,
    val topic: TopicResource,
)