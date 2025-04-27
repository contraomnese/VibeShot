package com.arbuzerxxl.vibeshot.data.storage.dto.interests

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity
data class InterestsPhotoDetailsDto(
    @ColumnInfo(name = PHOTO_ID) val photoId: String,
    @ColumnInfo(name = TITLE) val title: String,
    @ColumnInfo(name = URL) val url: String,
) {
    companion object {
        const val PHOTO_ID = "alias_interests_photo_id"
        const val TITLE = "alias_interests_title"
        const val URL = "alias_interests_high_quality_size_url"
    }
}