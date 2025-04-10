package com.arbuzerxxl.vibeshot.domain.repository

import com.arbuzerxxl.vibeshot.domain.models.InterestsResourceItem
import kotlinx.coroutines.flow.Flow

interface InterestsRepository {

    suspend fun getPhotos(): Flow<List<InterestsResourceItem>>
}