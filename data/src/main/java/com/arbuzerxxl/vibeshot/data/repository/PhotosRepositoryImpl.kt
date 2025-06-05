package com.arbuzerxxl.vibeshot.data.repository

import android.content.Context
import android.net.Uri
import androidx.room.withTransaction
import com.arbuzerxxl.vibeshot.data.exceptions.RequestPhotoInfoFetchException
import com.arbuzerxxl.vibeshot.data.exceptions.RequestPhotoSizesFetchException
import com.arbuzerxxl.vibeshot.data.exceptions.RequestPhotosFetchException
import com.arbuzerxxl.vibeshot.data.exceptions.RequestUploadPhotoException
import com.arbuzerxxl.vibeshot.data.mappers.toDomain
import com.arbuzerxxl.vibeshot.data.mappers.toEntity
import com.arbuzerxxl.vibeshot.data.network.api.FlickrPhotoApi
import com.arbuzerxxl.vibeshot.data.network.api.FlickrUploadPhotoApi
import com.arbuzerxxl.vibeshot.data.network.model.response.photos.PhotoExif
import com.arbuzerxxl.vibeshot.data.network.model.response.photos.PhotoExifResponse
import com.arbuzerxxl.vibeshot.data.network.model.response.photos.PhotoInfo
import com.arbuzerxxl.vibeshot.data.network.model.response.photos.PhotoInfoResponse
import com.arbuzerxxl.vibeshot.data.network.model.response.photos.PhotoSizesResponse
import com.arbuzerxxl.vibeshot.data.storage.db.photo.PhotoDatabase
import com.arbuzerxxl.vibeshot.data.storage.db.photo.details.dto.DetailsPhotoDto
import com.arbuzerxxl.vibeshot.data.utils.PostNetworkParams
import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoResource
import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoSizesResource
import com.arbuzerxxl.vibeshot.domain.repository.PhotosRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.UUID
import java.util.concurrent.TimeUnit

class PhotosRepositoryImpl(
    private val api: FlickrPhotoApi,
    private val uploadApi: FlickrUploadPhotoApi,
    private val apiKey: String,
    private val apiSecret: String,
    private val apiBaseUrl: String,
    private val storage: PhotoDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val context: Context,
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
                key = apiKey,
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

    override suspend fun uploadPhoto(token: String, tokenSecret: String, photoUrl: Uri, title: String): String? =
        withContext(dispatcher) {
            try {

                val inputStream = context.contentResolver.openInputStream(photoUrl)
                val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
                inputStream.use { input ->
                    tempFile.outputStream().use { output ->
                        input?.copyTo(output)
                    }
                }
                val photoRequestBody = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
                val photo = MultipartBody.Part.createFormData("photo", tempFile.name, photoRequestBody)
                val nonce = UUID.randomUUID().toString()
                val timestamp = System.currentTimeMillis().toString()
                val postMethod = "services/upload/"

                val params = PostNetworkParams(
                    baseUrl = apiBaseUrl + postMethod,
                    token = token,
                    nonce = nonce,
                    timestamp = timestamp,
                    consumerKey = apiKey,
                    consumerSecret = apiSecret,
                    tokenSecret = tokenSecret,
                    title = title
                )

                val response = uploadApi.uploadPhoto(
                    photo = photo,
                    authorizationHeader = params.authorizationHeader,
                    title = title.toRequestBody("text/plain".toMediaTypeOrNull())
                )

                if (response.status == "ok") {
                    response.photoId
                } else {
                    throw RequestUploadPhotoException(Throwable(response.msg))
                }

            } catch (cause: Throwable) {
                if (cause is CancellationException) throw cause
                throw RequestUploadPhotoException(cause)
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

    private suspend fun getPhotoFromNetwork(photoId: String, photoUrl: String): DetailsPhotoDto =
        withContext(dispatcher) {

            val infoDeferred = async { api.getInfo(key = apiKey, photoId = photoId) }
            val exifDeferred = async { api.getExif(key = apiKey, photoId = photoId) }

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

    private suspend fun savePhotoToStorage(info: PhotoInfo, exif: PhotoExif? = null, url: String) =
        storage.withTransaction {
            storage.detailsDao().insert(
                info.toEntity(exif, url)
            )
        }
}
