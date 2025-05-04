package com.arbuzerxxl.vibeshot.domain.repository

import androidx.paging.PagingData
import com.arbuzerxxl.vibeshot.domain.models.interest.InterestsResource
import kotlinx.coroutines.flow.SharedFlow

interface InterestsRepository {

    val data: SharedFlow<PagingData<InterestsResource>>
}