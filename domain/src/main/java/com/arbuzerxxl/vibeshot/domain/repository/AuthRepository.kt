package com.arbuzerxxl.vibeshot.domain.repository

import com.arbuzerxxl.vibeshot.domain.models.AccessToken
import com.arbuzerxxl.vibeshot.domain.models.RequestTokenDomain
import com.arbuzerxxl.vibeshot.domain.models.Verifier

interface AuthRepository {

    suspend fun getRequestToken(): RequestTokenDomain
    suspend fun authorize(): Verifier
    suspend fun getAccessToken(): AccessToken

}