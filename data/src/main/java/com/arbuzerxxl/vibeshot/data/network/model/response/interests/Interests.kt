package com.arbuzerxxl.vibeshot.data.network.model.response.interests

import com.arbuzerxxl.vibeshot.data.adapters.InterestsResponseTypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName

@JsonAdapter(InterestsResponseTypeAdapter::class)
sealed class InterestsResponse {
    data class Success(val photos: InterestsPhotos) : InterestsResponse()
    data class Error(val code: Int, val message: String) : InterestsResponse()
}

data class InterestsPhotos(
    @SerializedName("page") val page: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("perpage") val perpage: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("photo") val photo: List<InterestPhoto>,
)

data class InterestPhoto(
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
