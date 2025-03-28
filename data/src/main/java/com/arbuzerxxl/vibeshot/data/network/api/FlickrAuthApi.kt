package com.arbuzerxxl.vibeshot.data.network.api

import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrAuthApi {

    @GET("oauth/request_token")
    suspend fun getRequestToken(
        @Query("oauth_nonce") nonce: String,
        @Query("oauth_timestamp") timestamp: String,
        @Query("oauth_consumer_key") consumerKey: String,
        @Query("oauth_signature_method") method: String,
        @Query("oauth_version") oauthVersion: String,
        @Query("oauth_signature") signature: String,
        @Query("oauth_callback") callback: String,
    ): String

    @GET("oauth/access_token")
    suspend fun getAccessToken(
        @Query("oauth_nonce") nonce: String,
        @Query("oauth_timestamp") timestamp: String,
        @Query("oauth_consumer_key") consumerKey: String,
        @Query("oauth_signature_method") method: String,
        @Query("oauth_version") oauthVersion: String,
        @Query("oauth_signature") signature: String,
        @Query("oauth_token") token: String,
        @Query("oauth_verifier") verifier: String,
    ): String
}