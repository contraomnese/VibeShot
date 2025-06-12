package com.arbuzerxxl.vibeshot.domain.repository

import androidx.paging.PagingData
import com.arbuzerxxl.vibeshot.domain.models.interest.SearchResource
import kotlinx.coroutines.flow.SharedFlow

interface SearchRepository {

    val data: SharedFlow<PagingData<SearchResource>>

    suspend fun searchByText(text: String)

    suspend fun searchByTag(tag: String)

    suspend fun clearData()
}