package com.arbuzerxxl.vibeshot.data.network.api

import com.arbuzerxxl.vibeshot.data.network.model.response.upload.FlickrUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FlickrUploadPhotoApi {

    @Multipart
    @POST("services/upload/")
    suspend fun uploadPhoto(
        @Header("Authorization") authorizationHeader: String,
        @Part photo: MultipartBody.Part,
        @Part("title") title: RequestBody
    ): FlickrUploadResponse
}