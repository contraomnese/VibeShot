package com.arbuzerxxl.vibeshot.data.storage.db.photo.details.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arbuzerxxl.vibeshot.data.network.model.photos.ExifData
import com.arbuzerxxl.vibeshot.data.network.model.photos.Tag

@Entity(tableName = DetailsPhotoEntity.TABLE_NAME)
data class DetailsPhotoEntity(
    @PrimaryKey
    @ColumnInfo(name = PHOTO_ID) val photoId: String,

    @ColumnInfo(name = PHOTO_URL) val photoUrl: String,
    @ColumnInfo(name = SECRET) val secret: String,
    @ColumnInfo(name = SERVER) val server: String,
    @ColumnInfo(name = FARM) val farm: Int,
    @ColumnInfo(name = DATE_UPLOADED) val dateUploaded: String,
    @ColumnInfo(name = IS_FAVORITE) val isFavorite: Int,
    @ColumnInfo(name = LICENSE) val license: String,
    @ColumnInfo(name = SAFETY_LEVEL) val safetyLevel: String,
    @ColumnInfo(name = VIEWS) val views: String,

    @ColumnInfo(name = OWNER_NSID) val ownerNsid: String,
    @ColumnInfo(name = OWNER_USERNAME) val ownerUsername: String,
    @ColumnInfo(name = OWNER_REALNAME) val ownerRealName: String,
    @ColumnInfo(name = OWNER_LOCATION) val ownerLocation: String?,
    @ColumnInfo(name = OWNER_ICON_SERVER) val ownerIconServer: String,
    @ColumnInfo(name = OWNER_ICON_FARM) val ownerIconFarm: Int,
    @ColumnInfo(name = OWNER_PATH_ALIAS) val ownerPathAlias: String?,

    @ColumnInfo(name = TITLE) val title: String,
    @ColumnInfo(name = DESCRIPTION) val description: String,
    @ColumnInfo(name = COMMENTS) val comments: String,

    @ColumnInfo(name = DATE_POSTED) val datePosted: String,
    @ColumnInfo(name = DATE_TAKEN) val dateTaken: String,
    @ColumnInfo(name = DATE_LAST_UPDATE) val dateLastUpdate: String,

    @ColumnInfo(name = TAGS_JSON) val tagsJson: List<Tag>,

    @ColumnInfo(name = CAMERA) val camera: String? = null,
    @ColumnInfo(name = EXIF_JSON) val exifJson: List<ExifData>? = null,

    @ColumnInfo(name = LAST_UPDATED) val lastUpdated: Long = System.currentTimeMillis()
) {
    companion object {
        const val TABLE_NAME = "photo_details"

        const val PHOTO_ID = "photo_id"
        const val PHOTO_URL = "photo_url"
        const val SECRET = "secret"
        const val SERVER = "server"
        const val FARM = "farm"
        const val DATE_UPLOADED = "date_uploaded"
        const val IS_FAVORITE = "is_favorite"
        const val LICENSE = "license"
        const val SAFETY_LEVEL = "safety_level"
        const val VIEWS = "views"

        const val OWNER_NSID = "owner_nsid"
        const val OWNER_USERNAME = "owner_username"
        const val OWNER_REALNAME = "owner_realname"
        const val OWNER_LOCATION = "owner_location"
        const val OWNER_ICON_SERVER = "owner_icon_server"
        const val OWNER_ICON_FARM = "owner_icon_farm"
        const val OWNER_PATH_ALIAS = "owner_path_alias"

        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val COMMENTS = "comments"

        const val DATE_POSTED = "date_posted"
        const val DATE_TAKEN = "date_taken"
        const val DATE_LAST_UPDATE = "date_last_update"

        const val TAGS_JSON = "tags_json"
        const val CAMERA = "camera"
        const val EXIF_JSON = "exif_json"

        const val SIZES_JSON = "sizes_json"

        const val LAST_UPDATED = "last_updated"
    }
}