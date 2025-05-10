package com.arbuzerxxl.vibeshot.data.mappers

import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoExif
import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoInfo
import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoSizes
import com.arbuzerxxl.vibeshot.data.storage.db.details.dto.DetailsPhotoDto
import com.arbuzerxxl.vibeshot.data.storage.db.details.entities.DetailsPhotoEntity
import com.arbuzerxxl.vibeshot.domain.models.photo.CameraResource
import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoResource
import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoSizesResource
import com.arbuzerxxl.vibeshot.domain.utils.formatDateTimeWithLocale
import com.arbuzerxxl.vibeshot.domain.utils.formatUnixTimeWithSystemLocale
import com.arbuzerxxl.vibeshot.domain.utils.stripHtmlTags

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

    val highQuality = sizes.filter { it.label.startsWith("Large") || it.label.startsWith("Medium") }.maxBy { it.width * it.height }
    val lowQuality = sizes.filter { it.label.startsWith("Small") || it.label.startsWith("Medium") }.minBy { it.width * it.height }

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
        url = photoUrl,
        owner = if (ownerRealName.isNotEmpty()) ownerRealName else ownerUserName,
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

internal fun PhotoInfo.toEntity(exif: PhotoExif?, url: String): DetailsPhotoEntity =
    DetailsPhotoEntity(
        photoId = id,
        photoUrl = url,
        secret = secret,
        server = server,
        farm = farm,
        dateUploaded = dateUploaded,
        isFavorite = isFavorite,
        license = license,
        safetyLevel = safetyLevel,
        views = views,
        ownerNsid = owner.nsid,
        ownerUsername = owner.username,
        ownerRealName = owner.realName,
        ownerLocation = owner.location,
        ownerIconServer = owner.iconServer,
        ownerIconFarm = owner.iconFarm,
        ownerPathAlias = owner.pathAlias,
        title = title.content,
        description = description.content.stripHtmlTags(),
        comments = comments.content,
        datePosted = dates.posted,
        dateTaken = dates.taken,
        dateLastUpdate = dates.lastUpdate,
        tagsJson = tags.tag,
        camera = if (exif?.camera?.isNotEmpty() == true) exif.camera else null,
        exifJson = exif?.exif,
        lastUpdated = System.currentTimeMillis()
    )
