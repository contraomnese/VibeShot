package com.arbuzerxxl.vibeshot.domain.models.photo_tasks

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf


data class SeasonResource(
    val seasons: ImmutableList<String>,
) {
    companion object {
        val EMPTY: SeasonResource = SeasonResource(seasons = persistentListOf())
    }
}

