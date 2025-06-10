package com.arbuzerxxl.vibeshot.data.network.api

import com.arbuzerxxl.vibeshot.data.network.model.response.search.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

private val EXTRAS = listOf<String>("url_s", "url_m", "url_l")

interface FlickrSearchApi {

    @GET("rest/?method=flickr.photos.search")
    suspend fun searchByText(
        @Query("api_key") key: String,
        @Query("format") format: String = "json",
        @Query("nojsoncallback") noJsonCallback: String = "1",
        @Query("text") text: String,
        @Query("extras") extras: String? = EXTRAS.joinToString(),
        @Query("per_page") pageSize: Int? = null,
        @Query("page") page: Int? = null,
    ): SearchResponse

    @GET("rest/?method=flickr.photos.search")
    suspend fun searchByTag(
        @Query("api_key") key: String,
        @Query("format") format: String = "json",
        @Query("nojsoncallback") noJsonCallback: String = "1",
        @Query("tags") tag: String,
        @Query("extras") extras: String? = EXTRAS.joinToString(),
        @Query("per_page") pageSize: Int? = null,
        @Query("page") page: Int? = null,
    ): SearchResponse
}
