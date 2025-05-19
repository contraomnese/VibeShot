package com.arbuzerxxl.vibeshot.data.storage.db.photo.interests.dto

import androidx.room.ColumnInfo


data class InterestsPhotoDto(
    @ColumnInfo(name = PHOTO_ID) val photoId: String,
    @ColumnInfo(name = TITLE) val title: String,
    @ColumnInfo(name = HIGH_QUALITY_URL) val highQualityUrl: String,
    @ColumnInfo(name = LOW_QUALITY_URL) val lowQualityUrl: String,
    @ColumnInfo(name = WIDTH) val width: Int,
    @ColumnInfo(name = HEIGHT) val height: Int,
    @ColumnInfo(name = PAGE) val page: Int,
) {
    companion object {
        const val PHOTO_ID = "alias_interests_photo_id"
        const val TITLE = "alias_interests_title"
        const val HIGH_QUALITY_URL = "alias_interests_high_quality_url"
        const val LOW_QUALITY_URL = "alias_interests_low_quality_url"
        const val WIDTH = "alias_interests_width"
        const val HEIGHT = "alias_interests_height"
        const val PAGE = "alias_interests_page"
    }
}