package com.arbuzerxxl.vibeshot.domain.repository

import androidx.paging.PagingData
import com.arbuzerxxl.vibeshot.domain.models.interest.SearchResource
import kotlinx.coroutines.flow.SharedFlow

interface SearchRepository {

    val data: SharedFlow<PagingData<SearchResource>>

    fun search(query: String)
}