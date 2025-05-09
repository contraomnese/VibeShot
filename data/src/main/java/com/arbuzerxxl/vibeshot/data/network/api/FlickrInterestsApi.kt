package com.arbuzerxxl.vibeshot.data.network.api

import com.arbuzerxxl.vibeshot.data.network.model.interestingness.InterestsNetworkResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrInterestsApi {

    @GET("rest/?method=flickr.interestingness.getList")
    suspend fun getPhotos(
        @Query("api_key") key: String,
        @Query("format") format: String = "json",
        @Query("nojsoncallback") noJsonCallback: String = "1",
        @Query("date") date: String? = null,
        @Query("extras") extras: String? = null,
        @Query("per_page") pageSize: Int? = null,
        @Query("page") page: Int? = null,
    ): InterestsNetworkResponse
}
