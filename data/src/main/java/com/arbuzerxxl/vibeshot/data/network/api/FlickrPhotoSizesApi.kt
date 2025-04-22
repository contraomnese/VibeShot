package com.arbuzerxxl.vibeshot.data.network.api

import com.arbuzerxxl.vibeshot.data.network.model.photos.PhotoSizesNetworkResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrPhotoSizesApi {

    @GET("rest/?method=flickr.photos.getSizes")
    suspend fun getSizes(
        @Query("api_key") key: String,
        @Query("format") format: String = "json",
        @Query("nojsoncallback") noJsonCallback: String = "1",
        @Query("photo_id") id: String,
    ): PhotoSizesNetworkResponse
}
