package com.arbuzerxxl.vibeshot.domain.usecases.photos

import androidx.paging.PagingData
import com.arbuzerxxl.vibeshot.domain.models.InterestsPhotoResource
import com.arbuzerxxl.vibeshot.domain.repository.InterestsRepository
import kotlinx.coroutines.flow.Flow

class GetInterestsPhotosUseCase(
    private val repository: InterestsRepository,
) {

    fun execute(): Flow<PagingData<InterestsPhotoResource>> = repository.getPhotos()
}