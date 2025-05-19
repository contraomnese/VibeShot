package com.arbuzerxxl.vibeshot.data.repository

import androidx.room.withTransaction
import com.arbuzerxxl.vibeshot.data.exceptions.RequestPhotoInfoFetchException
import com.arbuzerxxl.vibeshot.data.exceptions.RequestPhotoSizesFetchException
import com.arbuzerxxl.vibeshot.data.exceptions.RequestPhotosFetchException
import com.arbuzerxxl.vibeshot.data.mappers.toDomain
import com.arbuzerxxl.vibeshot.data.mappers.toEntity
import com.arbuzerxxl.vibeshot.data.network.api.FlickrPhotoApi
import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoExif
import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoExifResponse
import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoInfo
import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoInfoResponse
import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoSizesResponse
import com.arbuzerxxl.vibeshot.data.storage.db.photo.PhotoDatabase
import com.arbuzerxxl.vibeshot.data.storage.db.photo.details.dto.DetailsPhotoDto
import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoResource
import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoSizesResource
import com.arbuzerxxl.vibeshot.domain.repository.PhotosRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class PhotosRepositoryImpl(
    private val api: FlickrPhotoApi,
    private val key: String,
    private val storage: PhotoDatabase,
    private val dispatcher: CoroutineDispatcher,
) : PhotosRepository {

    override suspend fun getPhoto(photoId: String, photoUrl: String): PhotoResource = withContext(dispatcher) {
        try {
            updateStorage()

            val photo = getPhotoFromStorage(photoId) ?: getPhotoFromNetwork(photoId = photoId, photoUrl = photoUrl)

            photo.toDomain()

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
                is PhotoSizesResponse.Success -> {
                    response.photoSizes.toDomain()
                }
            }

        } catch (cause: Throwable) {
            if (cause is CancellationException) throw cause
            throw RequestPhotoSizesFetchException(cause)
        }
    }

    private suspend fun updateStorage() {
        if (!skipUpdateStorage()) {
            storage.withTransaction {
                storage.detailsDao().clearAll()
            }
        }
    }

    private suspend fun skipUpdateStorage(): Boolean {
        val lastUpdate = storage.detailsDao().getLastUpdateTime() ?: 0L
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
        val now = System.currentTimeMillis()
        val diff = now - lastUpdate

        return diff <= cacheTimeout
    }

    private suspend fun getPhotoFromStorage(photoId: String): DetailsPhotoDto? =
        storage.withTransaction { storage.detailsDao().getPhoto(photoId) }

    private suspend fun getPhotoFromNetwork(photoId: String, photoUrl: String): DetailsPhotoDto = withContext(dispatcher) {

        val infoDeferred = async { api.getInfo(key = key, photoId = photoId) }
        val exifDeferred = async { api.getExif(key = key, photoId = photoId) }

        val infoResponse = infoDeferred.await()
        val exifResponse = exifDeferred.await()

        val infoResource = when (infoResponse) {
            is PhotoInfoResponse.Error -> throw RequestPhotoInfoFetchException(Throwable(infoResponse.message))
            is PhotoInfoResponse.Success -> {
                infoResponse
            }
        }

        when (exifResponse) {
            is PhotoExifResponse.Error -> {
                savePhotoToStorage(info = infoResource.info, url = photoUrl)
            }

            is PhotoExifResponse.Success -> {
                savePhotoToStorage(info = infoResource.info, exif = exifResponse.exif, url = photoUrl)
            }
        }

        getPhotoFromStorage(photoId)!!
    }

    private suspend fun savePhotoToStorage(info: PhotoInfo, exif: PhotoExif? = null, url: String) = storage.withTransaction {
        storage.detailsDao().insert(
            info.toEntity(exif, url)
        )
    }
}
