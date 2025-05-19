package com.arbuzerxxl.vibeshot.data.storage.db.photo_tasks.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = TopicEntity.TABLE_NAME)
data class TopicEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = TITLE) val title: String,
) {
    companion object {
        const val TABLE_NAME = "topic"
        const val ID = "id"
        const val TITLE = "title"
    }
}