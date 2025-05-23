package com.arbuzerxxl.vibeshot.domain.models.photo_tasks


data class TasksResource(
    val tasks: List<String>,
)

@JvmInline
value class TaskResource(val task: String)

