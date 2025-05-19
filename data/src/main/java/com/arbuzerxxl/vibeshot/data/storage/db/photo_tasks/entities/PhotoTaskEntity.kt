package com.arbuzerxxl.vibeshot.data.storage.db.photo_tasks.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = PhotoTaskEntity.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = MoodEntity::class,
            parentColumns = ["id"],
            childColumns = ["mood_id"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        ),
        ForeignKey(
            entity = SeasonEntity::class,
            parentColumns = ["id"],
            childColumns = ["season_id"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        ),
        ForeignKey(
            entity = TopicEntity::class,
            parentColumns = ["id"],
            childColumns = ["topic_id"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ],
    indices = [
        Index(value = ["mood_id"]),
        Index(value = ["season_id"]),
        Index(value = ["topic_id"])
    ])
data class PhotoTaskEntity(

    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = MOOD_ID) val moodId: Int,
    @ColumnInfo(name = SEASON_ID) val seasonId: Int,
    @ColumnInfo(name = TOPIC_ID) val topicId: Int,
    @ColumnInfo(name = TASK) val task: String,

    ) {
    companion object {
        const val TABLE_NAME = "tasks"
        const val ID = "id"
        const val MOOD_ID = "mood_id"
        const val SEASON_ID = "season_id"
        const val TOPIC_ID = "topic_id"
        const val TASK = "task"

    }
}