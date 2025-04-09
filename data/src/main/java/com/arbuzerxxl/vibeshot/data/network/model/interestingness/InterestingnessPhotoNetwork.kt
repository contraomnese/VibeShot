package com.arbuzerxxl.vibeshot.data.network.model.interestingness

import com.google.gson.annotations.SerializedName

data class InterestingnessPhotoNetwork(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: String,
    val title: String,
    @SerializedName("ispublic") val isPublic: Boolean,
    @SerializedName("isfriend") val isFriend: Boolean,
    @SerializedName("isfamily") val isFamily: Boolean,

    val license: String?,
    @SerializedName("dateupload") val dateUpload: String?,
    @SerializedName("lastupdate") val lastUpdate: String?,
    @SerializedName("datetaken") val dateTaken: String?,
    @SerializedName("datetakengranularity") val dateTakenGranularity: String?,
    @SerializedName("datetakenunknown") val dateTakenUnknown: String?,
    @SerializedName("ownername") val ownerName: String?,
    @SerializedName("iconserver") val iconServer: String?,
    @SerializedName("iconfarm") val iconFarm: String?,
    val views: String?,
    val tags: String?,
    val latitude: String?,
    val longitude: String?,
    val accuracy: String?,
    val context: String?,
    @SerializedName("place_id") val placeId: String?,
    @SerializedName("woeid") val woeId: String?,
    @SerializedName("geo_is_public") val geoIsPublic: Boolean?,
    @SerializedName("geo_is_contact") val geoIsContact: Boolean?,
    @SerializedName("geo_is_friend") val geoIsFriend: Boolean?,
    @SerializedName("geo_is_family") val geoIsFamily: Boolean?,
    val media: String?,
    @SerializedName("media_status") val mediaStatus: String?,
    @SerializedName("pathalias") val pathAlias: String?,
    val description: PhotoDescriptionNetwork?,

    @SerializedName("height_sq")
    val heightSQ: String?,
    @SerializedName("width_sq")
    val widthSQ: String?,
    @SerializedName("url_sq")
    val urlSQ: String?,

    @SerializedName("height_t")
    val heightT: String?,
    @SerializedName("width_t")
    val widthT: String?,
    @SerializedName("url_t")
    val urlT: String?,

    @SerializedName("height_s")
    val heightS: String?,
    @SerializedName("width_s")
    val widthS: String?,
    @SerializedName("url_s")
    val urlS: String?,

    @SerializedName("height_q")
    val heightQ: String?,
    @SerializedName("width_q")
    val widthQ: String?,
    @SerializedName("url_q")
    val urlQ: String?,

    @SerializedName("height_m")
    val heightM: String?,
    @SerializedName("width_m")
    val widthM: String?,
    @SerializedName("url_m")
    val urlM: String?,

    @SerializedName("height_n")
    val heightN: String?,
    @SerializedName("width_n")
    val widthN: String?,
    @SerializedName("url_n")
    val urlN: String?,

    @SerializedName("height_z")
    val heightZ: String?,
    @SerializedName("width_z")
    val widthZ: String?,
    @SerializedName("url_z")
    val urlZ: String?,

    @SerializedName("height_c")
    val heightC: String?,
    @SerializedName("width_c")
    val widthC: String?,
    @SerializedName("url_c")
    val urlC: String?,

    @SerializedName("height_l")
    val heightL: String?,
    @SerializedName("width_l")
    val widthL: String?,
    @SerializedName("url_l")
    val urlL: String?,

    @SerializedName("height_o")
    val heightO: String?,
    @SerializedName("width_o")
    val widthO: String?,
    @SerializedName("url_o")
    val urlO: String?,

)