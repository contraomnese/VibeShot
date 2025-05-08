package com.arbuzerxxl.vibeshot.data.storage.db.details.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arbuzerxxl.vibeshot.data.storage.db.details.dto.DetailsPhotoDto
import com.arbuzerxxl.vibeshot.data.storage.db.details.entities.DetailsPhotoEntity

@Dao
interface DetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: DetailsPhotoEntity)

    @Query("SELECT photo_id as ${DetailsPhotoDto.PHOTO_ID}," +
            " title as ${DetailsPhotoDto.TITLE}," +
            " description as ${DetailsPhotoDto.DESCRIPTION}," +
            " comments as ${DetailsPhotoDto.COMMENTS}," +
            " views as ${DetailsPhotoDto.VIEWS}," +
            " license as ${DetailsPhotoDto.LICENSE}," +
            " owner_nsid as ${DetailsPhotoDto.OWNER_NSID}," +
            " owner_realname as ${DetailsPhotoDto.OWNER_REALNAME}," +
            " owner_icon_server as ${DetailsPhotoDto.OWNER_ICON_SERVER}," +
            " owner_icon_farm as ${DetailsPhotoDto.OWNER_ICON_FARM}," +
            " date_taken as ${DetailsPhotoDto.DATE_TAKEN}," +
            " date_uploaded as ${DetailsPhotoDto.DATE_UPLOADED}," +
            " tags_json as ${DetailsPhotoDto.TAGS_JSON}," +
            " camera as ${DetailsPhotoDto.CAMERA}," +
            " exif_json as ${DetailsPhotoDto.EXIF_JSON}," +
            " sizes_json as ${DetailsPhotoDto.SIZES_JSON}" +
            " FROM photo_details" +
            " WHERE photo_id LIKE :photoId")
    fun getPhoto(photoId: String): DetailsPhotoDto?

    @Query("SELECT MAX(last_updated) FROM photo_details")
    suspend fun getLastUpdateTime(): Long?

    @Query("DELETE FROM photo_details")
    suspend fun clearAll()
}