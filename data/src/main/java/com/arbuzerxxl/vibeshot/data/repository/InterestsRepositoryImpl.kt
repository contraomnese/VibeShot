package com.arbuzerxxl.vibeshot.data.repository

import com.arbuzerxxl.vibeshot.data.exceptions.RequestInterestsPhotosFetchException
import com.arbuzerxxl.vibeshot.data.mappers.toDomain
import com.arbuzerxxl.vibeshot.data.network.api.FlickrInterestsApi
import com.arbuzerxxl.vibeshot.domain.models.InterestsResources
import com.arbuzerxxl.vibeshot.domain.repository.InterestsRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

private const val DEFAULT_ITEMS_COUNT_PER_PAGE = 25

class InterestsRepositoryImpl(
    private val api: FlickrInterestsApi,
    private val key: String,
    private val dispatcher: CoroutineDispatcher,
) : InterestsRepository {

    override suspend fun getPhotos(page: Int): InterestsResources = withContext(dispatcher) {
        try {
            api.getPhotos(
                key = key,
                page = page,
                perPage = DEFAULT_ITEMS_COUNT_PER_PAGE
            )
                .response
                .toDomain()

        } catch (cause: Throwable) {
            if (cause is CancellationException) throw cause
            throw RequestInterestsPhotosFetchException(cause)
        }
    }
}

