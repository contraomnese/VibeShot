package com.arbuzerxxl.vibeshot.domain.repository

import com.arbuzerxxl.vibeshot.domain.models.InterestsResources

fun interface InterestsRepository {

    suspend fun getPhotos(page: Int): InterestsResources
}