package com.sevenstars.domain.repository.auth

import com.sevenstars.domain.model.auth.RequestSignInEntity
import com.sevenstars.domain.model.auth.ResponseSignInEntity

interface AuthRepository {
    suspend fun signIn(
        body: RequestSignInEntity
    ): Result<ResponseSignInEntity>
}