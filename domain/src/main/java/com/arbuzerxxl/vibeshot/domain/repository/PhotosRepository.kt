package com.arbuzerxxl.vibeshot.domain.repository

import android.net.Uri
import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoResource
import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoSizesResource

interface PhotosRepository {

    suspend fun getPhoto(photoId: String, photoUrl: String): PhotoResource

    suspend fun getSizes(photoId: String): PhotoSizesResource

    suspend fun uploadPhoto(token: String, tokenSecret: String, photoUrl: Uri, title: String, description: String): String?
}