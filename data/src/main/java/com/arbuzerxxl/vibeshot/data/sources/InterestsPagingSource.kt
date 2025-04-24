package com.arbuzerxxl.vibeshot.data.sources

import androidx.paging.PagingState
import com.arbuzerxxl.vibeshot.domain.models.InterestsPhotoResource
import com.arbuzerxxl.vibeshot.domain.repository.InterestsRepository
import com.arbuzerxxl.vibeshot.domain.repository.PhotoSizesRepository
import com.arbuzerxxl.vibeshot.domain.sources.InterestsPagingSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class InterestsPagingSourceImpl(
    private val interestsRepository: InterestsRepository,
    private val photoSizesRepository: PhotoSizesRepository,
    private val dispatcher: CoroutineDispatcher,
) : InterestsPagingSource() {

    override fun getRefreshKey(state: PagingState<Int, InterestsPhotoResource>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, InterestsPhotoResource> {
        val page = params.key ?: 1

        val photos = interestsRepository.getPhotos(page)

        val resources = coroutineScope {
            photos.resources.map { resource ->
                async(dispatcher) {
                    InterestsPhotoResource(
                        id = resource.id,
                        title = resource.title,
                        sizes = photoSizesRepository.getSizes(resource.id)
                    )
                }
            }.awaitAll()
        }

        return LoadResult.Page(
            data = resources,
            prevKey = if (page == 1) null else page - 1,
            nextKey = if (page < photos.pages) page + 1 else null
        )
    }
}