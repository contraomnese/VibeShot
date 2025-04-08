package com.arbuzerxxl.vibeshot.data.storage.api

import com.arbuzerxxl.vibeshot.data.storage.models.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserStorage {

    val user: Flow<UserEntity?>

    suspend fun save(user: UserEntity)

    suspend fun saveAsGuest()

    suspend fun delete()
}