package com.arbuzerxxl.vibeshot.data.storage.db.photo_tasks.dao

import androidx.room.Dao
import androidx.room.Query
import com.arbuzerxxl.vibeshot.data.storage.db.photo_tasks.dto.PhotoTaskDto

@Dao
interface PhotoTasksDao {

    @Query("SELECT task as ${PhotoTaskDto.TASK}" +
            " FROM tasks" +
            " JOIN mood as m on m.id = tasks.mood_id" +
            " JOIN season as s on s.id = tasks.season_id" +
            " JOIN topic as t on t.id = tasks.topic_id" +
            " WHERE m.title LIKE :mood and s.title LIKE :season and t.title LIKE :topic ")
    fun getTasks(mood: String, season: String, topic: String): List<PhotoTaskDto>
}

