package com.arbuzerxxl.vibeshot.domain.repository

import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoResource
import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoSizesResource

interface PhotosRepository {

    suspend fun getPhoto(photoId: String): PhotoResource

    suspend fun getSizes(photoId: String): PhotoSizesResource

}