package com.arbuzerxxl.vibeshot.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arbuzerxxl.vibeshot.data.network.api.FlickrSearchApi
import com.arbuzerxxl.vibeshot.data.sources.SearchPagingSource
import com.arbuzerxxl.vibeshot.domain.models.interest.SearchResource
import com.arbuzerxxl.vibeshot.domain.repository.PhotosRepository
import com.arbuzerxxl.vibeshot.domain.repository.SearchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

private const val SEARCH_PAGE_SIZE = 25

@OptIn(ExperimentalPagingApi::class)
class SearchRepositoryImpl(
    private val key: String,
    private val searchApi: FlickrSearchApi,
    private val photosRepository: PhotosRepository,
    private val dispatcher: CoroutineDispatcher
) : SearchRepository, KoinComponent {

    private val _data = MutableSharedFlow<PagingData<SearchResource>>(replay = 1)
    override val data: SharedFlow<PagingData<SearchResource>> = _data

    override fun search(query: String) {
        CoroutineScope(dispatcher).launch {
            Pager(
                config = PagingConfig(pageSize = SEARCH_PAGE_SIZE, enablePlaceholders = true, initialLoadSize = SEARCH_PAGE_SIZE),
            ) {
                SearchPagingSource(searchApi = searchApi, query = query, key = key, photosRepository = photosRepository)
            }.flow
                .cachedIn(CoroutineScope(dispatcher))
                .collect {
                    _data.emit(it)
                }
        }
    }
}