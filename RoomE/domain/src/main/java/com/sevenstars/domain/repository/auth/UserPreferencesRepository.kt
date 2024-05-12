package com.sevenstars.domain.repository.auth

import com.sevenstars.domain.enums.Provider

interface UserPreferencesRepository {
    suspend fun setAccessToken(
        accessToken: String
    )

    suspend fun getAccessToken(): Result<String>

    suspend fun setRefreshToken(
        refreshToken: String
    )

    suspend fun getRefreshToken(): Result<String>

    suspend fun setLoginProvider(
        provider: Provider
    )

    suspend fun getLoginProvider(): Result<String>
}