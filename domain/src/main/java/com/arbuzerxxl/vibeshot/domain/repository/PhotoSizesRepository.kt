package com.arbuzerxxl.vibeshot.domain.repository

import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoSizesResource

interface PhotoSizesRepository {

    suspend fun getSizes(photoId: String): PhotoSizesResource

}