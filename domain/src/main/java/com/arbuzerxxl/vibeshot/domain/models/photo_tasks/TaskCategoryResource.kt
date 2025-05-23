package com.arbuzerxxl.vibeshot.domain.models.photo_tasks

data class TaskCategoryResource(
    val mood: MoodResource,
    val season: SeasonResource,
    val topic: TopicResource,
) {
    companion object {
        val EMPTY: TaskCategoryResource =
            TaskCategoryResource(mood = MoodResource.EMPTY, season = SeasonResource.EMPTY, topic = TopicResource.EMPTY)
    }
}