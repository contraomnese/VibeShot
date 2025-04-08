package com.arbuzerxxl.vibeshot.domain.models.auth

sealed interface AuthState {
    data object Unauthenticated : AuthState
    data class Guest(val user: User) : AuthState
    data class Authenticated(val user: User) : AuthState
}