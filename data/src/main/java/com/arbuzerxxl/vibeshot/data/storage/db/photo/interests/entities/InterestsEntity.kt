package com.arbuzerxxl.vibeshot.data.storage.db.photo.interests.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = InterestsEntity.TABLE_NAME)
data class InterestsEntity(
    @PrimaryKey
    @ColumnInfo(name = PHOTO_ID) val photoId: String,
    @ColumnInfo(name = TITLE) val title: String,
    @ColumnInfo(name = HIGH_QUALITY_URL) val highQualityUrl: String,
    @ColumnInfo(name = LOW_QUALITY_URL) val lowQualityUrl: String,
    @ColumnInfo(name = WIDTH) val width: Int,
    @ColumnInfo(name = HEIGHT) val height: Int,
    @ColumnInfo(name = PAGE) val page: Int,
    @ColumnInfo(name = LAST_UPDATED) val lastUpdated: Long = System.currentTimeMillis()
) {
    companion object {
        const val TABLE_NAME = "interests"
        const val PHOTO_ID = "photo_id"
        const val TITLE = "title"
        const val HIGH_QUALITY_URL = "high_quality_url"
        const val LOW_QUALITY_URL = "low_quality_url"
        const val WIDTH = "width"
        const val HEIGHT = "height"
        const val PAGE = "page"
        const val LAST_UPDATED = "last_updated"
    }
}