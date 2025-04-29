package com.arbuzerxxl.vibeshot.data.storage.db.interests.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arbuzerxxl.vibeshot.data.storage.db.interests.dto.InterestsPhotoDto
import com.arbuzerxxl.vibeshot.data.storage.db.interests.entities.InterestsEntity

@Dao
interface InterestsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(photos: List<InterestsEntity>)

    @Query("SELECT photo_id as ${InterestsPhotoDto.PHOTO_ID}," +
            " title as ${InterestsPhotoDto.TITLE}," +
            " high_quality_size_url as ${InterestsPhotoDto.HIGH_QUALITY_SIZE_URL}," +
            " low_quality_size_url as ${InterestsPhotoDto.LOW_QUALITY_SIZE_URL}," +
            " width as ${InterestsPhotoDto.WIDTH}," +
            " height as ${InterestsPhotoDto.HEIGHT}," +
            " page as ${InterestsPhotoDto.PAGE}" +
            " FROM interests")
    fun getAll(): PagingSource<Int, InterestsPhotoDto>

    @Query("SELECT MAX(last_updated) FROM interests")
    suspend fun getLastUpdateTime(): Long?

    @Query("DELETE FROM interests")
    suspend fun clearAll()
}