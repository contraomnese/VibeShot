package com.arbuzerxxl.vibeshot.data.repository

import com.arbuzerxxl.vibeshot.data.exceptions.RequestTokenFetchException
import com.arbuzerxxl.vibeshot.data.mappers.toDomain
import com.arbuzerxxl.vibeshot.data.network.api.FlickrInterestsApi
import com.arbuzerxxl.vibeshot.domain.models.InterestsResourceItem
import com.arbuzerxxl.vibeshot.domain.repository.InterestsRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

class InterestsRepositoryImpl(
    private val api: FlickrInterestsApi,
    private val key: String,
    private val dispatcher: CoroutineDispatcher
): InterestsRepository {

    override suspend fun getPhotos(): Flow<List<InterestsResourceItem>> = withContext(dispatcher) {
        try {
            flowOf(api.getPhotos(key = key, extras = "url_m").photos.photo.map { it.toDomain() })
        }
        catch (cause: Throwable) {
            if (cause is CancellationException) throw cause
            throw RequestTokenFetchException(cause)
        }
    }
}