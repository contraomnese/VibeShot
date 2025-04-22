package com.arbuzerxxl.vibeshot.domain.usecases.photos

import com.arbuzerxxl.vibeshot.domain.models.InterestsPhotoResource
import com.arbuzerxxl.vibeshot.domain.models.InterestsPhotoResources
import com.arbuzerxxl.vibeshot.domain.repository.InterestsRepository
import com.arbuzerxxl.vibeshot.domain.repository.PhotoSizesRepository
import com.arbuzerxxl.vibeshot.domain.usecases.UseCaseWithParams
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class GetInterestsPhotosUseCase(
    private val interestsRepository: InterestsRepository,
    private val photoSizesRepository: PhotoSizesRepository,
    private val dispatcher: CoroutineDispatcher
) : UseCaseWithParams<InterestsPhotoResources, Int> {

    override suspend fun execute(page: Int): InterestsPhotoResources = coroutineScope {
        val photos = interestsRepository.getPhotos(page)

        val resources = photos.resources.map { resource ->
            async(dispatcher) {
                InterestsPhotoResource(
                    id = resource.id,
                    title = resource.title,
                    sizes = photoSizesRepository.getSizes(resource.id)
                )
            }
        }.awaitAll()

        InterestsPhotoResources(resources, photos.pages)
    }
}