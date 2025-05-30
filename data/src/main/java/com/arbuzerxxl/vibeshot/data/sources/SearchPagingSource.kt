package com.arbuzerxxl.vibeshot.data.sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.arbuzerxxl.vibeshot.data.exceptions.RequestSearchPhotosFetchException
import com.arbuzerxxl.vibeshot.data.mappers.toDomain
import com.arbuzerxxl.vibeshot.data.network.api.FlickrSearchApi
import com.arbuzerxxl.vibeshot.data.network.model.response.search.SearchResponse
import com.arbuzerxxl.vibeshot.domain.models.interest.SearchResource
import kotlinx.coroutines.CancellationException

class SearchPagingSource(
    private val key: String,
    private val searchApi: FlickrSearchApi,
    private val query: String,
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
            val searchResponse = searchApi.searchByQuery(query = query, page = nextPageNumber, key = key, pageSize = params.loadSize)

            val searchResources = when (searchResponse) {
                is SearchResponse.Error -> throw RequestSearchPhotosFetchException(Throwable(searchResponse.message))
                is SearchResponse.Success -> searchResponse
            }

            return LoadResult.Page(
                data = searchResources.photos.photo.map { it.toDomain() },
                prevKey = null,
                nextKey = if (searchResponse.photos.page == searchResponse.photos.pages) null else searchResponse.photos.page.plus(1)
            )
        } catch (cause: Throwable) {
            if (cause is CancellationException) throw cause
            throw RequestSearchPhotosFetchException(cause)
        }
    }
}