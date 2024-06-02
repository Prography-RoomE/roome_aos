package com.sevenstars.domain.repository

import com.sevenstars.domain.model.profile.ProfileInfoEntity
import com.sevenstars.domain.model.profile.SavedProfileData
import com.sevenstars.domain.utils.RoomeResult

interface ProfileRepository {
    suspend fun getDefaultProfileData(
        accessToken: String
    ): RoomeResult<ProfileInfoEntity>

    suspend fun getProfileData(
        accessToken: String
    ): RoomeResult<SavedProfileData>

    suspend fun deleteProfileData(
        accessToken: String
    ): RoomeResult<Boolean>

    suspend fun saveUserStrengths(
        accessToken: String,
        body: List<Int>
    ): RoomeResult<Boolean>

    suspend fun saveUserImportantFactors(
        accessToken: String,
        body: List<Int>
    ): RoomeResult<Boolean>

    suspend fun saveUserDislikeFactors(
        accessToken: String,
        body: List<Int>
    ): RoomeResult<Boolean>

    suspend fun saveUserRoomCount(
        accessToken: String,
        count: Int,
        isPlusEnabled: Boolean
    ): RoomeResult<Boolean>

    suspend fun saveUserRoomCountRange(
        accessToken: String,
        minCount: Int,
        maxCount: Int
    ): RoomeResult<Boolean>

    suspend fun saveUserPreferredGenres(
        accessToken: String,
        body: List<Int>
    ): RoomeResult<Boolean>

    suspend fun saveUserMbti(
        accessToken: String,
        body: String
    ): RoomeResult<Boolean>

    suspend fun saveUserHorrorThemePos(
        accessToken: String,
        body: Int
    ): RoomeResult<Boolean>

    suspend fun saveUserHintUsagePreference(
        accessToken: String,
        body: Int
    ): RoomeResult<Boolean>

    suspend fun saveUserDeviceLockPreference(
        accessToken: String,
        body: Int
    ): RoomeResult<Boolean>

    suspend fun saveUserColor(
        accessToken: String,
        body: Int
    ): RoomeResult<Boolean>

    suspend fun saveUserActivity(
        accessToken: String,
        body: Int
    ): RoomeResult<Boolean>
}