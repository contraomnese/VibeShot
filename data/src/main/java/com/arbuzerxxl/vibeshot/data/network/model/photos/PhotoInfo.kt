package com.arbuzerxxl.vibeshot.data.network.model.photos

import com.arbuzerxxl.vibeshot.data.adapters.PhotoInfoResponseTypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName

@JsonAdapter(PhotoInfoResponseTypeAdapter::class)
sealed class PhotoInfoResponse {
    data class Success(val info: PhotoInfo) : PhotoInfoResponse()
    data class Error(val code: Int, val message: String) : PhotoInfoResponse()
}

data class PhotoInfo(
    @SerializedName("id") val id: String,
    @SerializedName("secret") val secret: String,
    @SerializedName("server") val server: String,
    @SerializedName("farm") val farm: Int,
    @SerializedName("dateuploaded") val dateUploaded: String,
    @SerializedName("isfavorite") val isFavorite: Int,
    @SerializedName("license") val license: String,
    @SerializedName("safety_level") val safetyLevel: String,
    @SerializedName("owner") val owner: Owner,
    @SerializedName("title") val title: Content,
    @SerializedName("description") val description: Content,
    @SerializedName("dates") val dates: Dates,
    @SerializedName("views") val views: String,
    @SerializedName("comments") val comments: Content,
    @SerializedName("tags") val tags: Tags,
)

data class Owner(
    @SerializedName("nsid") val nsid: String,
    @SerializedName("username") val username: String,
    @SerializedName("realname") val realName: String,
    @SerializedName("location") val location: String,
    @SerializedName("iconserver") val iconServer: String,
    @SerializedName("iconfarm") val iconFarm: Int,
    @SerializedName("path_alias") val pathAlias: String,
)

data class Content(
    @SerializedName("_content") val content: String
)

data class Dates(
    @SerializedName("posted") val posted: String,
    @SerializedName("taken") val taken: String,
    @SerializedName("lastupdate") val lastUpdate: String
)

data class Tags(
    @SerializedName("tag") val tag: List<Tag>
)

data class Tag (
    @SerializedName("raw") val raw: String,
)
