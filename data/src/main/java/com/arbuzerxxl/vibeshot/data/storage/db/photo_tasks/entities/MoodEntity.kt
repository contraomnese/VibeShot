package com.arbuzerxxl.vibeshot.data.storage.db.photo_tasks.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = MoodEntity.TABLE_NAME)
data class MoodEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = TITLE) val title: String,
) {
    companion object {
        const val TABLE_NAME = "mood"
        const val ID = "id"
        const val TITLE = "title"
    }
}