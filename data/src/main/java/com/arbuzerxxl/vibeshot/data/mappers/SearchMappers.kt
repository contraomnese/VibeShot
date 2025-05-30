package com.arbuzerxxl.vibeshot.data.mappers

import com.arbuzerxxl.vibeshot.data.network.model.response.search.SearchPhoto
import com.arbuzerxxl.vibeshot.domain.models.interest.SearchResource
import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoSizesResource


internal fun SearchPhoto.toDomain(): SearchResource = SearchResource(
    id = id,
    title = title,
    sizes = PhotoSizesResource(
        highQualityUrl = urlL ?: urlM ?: urlS,
        lowQualityUrl = urlS,
        width = widthL ?: widthM ?: widthS,
        height = heightL ?: heightM ?: heightS,
    )
)