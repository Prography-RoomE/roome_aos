package com.sevenstars.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.domain.enums.Provider
import com.sevenstars.domain.repository.auth.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val userDataStorePreferences: DataStore<Preferences>
): UserPreferencesRepository {

    private companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val LOGIN_PROVIDER_KEY = stringPreferencesKey("login_provider")
    }

    override suspend fun clearData(): Result<Boolean> {
        LoggerUtils.debug("clearData 호출")

        return try {
            userDataStorePreferences.edit { preferences ->
                preferences.clear()
            }
            Result.success(true)
        } catch (e: Exception){
            Result.success(false)
        }
    }

    override suspend fun setAccessToken(accessToken: String) {
        LoggerUtils.debug("setAccessToken 호출")

        userDataStorePreferences.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
        }
    }

    override suspend fun getAccessToken(): Result<String> {
        LoggerUtils.debug("getAccessToken 호출")

        return try {
            val accessToken = userDataStorePreferences.data.map { preferences ->
                preferences[ACCESS_TOKEN_KEY] ?: ""
            }.first()
            Result.success(accessToken)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun setRefreshToken(refreshToken: String) {
        LoggerUtils.debug("setRefreshToken 호출")

        userDataStorePreferences.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    override suspend fun getRefreshToken(): Result<String> {
        LoggerUtils.debug("getRefreshToken 호출")

        return try {
            val refreshToken = userDataStorePreferences.data.map { preferences ->
                preferences[REFRESH_TOKEN_KEY] ?: ""
            }.first()
            Result.success(refreshToken)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun setLoginProvider(provider: Provider) {
        LoggerUtils.debug("setLoginProvider 호출")

        userDataStorePreferences.edit { preferences ->
            preferences[LOGIN_PROVIDER_KEY] = provider.provider
        }
    }

    override suspend fun getLoginProvider(): Result<String> {
        LoggerUtils.debug("getLoginProvider 호출")

        return try {
            val provider = userDataStorePreferences.data.map { preferences ->
                preferences[LOGIN_PROVIDER_KEY] ?: ""
            }.first()
            Result.success(provider)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
