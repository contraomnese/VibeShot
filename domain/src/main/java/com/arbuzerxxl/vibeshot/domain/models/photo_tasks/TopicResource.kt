package com.arbuzerxxl.vibeshot.domain.models.photo_tasks

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf


data class TopicResource(
    val topics: ImmutableList<String>,
) {
    companion object {
        val EMPTY: TopicResource = TopicResource(topics = persistentListOf())
    }
}

