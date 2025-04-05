package com.arbuzerxxl.vibeshot.domain.repository

import com.arbuzerxxl.vibeshot.domain.models.auth.AuthState
import com.arbuzerxxl.vibeshot.domain.models.auth.tokens.RequestToken
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signIn(verifier: String)
    suspend fun signInAsGuest()
    suspend fun signOut()

    val authState: Flow<AuthState>
    suspend fun getAuthUrl(): String
}