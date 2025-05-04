package com.arbuzerxxl.vibeshot.domain.models.photo

data class PhotoResource(
    val id: String,
    val url: String,
    val owner: String,
    val title: String,
    val description: String,
    val dateUpload: String,
    val dateTaken: String,
    val views: String,
    val comments: String,
    val cameraResource: CameraResource?,
    val tags: List<String>,
    val license: String,
) {
    companion object {
        val empty: PhotoResource = PhotoResource(
            id = "",
            url = "",
            owner = "",
            title = "",
            description = "",
            dateUpload = "",
            dateTaken = "",
            views = "",
            comments = "",
            cameraResource = null,
            tags = listOf(),
            license = ""
        )
    }
}

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