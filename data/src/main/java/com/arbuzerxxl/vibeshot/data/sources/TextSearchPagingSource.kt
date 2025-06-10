package com.arbuzerxxl.vibeshot.data.sources

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.arbuzerxxl.vibeshot.data.exceptions.RequestSearchPhotosFetchException
import com.arbuzerxxl.vibeshot.data.mappers.toDomain
import com.arbuzerxxl.vibeshot.data.network.api.FlickrSearchApi
import com.arbuzerxxl.vibeshot.data.network.model.response.search.SearchResponse
import com.arbuzerxxl.vibeshot.data.utils.NetworkUtils
import com.arbuzerxxl.vibeshot.domain.models.interest.SearchResource
import kotlinx.coroutines.CancellationException

class TextSearchPagingSource(
    private val context: Context,
    private val key: String,
    private val searchApi: FlickrSearchApi,
    private var text: String
): PagingSource<Int, SearchResource>() {
    override fun getRefreshKey(state: PagingState<Int, SearchResource>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchResource> {

        if (!NetworkUtils(context).isInternetAvailable()) {
            return LoadResult.Error(RequestSearchPhotosFetchException(Throwable("No internet connection")))
        }

        return try {
            val nextPageNumber = params.key ?: 1

            val searchResponse = searchApi.searchByText(
                text = text,
                page = nextPageNumber,
                key = key,
                pageSize = params.loadSize
            )

            val searchResources = when (searchResponse) {
                is SearchResponse.Error -> throw RequestSearchPhotosFetchException(Throwable(searchResponse.message))
                is SearchResponse.Success -> searchResponse
            }

            LoadResult.Page(
                data = searchResources.photos.photo.map { it.toDomain() },
                prevKey = null,
                nextKey = if (searchResponse.photos.page == searchResponse.photos.pages) null else searchResponse.photos.page + 1
            )
        } catch (cause: Throwable) {
            if (cause is CancellationException) throw cause
            LoadResult.Error(RequestSearchPhotosFetchException(cause))
        }
    }
}