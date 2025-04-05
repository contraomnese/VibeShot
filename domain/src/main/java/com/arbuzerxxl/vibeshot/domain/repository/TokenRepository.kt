package com.arbuzerxxl.vibeshot.domain.repository

import com.arbuzerxxl.vibeshot.domain.models.auth.User

interface TokenRepository {

    suspend fun getUserBy(verifier: String): User
    suspend fun getAuthorizeUrlAndSaveRequestToken(): String
}