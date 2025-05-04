package com.arbuzerxxl.vibeshot.domain.repository

import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoResource

interface PhotosRepository {

    suspend fun getPhoto(photoId: String): PhotoResource

}