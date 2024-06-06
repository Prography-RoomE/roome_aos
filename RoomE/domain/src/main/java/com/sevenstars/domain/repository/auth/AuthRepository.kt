package com.sevenstars.domain.repository.auth

import com.sevenstars.domain.model.auth.RequestSignInEntity
import com.sevenstars.domain.model.auth.ResponseSignInEntity
import com.sevenstars.domain.utils.RoomeResult

interface AuthRepository {
    suspend fun signIn(
        body: RequestSignInEntity
    ): RoomeResult<ResponseSignInEntity>

    suspend fun unlink(
        accessToken: String,
        provider: String,
        code: String?
    ): RoomeResult<Boolean>

    suspend fun signOut(
        accessToken: String,
    ): RoomeResult<Boolean>

    suspend fun refreshToken(
        refreshToken: String
    ): RoomeResult<ResponseSignInEntity>
}