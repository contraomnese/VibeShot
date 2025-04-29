package com.arbuzerxxl.vibeshot.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.arbuzerxxl.vibeshot.data.mappers.toDomain
import com.arbuzerxxl.vibeshot.data.mediators.api.InterestsRemoteMediator
import com.arbuzerxxl.vibeshot.data.storage.db.AppDatabase
import com.arbuzerxxl.vibeshot.domain.models.InterestsPhotoResource
import com.arbuzerxxl.vibeshot.domain.repository.InterestsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf


@OptIn(ExperimentalPagingApi::class)
class InterestsRepositoryImpl(
    private val database: AppDatabase,
) : InterestsRepository, KoinComponent {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPhotos(perPage: Int, index: Int?): Flow<PagingData<InterestsPhotoResource>> {

        val mediator: InterestsRemoteMediator by inject(parameters = { parametersOf(perPage) })
        val pager = index?.let {
            Pager(
                config = PagingConfig(pageSize = 25, enablePlaceholders = false, initialLoadSize = index + 25),
                remoteMediator = mediator
            ) {
                database.interestsDao().getAll()
            }.flow
                .map { pagingData -> pagingData.map { it.toDomain() } }
        } ?: Pager(
            config = PagingConfig(pageSize = 25, enablePlaceholders = false),
            remoteMediator = mediator
        ) {
            database.interestsDao().getAll()
        }.flow
            .map { pagingData -> pagingData.map { it.toDomain() } }
        return pager
    }
}

