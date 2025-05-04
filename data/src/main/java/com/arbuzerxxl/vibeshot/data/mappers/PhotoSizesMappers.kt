package com.arbuzerxxl.vibeshot.data.mappers

import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoSizes
import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoSizesResource

fun PhotoSizes.toDomain(): PhotoSizesResource {

    val highQuality = sizes.first { it.label == "Large 2048" || it.label == "Large 1600" || it.label == "Large" }
    val lowQuality = sizes.first { it.label == "Small 400" || it.label == "Small 320" || it.label == "Small" }


    return PhotoSizesResource(
        width = highQuality.width,
        height = highQuality.height,
        highQualityUrl = highQuality.sourceUrl,
        lowQualityUrl = lowQuality.sourceUrl
    )


}
