package com.arbuzerxxl.vibeshot.domain.repository

import com.arbuzerxxl.vibeshot.domain.models.AccessTokenDomain
import com.arbuzerxxl.vibeshot.domain.models.RequestTokenDomain

interface AuthRepository {

    suspend fun getRequestToken(): RequestTokenDomain
    fun getAuthorizeUrl(requestToken: String): String
    suspend fun getAccessToken(requestToken: String, verifier: String, secretToken: String): AccessTokenDomain

}