package com.arbuzerxxl.vibeshot.domain.repository

import com.arbuzerxxl.vibeshot.domain.models.InterestsResources
import kotlinx.coroutines.flow.Flow

fun interface InterestsRepository {

    suspend fun getPhotos(page: Int): Flow<InterestsResources>
}