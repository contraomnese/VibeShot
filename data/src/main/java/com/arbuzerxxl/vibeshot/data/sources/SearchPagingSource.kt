package com.arbuzerxxl.vibeshot.data.sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.arbuzerxxl.vibeshot.data.exceptions.RequestSearchPhotosFetchException
import com.arbuzerxxl.vibeshot.data.mappers.toDomain
import com.arbuzerxxl.vibeshot.data.network.api.FlickrSearchApi
import com.arbuzerxxl.vibeshot.domain.models.interest.SearchResource
import com.arbuzerxxl.vibeshot.domain.repository.PhotosRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class SearchPagingSource(
    private val key: String,
    private val searchApi: FlickrSearchApi,
    private val photosRepository: PhotosRepository,
    private val query: String

): PagingSource<Int, SearchResource>() {
    override fun getRefreshKey(state: PagingState<Int, SearchResource>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchResource> {
        try {
            val nextPageNumber = params.key ?: 1
            val response = searchApi.searchByQuery(query = query, page = nextPageNumber, key = key, pageSize = params.loadSize)
            val resources = coroutineScope {
                val awaitAll = response.response.photos.map { photo ->
                    async(Dispatchers.IO) {
                        val sizes = photosRepository.getSizes(photo.id)
                        photo.toDomain(sizes)
                    }
                }.awaitAll()
                awaitAll
            }
            return LoadResult.Page(
                data = resources,
                prevKey = null,
                nextKey = if (response.response.page == response.response.pages) null else response.response.page.plus(1)
            )
        } catch (cause: Throwable) {
            if (cause is CancellationException) throw cause
            throw RequestSearchPhotosFetchException(cause)
        }
    }
}