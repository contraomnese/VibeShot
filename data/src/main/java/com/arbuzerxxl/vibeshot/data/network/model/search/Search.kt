package com.arbuzerxxl.vibeshot.data.network.model.search

import com.arbuzerxxl.vibeshot.data.adapters.SearchResponseTypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName

@JsonAdapter(SearchResponseTypeAdapter::class)
sealed class SearchResponse {
    data class Success(val photos: SearchPhotos) : SearchResponse()
    data class Error(val code: Int, val message: String) : SearchResponse()
}

data class SearchPhotos(
    @SerializedName("page") val page: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("perpage") val perpage: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("photo") val photo: List<SearchPhoto>,
)

data class SearchPhoto(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String,
    @SerializedName("ispublic") val isPublic: Int,
    @SerializedName("isfriend") val isFriend: Int,
    @SerializedName("isfamily") val isFamily: Int,

    // with extras
    @SerializedName("url_s") val urlS: String,
    @SerializedName("height_s") val heightS: Int,
    @SerializedName("width_s") val widthS: Int,

    @SerializedName("url_m") val urlM: String? = null,
    @SerializedName("height_m") val heightM: Int? = null,
    @SerializedName("width_m") val widthM: Int? = null,

    @SerializedName("url_l") val urlL: String? = null,
    @SerializedName("height_l") val heightL: Int? = null,
    @SerializedName("width_l") val widthL: Int? = null,
)
