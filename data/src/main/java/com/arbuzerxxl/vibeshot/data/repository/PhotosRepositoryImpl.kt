package com.arbuzerxxl.vibeshot.data.repository

import com.arbuzerxxl.vibeshot.data.exceptions.RequestPhotoInfoFetchException
import com.arbuzerxxl.vibeshot.data.exceptions.RequestPhotoSizesFetchException
import com.arbuzerxxl.vibeshot.data.exceptions.RequestPhotosFetchException
import com.arbuzerxxl.vibeshot.data.mappers.toDomain
import com.arbuzerxxl.vibeshot.data.network.api.FlickrPhotoApi
import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoExifResponse
import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoInfoResponse
import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoSizesResponse
import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoResource
import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoSizesResource
import com.arbuzerxxl.vibeshot.domain.repository.PhotosRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class PhotosRepositoryImpl(
    private val api: FlickrPhotoApi,
    private val key: String,
    private val dispatcher: CoroutineDispatcher,
): PhotosRepository {

    override suspend fun getPhoto(photoId: String): PhotoResource = withContext(dispatcher) {
        try {
            val infoDeferred = async { api.getInfo(key = key, photoId = photoId) }
            val exifDeferred = async { api.getExif(key = key, photoId = photoId) }

            val infoResponse = infoDeferred.await()
            val exifResponse = exifDeferred.await()

            val basePhotoResource = when (infoResponse) {
                is PhotoInfoResponse.Error -> throw RequestPhotoInfoFetchException(Throwable(infoResponse.message))
                is PhotoInfoResponse.Success -> infoResponse.toDomain()
            }

            val enrichedPhotoResource = when (exifResponse) {
                is PhotoExifResponse.Error -> {
                    basePhotoResource
                }
                is PhotoExifResponse.Success -> {
                    basePhotoResource.copy(cameraResource = exifResponse.toDomain())
                }
            }

            enrichedPhotoResource

        } catch (cause: Throwable) {
            if (cause is CancellationException) throw cause
            throw RequestPhotosFetchException(cause)
        }
    }

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