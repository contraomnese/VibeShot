package com.arbuzerxxl.vibeshot.data.storage.db.photo_tasks.dao

import androidx.room.Dao
import androidx.room.Query
import com.arbuzerxxl.vibeshot.data.storage.db.photo_tasks.entities.MoodEntity
import com.arbuzerxxl.vibeshot.data.storage.db.photo_tasks.entities.SeasonEntity
import com.arbuzerxxl.vibeshot.data.storage.db.photo_tasks.entities.TopicEntity

@Dao
interface TasksCategoryDao {

    @Query("SELECT id, title FROM mood")
    fun getMoods(): List<MoodEntity>

    @Query("SELECT id, title FROM season")
    fun getSeasons(): List<SeasonEntity>

    @Query("SELECT id, title FROM topic")
    fun getTopics(): List<TopicEntity>
}

