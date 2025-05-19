package com.arbuzerxxl.vibeshot.data.storage.db.photo.interests.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arbuzerxxl.vibeshot.data.storage.db.photo.interests.dto.InterestsPhotoDto
import com.arbuzerxxl.vibeshot.data.storage.db.photo.interests.entities.InterestsEntity

@Dao
interface InterestsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(photos: List<InterestsEntity>)

    @Query("SELECT photo_id as ${InterestsPhotoDto.Companion.PHOTO_ID}," +
            " title as ${InterestsPhotoDto.Companion.TITLE}," +
            " high_quality_url as ${InterestsPhotoDto.Companion.HIGH_QUALITY_URL}," +
            " low_quality_url as ${InterestsPhotoDto.Companion.LOW_QUALITY_URL}," +
            " width as ${InterestsPhotoDto.Companion.WIDTH}," +
            " height as ${InterestsPhotoDto.Companion.HEIGHT}," +
            " page as ${InterestsPhotoDto.Companion.PAGE}" +
            " FROM interests")
    fun getAll(): PagingSource<Int, InterestsPhotoDto>

    @Query("SELECT MAX(last_updated) FROM interests")
    suspend fun getLastUpdateTime(): Long?

    @Query("DELETE FROM interests")
    suspend fun clearAll()
}