package com.arbuzerxxl.vibeshot.data.repository

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arbuzerxxl.vibeshot.data.network.api.FlickrSearchApi
import com.arbuzerxxl.vibeshot.data.sources.TagSearchPagingSource
import com.arbuzerxxl.vibeshot.data.sources.TextSearchPagingSource
import com.arbuzerxxl.vibeshot.domain.models.interest.SearchResource
import com.arbuzerxxl.vibeshot.domain.repository.SearchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

private const val SEARCH_PAGE_SIZE = 25

@OptIn(ExperimentalPagingApi::class)
class SearchRepositoryImpl(
    private val context: Context,
    private val key: String,
    private val searchApi: FlickrSearchApi,
    private val dispatcher: CoroutineDispatcher
) : SearchRepository, KoinComponent {

    private val _data = MutableSharedFlow<PagingData<SearchResource>>(replay = 1)
    override val data: SharedFlow<PagingData<SearchResource>> = _data

    override suspend fun searchByText(text: String) {
        CoroutineScope(dispatcher).launch {
            Pager(
                config = PagingConfig(pageSize = SEARCH_PAGE_SIZE, enablePlaceholders = true),
            ) {
                TextSearchPagingSource(searchApi = searchApi, text = text, key = key, context = context)
            }.flow
                .cachedIn(CoroutineScope(dispatcher))
                .collect {
                    _data.emit(it)
                }
        }
    }

    override suspend fun searchByTag(tag: String) {
        CoroutineScope(dispatcher).launch {
            Pager(
                config = PagingConfig(pageSize = SEARCH_PAGE_SIZE, enablePlaceholders = true),
            ) {
                TagSearchPagingSource(searchApi = searchApi, tag = tag, key = key, context = context)
            }.flow
                .cachedIn(CoroutineScope(dispatcher))
                .collect {
                    _data.emit(it)
                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun clearData() {
        _data.emit(PagingData.empty())
    }
}