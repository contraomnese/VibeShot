package com.arbuzerxxl.vibeshot.data.storage.db.photo.details.dto

import androidx.room.ColumnInfo
import com.arbuzerxxl.vibeshot.data.network.model.response.photos.ExifData
import com.arbuzerxxl.vibeshot.data.network.model.response.photos.Tag

data class DetailsPhotoDto(
    @ColumnInfo(name = PHOTO_ID) val photoId: String,
    @ColumnInfo(name = PHOTO_URL) val photoUrl: String,

    // Основная информация о фото
    @ColumnInfo(name = TITLE) val title: String,
    @ColumnInfo(name = DESCRIPTION) val description: String,
    @ColumnInfo(name = COMMENTS) val comments: String,
    @ColumnInfo(name = VIEWS) val views: String,
    @ColumnInfo(name = LICENSE) val license: String,

    // Информация о владельце
    @ColumnInfo(name = OWNER_NSID) val ownerNsid: String,
    @ColumnInfo(name = OWNER_REALNAME) val ownerRealName: String,
    @ColumnInfo(name = OWNER_USERNAME) val ownerUserName: String,
    @ColumnInfo(name = OWNER_ICON_SERVER) val ownerIconServer: String,
    @ColumnInfo(name = OWNER_ICON_FARM) val ownerIconFarm: Int,

    // Даты
    @ColumnInfo(name = DATE_TAKEN) val dateTaken: String,
    @ColumnInfo(name = DATE_UPLOADED) val dateUploaded: String,
    // Теги
    @ColumnInfo(name = TAGS_JSON) val tagsJson: List<Tag>,

    // EXIF данные
    @ColumnInfo(name = CAMERA) val camera: String?,
    @ColumnInfo(name = EXIF_JSON) val exifJson: List<ExifData>?,

    ) {
    companion object {

        const val PHOTO_ID = "alias_details_photo_id"
        const val PHOTO_URL = "alias_details_photo_url"
        const val LICENSE = "alias_details_license"
        const val VIEWS = "alias_details_views"
        const val OWNER_NSID = "alias_details_owner_nsid"
        const val OWNER_REALNAME = "alias_details_owner_realname"
        const val OWNER_USERNAME = "alias_details_owner_username"
        const val OWNER_ICON_SERVER = "alias_details_owner_icon_server"
        const val OWNER_ICON_FARM = "alias_details_owner_icon_farm"
        const val TITLE = "alias_details_title"
        const val DESCRIPTION = "alias_details_description"
        const val COMMENTS = "alias_details_comments"
        const val DATE_TAKEN = "alias_details_date_taken"
        const val DATE_UPLOADED = "alias_details_date_uploaded"
        const val TAGS_JSON = "alias_details_tags_json"
        const val CAMERA = "alias_details_camera"
        const val EXIF_JSON = "alias_details_exif_json"
    }
}