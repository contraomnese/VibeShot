package com.arbuzerxxl.vibeshot.data.mappers


import com.arbuzerxxl.vibeshot.data.storage.db.interests.dto.InterestsPhotoDto
import com.arbuzerxxl.vibeshot.domain.models.interest.InterestsResource
import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoSizesResource


internal fun InterestsPhotoDto.toDomain(): InterestsResource {
    return InterestsResource(
        id = photoId,
        title = title,
        sizes = PhotoSizesResource(
            width = width,
            height = height,
            highQualityUrl = highQualityUrl,
            lowQualityUrl = lowQualityUrl
        )
    )
}

