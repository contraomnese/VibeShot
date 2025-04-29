package com.arbuzerxxl.vibeshot.data.mappers


import com.arbuzerxxl.vibeshot.data.network.model.interestingness.InterestsPhotoNetwork
import com.arbuzerxxl.vibeshot.data.network.model.interestingness.InterestsPhotosNetwork
import com.arbuzerxxl.vibeshot.data.storage.db.interests.dto.InterestsPhotoDto
import com.arbuzerxxl.vibeshot.domain.models.InterestsPhotoResource
import com.arbuzerxxl.vibeshot.domain.models.InterestsResource
import com.arbuzerxxl.vibeshot.domain.models.InterestsResources
import com.arbuzerxxl.vibeshot.domain.models.PhotoSizesResource

fun InterestsPhotosNetwork.toDomain(): InterestsResources {

    return InterestsResources(
        resources = photos.map { it.toDomain() },
        pages = pages,
    )
}

fun InterestsPhotoNetwork.toDomain(): InterestsResource {
    return InterestsResource(
        id = id,
        title = title,
    )
}

fun InterestsPhotoDto.toDomain(): InterestsPhotoResource {
    return InterestsPhotoResource(
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

