package com.arbuzerxxl.vibeshot.data.network.api

import com.arbuzerxxl.vibeshot.data.sources.model.SearchNetworkResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrSearchApi {

    @GET("rest/?method=flickr.photos.search")
    suspend fun searchByQuery(
        @Query("api_key") key: String,
        @Query("format") format: String = "json",
        @Query("nojsoncallback") noJsonCallback: String = "1",
        @Query("text") query: String,
        @Query("extras") extras: String? = null,
        @Query("per_page") pageSize: Int? = null,
        @Query("page") page: Int? = null,
    ): SearchNetworkResponse
}