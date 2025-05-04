package com.arbuzerxxl.vibeshot.data.mappers

import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoExifResponse
import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoInfoResponse
import com.arbuzerxxl.vibeshot.domain.models.photo.CameraResource
import com.arbuzerxxl.vibeshot.domain.models.photo.PhotoResource

private const val EMPTY_DATA = ""

internal fun PhotoInfoResponse.Success.toDomain(): PhotoResource = PhotoResource(
    id = info.id,
    url = "",
    owner = info.owner.realName,
    title = info.title.content,
    description = info.description.content,
    dateUpload = info.dateUploaded,
    dateTaken = info.dates.taken,
    views = info.views,
    comments = info.comments.content,
    cameraResource = null,
    tags = info.tags.tag.map { it.raw },
    license = info.license
)

internal fun PhotoExifResponse.Success.toDomain(): CameraResource = CameraResource(
    model = exif.camera ?: EMPTY_DATA,
    lens = exif.exif.firstOrNull { it.label == "LensModel" }?.raw?.content ?: EMPTY_DATA,
    aperture = exif.exif.firstOrNull { it.label == "Aperture" }?.clean?.content ?: EMPTY_DATA,
    focalLength = exif.exif.firstOrNull { it.label == "Focal Length" }?.raw?.content ?: EMPTY_DATA,
    iso = exif.exif.firstOrNull { it.label == "ISO Speed" }?.raw?.content ?: EMPTY_DATA,
    flash = exif.exif.firstOrNull { it.label == "Flash" }?.raw?.content ?: EMPTY_DATA,
    exposureTime = exif.exif.firstOrNull { it.label == "Exposure" }?.raw?.content ?: EMPTY_DATA,
    whiteBalance = exif.exif.firstOrNull { it.label == "White Balance" }?.raw?.content ?: EMPTY_DATA
)