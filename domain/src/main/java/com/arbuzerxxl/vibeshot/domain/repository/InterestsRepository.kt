package com.arbuzerxxl.vibeshot.domain.repository

import androidx.paging.PagingData
import com.arbuzerxxl.vibeshot.domain.models.InterestsPhotoResource
import kotlinx.coroutines.flow.Flow

interface InterestsRepository {

    fun getPhotos(perPage: Int, index: Int?): Flow<PagingData<InterestsPhotoResource>>
}