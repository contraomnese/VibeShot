package com.arbuzerxxl.vibeshot.data.mappers

import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoSizesNetwork
import com.arbuzerxxl.vibeshot.domain.models.PhotoSizesResource

fun PhotoSizesNetwork.toDomain(): PhotoSizesResource {

    val original = sizes.first { it.label == "Original" }
    val highQuality = sizes.firstOrNull { it.label == "Large 2048" || it.label == "Large 1600" || it.label == "Large" } ?: original
    val lowQuality = sizes.first { it.label == "Small 400" || it.label == "Small 320" || it.label == "Small" }

    return PhotoSizesResource(
        width = highQuality.width,
        height = highQuality.height,
        highQualityUrl = highQuality.source,
        lowQualityUrl = lowQuality.source
    )
}