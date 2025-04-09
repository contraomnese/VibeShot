package com.arbuzerxxl.vibeshot.data.repository

import com.arbuzerxxl.vibeshot.data.exceptions.RequestTokenFetchException
import com.arbuzerxxl.vibeshot.data.mappers.toDomain
import com.arbuzerxxl.vibeshot.data.network.api.FlickrInterestingnessApi
import com.arbuzerxxl.vibeshot.domain.models.InterestingnessResourceItem
import com.arbuzerxxl.vibeshot.domain.repository.InterestingnessRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext

class InterestingnessRepositoryImpl(
    private val api: FlickrInterestingnessApi,
    private val key: String,
    private val dispatcher: CoroutineDispatcher
): InterestingnessRepository {

    override suspend fun getPhotos(): Flow<InterestingnessResourceItem> = withContext(dispatcher) {
        try {
            api.getPhotos(key = key, extras = "url_m").photos.photos.map { it.toDomain() }.asFlow()
        }
        catch (cause: Throwable) {
            if (cause is CancellationException) throw cause
            throw RequestTokenFetchException(cause)
        }
    }
}