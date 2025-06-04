package com.arbuzerxxl.vibeshot.domain.repository

import androidx.paging.PagingData
import com.arbuzerxxl.vibeshot.domain.models.interest.SearchResource
import kotlinx.coroutines.flow.SharedFlow

interface SearchRepository {

    val data: SharedFlow<PagingData<SearchResource>>

    suspend fun search(query: String)

    suspend fun clear()
}