package com.arbuzerxxl.vibeshot.data.repository

import com.arbuzerxxl.vibeshot.data.exceptions.RequestPhotoSizesFetchException
import com.arbuzerxxl.vibeshot.data.mappers.toDomain
import com.arbuzerxxl.vibeshot.data.network.api.FlickrPhotoApi
import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoSizesResponse
import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoSizesResource
import com.arbuzerxxl.vibeshot.domain.repository.PhotoSizesRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class PhotoSizesRepositoryImpl(
    private val api: FlickrPhotoApi,
    private val key: String,
    private val dispatcher: CoroutineDispatcher,
) : PhotoSizesRepository {

    override suspend fun getSizes(photoId: String): PhotoSizesResource = withContext(dispatcher) {
        try {
            val response = api.getSizes(
                key = key,
                id = photoId,
            )
            when (response) {
                is PhotoSizesResponse.Error -> throw RequestPhotoSizesFetchException(Throwable(response.message))
                is PhotoSizesResponse.Success -> { response.photoSizes.toDomain() }
            }

        } catch (cause: Throwable) {
            if (cause is CancellationException) throw cause
            throw RequestPhotoSizesFetchException(cause)
        }
    }
}