package com.arbuzerxxl.vibeshot.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.arbuzerxxl.vibeshot.data.mappers.toDomain
import com.arbuzerxxl.vibeshot.data.mediators.api.InterestsRemoteMediator
import com.arbuzerxxl.vibeshot.data.storage.database.AppDatabase
import com.arbuzerxxl.vibeshot.domain.models.InterestsPhotoResource
import com.arbuzerxxl.vibeshot.domain.repository.InterestsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class InterestsRepositoryImpl(
    private val database: AppDatabase,
    private val mediator: InterestsRemoteMediator
) : InterestsRepository {


    @OptIn(ExperimentalPagingApi::class)
    override fun getPhotos(): Flow<PagingData<InterestsPhotoResource>> {

        return Pager(
            config = PagingConfig(pageSize = 25, initialLoadSize = 25, enablePlaceholders = false),
            remoteMediator = mediator
        ) {
            database.interestsDao().pagingSource()
        }.flow
            .map { pagingData -> pagingData.map { it.toDomain() } }
    }
}

