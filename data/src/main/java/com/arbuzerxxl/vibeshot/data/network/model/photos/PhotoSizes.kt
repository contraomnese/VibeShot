package com.arbuzerxxl.vibeshot.data.network.model.photos

import com.arbuzerxxl.vibeshot.data.adapters.PhotoSizesResponseTypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName

@JsonAdapter(PhotoSizesResponseTypeAdapter::class)
sealed class PhotoSizesResponse {
    data class Success(val photoSizes: PhotoSizes) : PhotoSizesResponse()
    data class Error(val code: Int, val message: String) : PhotoSizesResponse()
}

data class PhotoSizes(
    @SerializedName("canblog") val canBlog: Int,
    @SerializedName("canprint") val canPrint: Int,
    @SerializedName("candownload") val canDownload: Int,
    @SerializedName("size") val sizes: List<Size>
)

data class Size(
    @SerializedName("label") val label: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("source") val sourceUrl: String,
    @SerializedName("url") val pageUrl: String,
)