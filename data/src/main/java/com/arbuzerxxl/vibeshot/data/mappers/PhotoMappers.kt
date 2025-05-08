package com.arbuzerxxl.vibeshot.data.mappers

import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoSizes
import com.arbuzerxxl.vibeshot.data.storage.db.details.dto.DetailsPhotoDto
import com.arbuzerxxl.vibeshot.domain.models.photo.CameraResource
import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoResource
import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoSizesResource
import com.arbuzerxxl.vibeshot.domain.utils.formatDateTimeWithLocale
import com.arbuzerxxl.vibeshot.domain.utils.formatUnixTimeWithSystemLocale

private const val EMPTY_DATA = ""

internal object LicenseMapper {
    private val licenseMap = mapOf(
        "0" to "All Rights Reserved",
        "4" to "Attribution License",
        "6" to "Attribution-NoDerivs License",
        "3" to "Attribution-NonCommercial-NoDerivs License",
        "2" to "Attribution-NonCommercial License",
        "1" to "Attribution-NonCommercial-ShareAlike License",
        "5" to "Attribution-ShareAlike License",
        "7" to "No known copyright restrictions",
        "8" to "United States Government Work",
        "9" to "Public Domain Dedication (CC0)",
        "10" to "Public Domain Mark"
    )

    fun getNameById(id: String): String {
        return licenseMap[id] ?: "Unknown License"
    }
}

internal fun PhotoSizes.toDomain(): PhotoSizesResource {

    val highQuality =
        sizes.first { it.label == "Large 2048" || it.label == "Large 1600" || it.label == "Large" || it.label == "Medium 800" || it.label == "Medium 640" || it.label == "Medium" }
    val lowQuality = sizes.first { it.label == "Small 400" || it.label == "Small 320" || it.label == "Small" }

    return PhotoSizesResource(
        width = highQuality.width,
        height = highQuality.height,
        highQualityUrl = highQuality.sourceUrl,
        lowQualityUrl = lowQuality.sourceUrl
    )
}

internal fun DetailsPhotoDto.toDomain(): PhotoResource {
    return PhotoResource(
        id = photoId,
        url = sizesJson?.first { it.label == "Large 2048" || it.label == "Large 1600" || it.label == "Large" || it.label == "Medium 800" || it.label == "Medium 640" || it.label == "Medium" }?.sourceUrl
            ?: EMPTY_DATA,
        owner = ownerRealName,
        ownerIconUrl = "https://farm${ownerIconFarm}.staticflickr.com/${ownerIconServer}/buddyicons/${ownerNsid}_r.jpg",
        title = title.trim(),
        description = description.trim(),
        dateUpload = dateUploaded.formatUnixTimeWithSystemLocale(),
        dateTaken = dateTaken.formatDateTimeWithLocale(),
        views = views,
        comments = comments,
        cameraResource = if (camera != null) CameraResource(
            model = camera,
            lens = exifJson?.firstOrNull { it.label == "LensModel" }?.raw?.content ?: EMPTY_DATA,
            aperture = exifJson?.firstOrNull { it.label == "Aperture" }?.clean?.content ?: EMPTY_DATA,
            focalLength = exifJson?.firstOrNull { it.label == "Focal Length" }?.raw?.content ?: EMPTY_DATA,
            iso = exifJson?.firstOrNull { it.label == "ISO Speed" }?.raw?.content ?: EMPTY_DATA,
            flash = exifJson?.firstOrNull { it.label == "Flash" }?.raw?.content ?: EMPTY_DATA,
            exposureTime = exifJson?.firstOrNull { it.label == "Exposure" }?.raw?.content ?: EMPTY_DATA,
            whiteBalance = exifJson?.firstOrNull { it.label == "White Balance" }?.raw?.content ?: EMPTY_DATA

        ) else null,
        tags = tagsJson.map { it.raw },
        license = LicenseMapper.getNameById(license)
    )
}

