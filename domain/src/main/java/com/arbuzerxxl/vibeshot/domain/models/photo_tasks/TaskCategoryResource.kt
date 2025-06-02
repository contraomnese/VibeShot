package com.arbuzerxxl.vibeshot.domain.models.photo_tasks

data class TaskCategoryResource(
    val moods: List<MoodResource>,
    val seasons: List<SeasonResource>,
    val topics: List<TopicResource>,
)