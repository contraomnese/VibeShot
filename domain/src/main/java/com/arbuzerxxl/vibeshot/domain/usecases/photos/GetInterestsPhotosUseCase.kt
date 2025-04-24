package com.arbuzerxxl.vibeshot.domain.usecases.photos

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.arbuzerxxl.vibeshot.domain.models.InterestsPhotoResource
import com.arbuzerxxl.vibeshot.domain.sources.InterestsPagingSource
import kotlinx.coroutines.flow.Flow

class GetInterestsPhotosUseCase(
    private val source: InterestsPagingSource
) {

    fun execute(): Flow<PagingData<InterestsPhotoResource>> {
        return Pager(PagingConfig(pageSize = 25, enablePlaceholders = false)) {
            source
        }.flow
    }
}