package com.arbuzerxxl.vibeshot.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.arbuzerxxl.vibeshot.data.mappers.toDomain
import com.arbuzerxxl.vibeshot.data.mediators.api.InterestsRemoteMediator
import com.arbuzerxxl.vibeshot.data.storage.db.AppDatabase
import com.arbuzerxxl.vibeshot.domain.models.interest.InterestsResource
import com.arbuzerxxl.vibeshot.domain.repository.InterestsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

private const val INTERESTS_PAGE_SIZE = 25

@OptIn(ExperimentalPagingApi::class)
class InterestsRepositoryImpl(
    private val database: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) : InterestsRepository, KoinComponent {

    private val mediator: InterestsRemoteMediator by inject(parameters = { parametersOf(INTERESTS_PAGE_SIZE) })

    private val _data = MutableSharedFlow<PagingData<InterestsResource>>(replay = 1)
    override val data: SharedFlow<PagingData<InterestsResource>> = _data

    init {
        load()
    }

    private fun load() = CoroutineScope(dispatcher).launch {
        Pager(
            config = PagingConfig(pageSize = INTERESTS_PAGE_SIZE, enablePlaceholders = true),
            remoteMediator = mediator
        ) {
            database.interestsDao().getAll()
        }.flow
            .cachedIn(CoroutineScope(dispatcher))
            .map { pagingData -> pagingData.map { it.toDomain() } }
            .collect {
                _data.emit(it)
            }
    }
}

