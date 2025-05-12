package com.arbuzerxxl.vibeshot.data.network.api

import com.arbuzerxxl.vibeshot.data.network.model.interests.InterestsResponse
import retrofit2.http.GET
import retrofit2.http.Query

private val EXTRAS = listOf<String>("url_s", "url_m", "url_l")

interface FlickrInterestsApi {

    @GET("rest/?method=flickr.interestingness.getList")
    suspend fun getPhotos(
        @Query("api_key") key: String,
        @Query("format") format: String = "json",
        @Query("nojsoncallback") noJsonCallback: String = "1",
        @Query("date") date: String? = null,
        @Query("extras") extras: String? = EXTRAS.joinToString(),
        @Query("per_page") pageSize: Int? = null,
        @Query("page") page: Int? = null,
    ): InterestsResponse
}
