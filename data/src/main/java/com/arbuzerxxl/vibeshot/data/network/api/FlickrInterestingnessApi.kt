package com.arbuzerxxl.vibeshot.data.network.api

import com.arbuzerxxl.vibeshot.data.network.model.interestingness.InterestingnessNetworkResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrInterestingnessApi {

    @GET("rest/?method=flickr.interestingness.getList")
    suspend fun getPhotos(
        @Query("api_key") key: String,
        @Query("date") date: String? = null,
        @Query("extras") extras: String? = null,
        @Query("per_page") perPage: String? = null,
        @Query("page") page: String? = null,
    ): InterestingnessNetworkResponse

}