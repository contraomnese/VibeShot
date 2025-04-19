package com.arbuzerxxl.vibeshot.data.mappers


import com.arbuzerxxl.vibeshot.data.network.model.interestingness.InterestsPhotoNetwork
import com.arbuzerxxl.vibeshot.data.network.model.interestingness.InterestsPhotosNetwork
import com.arbuzerxxl.vibeshot.domain.models.HighQualityInterestsPhotoUrl
import com.arbuzerxxl.vibeshot.domain.models.InterestsResource
import com.arbuzerxxl.vibeshot.domain.models.InterestsResources
import com.arbuzerxxl.vibeshot.domain.models.LowQualityInterestsPhotoUrl

fun InterestsPhotosNetwork.toDomain(): InterestsResources {

    return InterestsResources(
        resources = photos.map { it.toDomain() },
        pages = pages,
    )
}

fun InterestsPhotoNetwork.toDomain(): InterestsResource {
    return InterestsResource(
        title = title,
        lowQualityImageUrl = LowQualityInterestsPhotoUrl(urlS!!),
        highQualityImageUrl = HighQualityInterestsPhotoUrl(urlL ?: urlS),
        height = heightL ?: heightS!!,
        width = widthL ?: widthS!!
    )
}

