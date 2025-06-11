package com.arbuzerxxl.vibeshot.domain.models.photo

data class PhotoResource(
    val id: String,
    val resourceUrl: String,
    val fullPhotoPageUrl: String,
    val owner: String,
    val ownerIconUrl: String,
    val title: String,
    val description: String,
    val dateUpload: String,
    val dateTaken: String,
    val views: String,
    val comments: String,
    val cameraResource: CameraResource?,
    val tags: List<String>,
    val license: String,
)

data class CameraResource(
    val model: String,
    val lens: String,
    val aperture: String,
    val focalLength: String,
    val iso: String,
    val flash: String,
    val exposureTime: String,
    val whiteBalance: String,
)