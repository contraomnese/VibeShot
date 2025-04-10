package com.arbuzerxxl.vibeshot.data.network.model.interestingness

import com.google.gson.annotations.SerializedName

data class InterestsPhotoNetwork(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String,
    @SerializedName("ispublic") val isPublic: Int,
    @SerializedName("isfriend") val isFriend: Int,
    @SerializedName("isfamily") val isFamily: Int,

    val license: String?,
    @SerializedName("dateupload") val dateUpload: String?,
    @SerializedName("lastupdate") val lastUpdate: String?,
    @SerializedName("datetaken") val dateTaken: String?,
    @SerializedName("datetakengranularity") val dateTakenGranularity: Int?,
    @SerializedName("datetakenunknown") val dateTakenUnknown: String?,
    @SerializedName("ownername") val ownerName: String?,
    @SerializedName("iconserver") val iconServer: String?,
    @SerializedName("iconfarm") val iconFarm: Int?,
    val views: String?,
    val tags: String?,
    val latitude: String?,
    val longitude: String?,
    val accuracy: String?,
    val context: Int?,
    @SerializedName("place_id") val placeId: String?,
    @SerializedName("woeid") val woeId: String?,
    @SerializedName("geo_is_public") val geoIsPublic: Int?,
    @SerializedName("geo_is_contact") val geoIsContact: Int?,
    @SerializedName("geo_is_friend") val geoIsFriend: Int?,
    @SerializedName("geo_is_family") val geoIsFamily: Int?,
    val media: String?,
    @SerializedName("media_status") val mediaStatus: String?,
    @SerializedName("pathalias") val pathAlias: String?,
    val description: PhotoDescriptionNetwork?,

    @SerializedName("height_sq")
    val heightSQ: Int?,
    @SerializedName("width_sq")
    val widthSQ: Int?,
    @SerializedName("url_sq")
    val urlSQ: String?,

    @SerializedName("height_t")
    val heightT: Int?,
    @SerializedName("width_t")
    val widthT: Int?,
    @SerializedName("url_t")
    val urlT: String?,

    @SerializedName("height_s")
    val heightS: Int?,
    @SerializedName("width_s")
    val widthS: Int?,
    @SerializedName("url_s")
    val urlS: String?,

    @SerializedName("height_q")
    val heightQ: Int?,
    @SerializedName("width_q")
    val widthQ: Int?,
    @SerializedName("url_q")
    val urlQ: String?,

    @SerializedName("height_m")
    val heightM: Int?,
    @SerializedName("width_m")
    val widthM: Int?,
    @SerializedName("url_m")
    val urlM: String?,

    @SerializedName("height_n")
    val heightN: Int?,
    @SerializedName("width_n")
    val widthN: Int?,
    @SerializedName("url_n")
    val urlN: String?,

    @SerializedName("height_z")
    val heightZ: Int?,
    @SerializedName("width_z")
    val widthZ: Int?,
    @SerializedName("url_z")
    val urlZ: String?,

    @SerializedName("height_c")
    val heightC: Int?,
    @SerializedName("width_c")
    val widthC: Int?,
    @SerializedName("url_c")
    val urlC: String?,

    @SerializedName("height_l")
    val heightL: Int?,
    @SerializedName("width_l")
    val widthL: Int?,
    @SerializedName("url_l")
    val urlL: String?,

    @SerializedName("height_o")
    val heightO: Int?,
    @SerializedName("width_o")
    val widthO: Int?,
    @SerializedName("url_o")
    val urlO: String?,

)