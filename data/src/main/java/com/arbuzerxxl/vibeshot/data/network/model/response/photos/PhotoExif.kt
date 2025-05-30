package com.arbuzerxxl.vibeshot.data.network.model.response.photos

import com.arbuzerxxl.vibeshot.data.adapters.PhotoExifResponseTypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName

@JsonAdapter(PhotoExifResponseTypeAdapter::class)
sealed class PhotoExifResponse {
    data class Success(val exif: PhotoExif) : PhotoExifResponse()
    data class Error(val code: Int, val message: String) : PhotoExifResponse()
}

data class PhotoExif(
    @SerializedName("id") val id: String,
    @SerializedName("secret") val secret: String,
    @SerializedName("server") val server: String,
    @SerializedName("farm") val farm: Int,
    @SerializedName("camera") val camera: String,
    @SerializedName("exif") val exif: List<ExifData>
)

data class ExifData(
    @SerializedName("tagspace") val tagSpace: String,
    @SerializedName("tagspaceid") val tagSpaceId: Int,
    @SerializedName("tag") val tag: String,
    @SerializedName("label") val label: String,
    @SerializedName("raw") val raw: ContentData? = null,
    @SerializedName("clean") val clean: ContentData? = null
)

data class ContentData(
    @SerializedName("_content") val content: String
)