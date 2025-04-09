package com.arbuzerxxl.vibeshot.domain.repository

import com.arbuzerxxl.vibeshot.domain.models.InterestingnessResourceItem
import kotlinx.coroutines.flow.Flow

interface InterestingnessRepository {

    suspend fun getPhotos(): Flow<InterestingnessResourceItem>
}