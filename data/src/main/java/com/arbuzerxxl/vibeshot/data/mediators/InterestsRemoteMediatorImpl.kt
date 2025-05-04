package com.arbuzerxxl.vibeshot.data.mediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.room.withTransaction
import com.arbuzerxxl.vibeshot.data.exceptions.RequestInterestsPhotosFetchException
import com.arbuzerxxl.vibeshot.data.mediators.api.InterestsRemoteMediator
import com.arbuzerxxl.vibeshot.data.network.api.FlickrInterestsApi
import com.arbuzerxxl.vibeshot.data.network.model.interestingness.InterestsPhotosNetwork
import com.arbuzerxxl.vibeshot.data.storage.db.AppDatabase
import com.arbuzerxxl.vibeshot.data.storage.db.interests.dto.InterestsPhotoDto
import com.arbuzerxxl.vibeshot.data.storage.db.interests.entities.InterestsEntity
import com.arbuzerxxl.vibeshot.domain.repository.PhotoSizesRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class InterestsRemoteMediatorImpl(
    private val database: AppDatabase,
    private val api: FlickrInterestsApi,
    private val key: String,
    private val photoSizesRepository: PhotoSizesRepository,
    private val dispatcher: CoroutineDispatcher,
): InterestsRemoteMediator() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, InterestsPhotoDto>,
    ): MediatorResult {
        return try {

            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1

                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()

                    lastItem?.page?.plus(1) ?: 1
                }
            }

            val response = loadPhotos(page = loadKey, pageSize = state.config.pageSize)

            val resources = coroutineScope {
                response.photos.map { photo ->
                    async(dispatcher) {
                        val resourceSizes = photoSizesRepository.getSizes(photo.id)
                        InterestsEntity(
                            photoId = photo.id,
                            title = photo.title,
                            highQualityUrl = resourceSizes.highQualityUrl,
                            lowQualityUrl = resourceSizes.lowQualityUrl,
                            width = resourceSizes.width,
                            height = resourceSizes.height,
                            page = loadKey,
                            lastUpdated = System.currentTimeMillis()
                        )
                    }
                }.awaitAll()
            }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.interestsDao().clearAll()
                }

                database.interestsDao().insertAll(resources)
            }

            MediatorResult.Success(
                endOfPaginationReached = response.pages == loadKey
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        val lastUpdate = database.interestsDao().getLastUpdateTime() ?: 0L
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)

        return if (System.currentTimeMillis() - lastUpdate <= cacheTimeout)
        {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    private suspend fun loadPhotos(page: Int, pageSize: Int): InterestsPhotosNetwork = withContext(dispatcher) {
        try {
            api.getPhotos(
                key = key,
                page = page,
                pageSize = pageSize
            )
                .response
        } catch (cause: Throwable) {
            if (cause is CancellationException) throw cause
            throw RequestInterestsPhotosFetchException(cause)
        }
    }
}