package com.arbuzerxxl.vibeshot.domain.models.photo_tasks

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf


data class MoodResource(
    val moods: ImmutableList<String>,
) {
    companion object {
        val EMPTY: MoodResource = MoodResource(moods = persistentListOf())
    }
}

