package com.sevenstars.data.repository.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.sevenstars.domain.model.auth.Provider
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

    override suspend fun setAccessToken(accessToken: String) {
        userDataStorePreferences.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
        }
    }

    override suspend fun getAccessToken(): Result<String> {
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
        userDataStorePreferences.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    override suspend fun getRefreshToken(): Result<String> {
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
        userDataStorePreferences.edit { preferences ->
            preferences[LOGIN_PROVIDER_KEY] = provider.toString()
        }
    }

    override suspend fun getLoginProvider(): Result<String> {
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
