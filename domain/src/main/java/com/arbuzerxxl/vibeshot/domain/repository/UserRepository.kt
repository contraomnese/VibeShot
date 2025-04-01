package com.arbuzerxxl.vibeshot.domain.repository

import com.arbuzerxxl.vibeshot.domain.models.auth.AuthState
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun observe(): Flow<AuthState>

    suspend fun saveUser(verifier: String)

    suspend fun saveUserAsGuest()

    suspend fun delete()

}