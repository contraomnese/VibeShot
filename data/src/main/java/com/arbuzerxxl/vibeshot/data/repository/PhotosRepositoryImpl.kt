package com.arbuzerxxl.vibeshot.data.repository

import androidx.room.withTransaction
import com.arbuzerxxl.vibeshot.data.exceptions.RequestPhotoInfoFetchException
import com.arbuzerxxl.vibeshot.data.exceptions.RequestPhotoSizesFetchException
import com.arbuzerxxl.vibeshot.data.exceptions.RequestPhotosFetchException
import com.arbuzerxxl.vibeshot.data.mappers.toDomain
import com.arbuzerxxl.vibeshot.data.network.api.FlickrPhotoApi
import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoExif
import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoExifResponse
import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoInfo
import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoInfoResponse
import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoSizesResponse
import com.arbuzerxxl.vibeshot.data.storage.db.AppDatabase
import com.arbuzerxxl.vibeshot.data.storage.db.details.dto.DetailsPhotoDto
import com.arbuzerxxl.vibeshot.data.storage.db.details.entities.DetailsPhotoEntity
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
    private val database: AppDatabase,
    private val dispatcher: CoroutineDispatcher,
) : PhotosRepository {

    override suspend fun getPhoto(photoId: String): PhotoResource = withContext(dispatcher) {
        try {
            if (isUpdateDb()) {
                database.withTransaction {
                    database.detailsDao().clearAll()
                }
            }

            val photo = getPhotoFromDb(photoId)

            photo?.toDomain() ?: withContext(dispatcher) {
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

                val photoResource = database.withTransaction {
                    when (exifResponse) {
                        is PhotoExifResponse.Error -> {
                            addPhotoToDb(info = infoResource.info)
                        }

                        is PhotoExifResponse.Success -> {
                            addPhotoToDb(info = infoResource.info, exif = exifResponse.exif)
                        }
                    }
                    getPhotoFromDb(photoId)!!
                }
                photoResource.toDomain()
            }

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

    private fun getPhotoFromDb(photoId: String): DetailsPhotoDto? = database.detailsDao().getPhoto(photoId)

    private suspend fun addPhotoToDb(info: PhotoInfo, exif: PhotoExif? = null) {
        database.detailsDao().insert(
            DetailsPhotoEntity(
                photoId = info.id,
                secret = info.secret,
                server = info.server,
                farm = info.farm,
                dateUploaded = info.dateUploaded,
                isFavorite = info.isFavorite,
                license = info.license,
                safetyLevel = info.safetyLevel,
                views = info.views,
                ownerNsid = info.owner.nsid,
                ownerUsername = info.owner.username,
                ownerRealName = info.owner.realName,
                ownerLocation = info.owner.location,
                ownerIconServer = info.owner.iconServer,
                ownerIconFarm = info.owner.iconFarm,
                ownerPathAlias = info.owner.pathAlias,
                title = info.title.content,
                description = info.description.content,
                comments = info.comments.content,
                datePosted = info.dates.posted,
                dateTaken = info.dates.taken,
                dateLastUpdate = info.dates.lastUpdate,
                tagsJson = info.tags.tag,
                camera = exif?.camera,
                exifJson = exif?.exif,
                sizesJson = null,
                lastUpdated = System.currentTimeMillis()
            )
        )
    }

    private suspend fun isUpdateDb(): Boolean {
        return false
        val lastUpdate = database.detailsDao().getLastUpdateTime() ?: 0L
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
        val result = (System.currentTimeMillis() - lastUpdate <= cacheTimeout)

        return result
    }
}
