package com.arbuzerxxl.vibeshot.domain.usecases.photos

import androidx.paging.PagingData
import com.arbuzerxxl.vibeshot.domain.models.InterestsPhotoResource
import com.arbuzerxxl.vibeshot.domain.repository.InterestsRepository
import com.arbuzerxxl.vibeshot.domain.usecases.UseCaseWithParams
import kotlinx.coroutines.flow.Flow

private const val PER_PAGE = 25

class GetInterestsPhotosUseCaseSuspend(
    private val repository: InterestsRepository,
): UseCaseWithParams<Flow<PagingData<InterestsPhotoResource>>, Int?> {

    override fun execute(index: Int?): Flow<PagingData<InterestsPhotoResource>> = repository.getPhotos(perPage = PER_PAGE, index = index)
}