package com.arbuzerxxl.vibeshot.data.mediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.room.withTransaction
import com.arbuzerxxl.vibeshot.data.exceptions.RequestInterestsPhotosFetchException
import com.arbuzerxxl.vibeshot.data.mediators.api.InterestsRemoteMediator
import com.arbuzerxxl.vibeshot.data.network.api.FlickrInterestsApi
import com.arbuzerxxl.vibeshot.data.network.model.interests.InterestsResponse
import com.arbuzerxxl.vibeshot.data.storage.db.photo.PhotoDatabase
import com.arbuzerxxl.vibeshot.data.storage.db.photo.interests.dto.InterestsPhotoDto
import com.arbuzerxxl.vibeshot.data.storage.db.photo.interests.entities.InterestsEntity
import com.arbuzerxxl.vibeshot.domain.repository.PhotosRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class InterestsRemoteMediatorImpl(
    private val database: PhotoDatabase,
    private val api: FlickrInterestsApi,
    private val key: String,
    private val photosRepository: PhotosRepository,
    private val dispatcher: CoroutineDispatcher,
) : InterestsRemoteMediator() {

    private var pages: Int? = null

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
                    val key = lastItem?.page?.let { page ->
                        pages?.let { pages ->
                            return if (page >= pages) MediatorResult.Success(endOfPaginationReached = true) else return@let
                        }
                        lastItem.page.plus(1)
                    } ?: 2
                    key
                }
            }

            val response = loadPhotos(page = loadKey, pageSize = state.config.pageSize)

            pages = response.photos.pages

            val resources = response.photos.photo.map { photo ->
                InterestsEntity(
                    photoId = photo.id,
                    title = photo.title,
                    highQualityUrl = photo.urlL ?: photo.urlM ?: photo.urlS,
                    lowQualityUrl = photo.urlS,
                    width = photo.widthL ?: photo.widthM ?: photo.widthS,
                    height = photo.heightL ?: photo.heightM ?: photo.heightS,
                    page = loadKey,
                    lastUpdated = System.currentTimeMillis()
                )
            }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.interestsDao().clearAll()
                }

                database.interestsDao().insertAll(resources)
            }

            MediatorResult.Success(
                endOfPaginationReached = pages == loadKey
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

        return if (System.currentTimeMillis() - lastUpdate <= cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    private suspend fun loadPhotos(page: Int, pageSize: Int): InterestsResponse.Success = withContext(dispatcher) {
        try {
            val interestsResponse = api.getPhotos(
                key = key,
                page = page,
                pageSize = pageSize
            )

            val interestResources = when (interestsResponse) {
                is InterestsResponse.Error -> throw RequestInterestsPhotosFetchException(Throwable(interestsResponse.message))
                is InterestsResponse.Success -> interestsResponse
            }
            interestResources

        } catch (cause: Throwable) {
            if (cause is CancellationException) throw cause
            throw RequestInterestsPhotosFetchException(cause)
        }
    }
}