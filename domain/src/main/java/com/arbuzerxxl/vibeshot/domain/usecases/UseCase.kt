package com.arbuzerxxl.vibeshot.domain.usecases

interface UseCaseWithParams <R, P> {
    suspend fun execute(params: P) : R
}

interface UseCaseWithoutParams <R> {
    suspend fun execute() : R
}

