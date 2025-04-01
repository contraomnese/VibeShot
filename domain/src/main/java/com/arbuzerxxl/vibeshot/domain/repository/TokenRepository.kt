package com.arbuzerxxl.vibeshot.domain.repository

import com.arbuzerxxl.vibeshot.domain.models.responses.AccessTokenResponse

interface TokenRepository {

    suspend fun getAccessToken(verifier: String): AccessTokenResponse
    suspend fun getAuthorizeUrl(): String
}