package com.arbuzerxxl.vibeshot.data.sources

//class InterestsPagingSourceImpl(
//    private val interestsRepository: InterestsRepository,
//    private val photoSizesRepository: PhotoSizesRepository,
//    private val dispatcher: CoroutineDispatcher,
//) : InterestsPagingSource() {
//
//    override fun getRefreshKey(state: PagingState<Int, InterestsPhotoResource>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, InterestsPhotoResource> {
//        val page = params.key ?: 1
//
//        val photos = interestsRepository.loadPhotos(page)
//
//        val resources = coroutineScope {
//            photos.resources.map { resource ->
//                async(dispatcher) {
//                    InterestsPhotoResource(
//                        id = resource.id,
//                        title = resource.title,
//                        sizes = photoSizesRepository.getSizes(resource.id)
//                    )
//                }
//            }.awaitAll()
//        }
//
//        return LoadResult.Page(
//            data = resources,
//            prevKey = if (page == 1) null else page - 1,
//            nextKey = if (page < photos.pages) page + 1 else null
//        )
//    }
//}