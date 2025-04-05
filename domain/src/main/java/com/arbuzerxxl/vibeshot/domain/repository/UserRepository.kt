package com.arbuzerxxl.vibeshot.domain.repository

import com.arbuzerxxl.vibeshot.domain.models.auth.AuthState
import com.arbuzerxxl.vibeshot.domain.models.auth.User
import com.arbuzerxxl.vibeshot.domain.models.auth.tokens.AccessToken
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun observe(): Flow<AuthState>

    suspend fun getBy(verifier: String): User

    suspend fun save(user: User)

    suspend fun saveAsGuest()

    suspend fun delete()

}