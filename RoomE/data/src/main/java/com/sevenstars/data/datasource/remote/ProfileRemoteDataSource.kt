package com.sevenstars.data.datasource.remote

import com.sevenstars.data.model.BaseResponse
import com.sevenstars.data.model.profile.RequestMbtiDTO
import com.sevenstars.data.model.profile.RequestRoomCountDTO
import com.sevenstars.data.model.profile.RequestRoomCountRangeDTO
import com.sevenstars.data.model.profile.RequestSaveIdsDTO
import com.sevenstars.data.model.profile.ResponseProfileDTO
import com.sevenstars.data.model.profile.info.RequestSaveIdDTO
import com.sevenstars.data.model.profile.info.ResponseProfileInfoDTO

interface ProfileRemoteDataSource {
    suspend fun getDefaultProfileData(token: String): BaseResponse<ResponseProfileInfoDTO>

    suspend fun getProfileData(
        accessToken: String
    ): BaseResponse<ResponseProfileDTO>

    suspend fun deleteProfileData(
        accessToken: String
    ): BaseResponse<Boolean>

    suspend fun saveUserStrengths(
        accessToken: String,
        body: RequestSaveIdsDTO
    ): BaseResponse<Boolean>

    suspend fun saveUserImportantFactors(
        accessToken: String,
        body: RequestSaveIdsDTO
    ): BaseResponse<Boolean>

    suspend fun saveUserDislikeFactors(
        accessToken: String,
        body: RequestSaveIdsDTO
    ): BaseResponse<Boolean>

    suspend fun saveUserRoomCount(
        accessToken: String,
        body: RequestRoomCountDTO
    ): BaseResponse<Boolean>

    suspend fun saveUserRoomCountRange(
        accessToken: String,
        body: RequestRoomCountRangeDTO
    ): BaseResponse<Boolean>

    suspend fun saveUserPreferredGenres(
        accessToken: String,
        body: RequestSaveIdsDTO
    ): BaseResponse<Boolean>

    suspend fun saveUserMbti(
        accessToken: String,
        body: RequestMbtiDTO
    ): BaseResponse<Boolean>

    suspend fun saveUserHorrorThemePos(
        accessToken: String,
        body: RequestSaveIdDTO
    ): BaseResponse<Boolean>

    suspend fun saveUserHintUsagePreference(
        accessToken: String,
        body: RequestSaveIdDTO
    ): BaseResponse<Boolean>

    suspend fun saveUserDeviceLockPreference(
        accessToken: String,
        body: RequestSaveIdDTO
    ): BaseResponse<Boolean>

    suspend fun saveUserColor(
        accessToken: String,
        body: RequestSaveIdDTO
    ): BaseResponse<Boolean>

    suspend fun saveUserActivity(
        accessToken: String,
        body: RequestSaveIdDTO
    ): BaseResponse<Boolean>
}