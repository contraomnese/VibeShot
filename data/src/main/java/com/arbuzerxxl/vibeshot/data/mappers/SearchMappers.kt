package com.arbuzerxxl.vibeshot.data.mappers

import com.arbuzerxxl.vibeshot.data.sources.model.SearchSource
import com.arbuzerxxl.vibeshot.domain.models.interest.SearchResource
import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoSizesResource

internal fun SearchSource.toDomain(sizes: PhotoSizesResource): SearchResource = SearchResource(
    id = id,
    title = title,
    sizes = sizes
)