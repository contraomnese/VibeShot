package com.arbuzerxxl.vibeshot.domain.usecases.auth

import com.arbuzerxxl.vibeshot.domain.models.auth.AuthState
import com.arbuzerxxl.vibeshot.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class ObserveAuthStateUseCase(
    private val repository: AuthRepository
) {
    operator fun invoke(): Flow<AuthState> = repository.authState
}