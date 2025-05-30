package com.arbuzerxxl.vibeshot.data.network.api

import com.arbuzerxxl.vibeshot.data.network.model.response.photos.PhotoExifResponse
import com.arbuzerxxl.vibeshot.data.network.model.response.photos.PhotoInfoResponse
import com.arbuzerxxl.vibeshot.data.network.model.response.photos.PhotoSizesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrPhotoApi {

    @GET("rest/?method=flickr.photos.getExif")
    suspend fun getExif(
        @Query("api_key") key: String,
        @Query("format") format: String = "json",
        @Query("nojsoncallback") noJsonCallback: String = "1",
        @Query("photo_id") photoId: String,
        @Query("secret") secret: String? = null,
    ) : PhotoExifResponse

    @GET("rest/?method=flickr.photos.getInfo")
    suspend fun getInfo(
        @Query("api_key") key: String,
        @Query("format") format: String = "json",
        @Query("nojsoncallback") noJsonCallback: String = "1",
        @Query("photo_id") photoId: String,
        @Query("secret") secret: String? = null,
    ): PhotoInfoResponse

    @GET("rest/?method=flickr.photos.getSizes")
    suspend fun getSizes(
        @Query("api_key") key: String,
        @Query("format") format: String = "json",
        @Query("nojsoncallback") noJsonCallback: String = "1",
        @Query("photo_id") id: String,
    ): PhotoSizesResponse

}