package com.arbuzerxxl.vibeshot.data.storage.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arbuzerxxl.vibeshot.data.storage.dto.interests.InterestsPhotoDto
import com.arbuzerxxl.vibeshot.data.storage.entities.InterestsEntity

@Dao
interface InterestsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(photos: List<InterestsEntity>)

    @Query("SELECT photo_id as ${InterestsPhotoDto.PHOTO_ID}," +
            " title as ${InterestsPhotoDto.TITLE}," +
            " original_size_url as ${InterestsPhotoDto.ORIGINAL_SIZE_URL}," +
            " high_quality_size_url as ${InterestsPhotoDto.HIGH_QUALITY_SIZE_URL}," +
            " low_quality_size_url as ${InterestsPhotoDto.LOW_QUALITY_SIZE_URL}," +
            " width as ${InterestsPhotoDto.WIDTH}," +
            " height as ${InterestsPhotoDto.HEIGHT}," +
            " page as ${InterestsPhotoDto.PAGE}" +
            " FROM interests")
    fun pagingSource(): PagingSource<Int, InterestsPhotoDto>


    @Query("SELECT MAX(last_updated) FROM interests")
    suspend fun getLastUpdateTime(): Long?

    @Query("DELETE FROM interests")
    suspend fun clearAll()
}