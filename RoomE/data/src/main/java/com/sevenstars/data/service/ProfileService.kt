package com.sevenstars.data.service

import com.sevenstars.data.model.BaseResponse
import com.sevenstars.data.model.profile.RequestMbtiDTO
import com.sevenstars.data.model.profile.RequestRoomCountDTO
import com.sevenstars.data.model.profile.RequestSaveIdsDTO
import com.sevenstars.data.model.profile.ResponseProfileDTO
import com.sevenstars.data.model.profile.info.RequestSaveIdDTO
import com.sevenstars.data.model.profile.info.ResponseProfileInfoDTO
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT

interface ProfileService {
    @GET("/profiles/defaults")
    suspend fun getDefaultProfileData(
        @Header("Authorization") accessToken: String
    ): Response<BaseResponse<ResponseProfileInfoDTO>>

    @GET("/profiles")
    suspend fun getProfileData(
        @Header("Authorization") accessToken: String
    ): Response<BaseResponse<ResponseProfileDTO>>

    @DELETE("/profiles")
    suspend fun deleteProfileData(
        @Header("Authorization") accessToken: String
    ): Response<BaseResponse<ResponseBody>>

    @PUT("/profiles/user-strengths")
    suspend fun saveUserStrengths(
        @Header("Authorization") accessToken: String,
        @Body body: RequestSaveIdsDTO
    ): Response<BaseResponse<ResponseBody>>

    @PUT("/profiles/theme-important-factors")
    suspend fun saveUserImportantFactors(
        @Header("Authorization") accessToken: String,
        @Body body: RequestSaveIdsDTO
    ): Response<BaseResponse<ResponseBody>>

    @PUT("/profiles/theme-disliked-factors")
    suspend fun saveUserDislikeFactors(
        @Header("Authorization") accessToken: String,
        @Body body: RequestSaveIdsDTO
    ): Response<BaseResponse<ResponseBody>>

    @PUT("/profiles/room-count")
    suspend fun saveUserRoomCount(
        @Header("Authorization") accessToken: String,
        @Body body: RequestRoomCountDTO
    ): Response<BaseResponse<ResponseBody>>

    @PUT("/profiles/preferred-genres")
    suspend fun saveUserPreferredGenres(
        @Header("Authorization") accessToken: String,
        @Body body: RequestSaveIdsDTO
    ): Response<BaseResponse<ResponseBody>>

    @PUT("/profiles/mbti")
    suspend fun saveUserMbti(
        @Header("Authorization") accessToken: String,
        @Body body: RequestMbtiDTO
    ): Response<BaseResponse<ResponseBody>>

    @PUT("/profiles/horror-theme-position")
    suspend fun saveUserHorrorThemePos(
        @Header("Authorization") accessToken: String,
        @Body body: RequestSaveIdDTO
    ): Response<BaseResponse<ResponseBody>>

    @PUT("/profiles/hint-usage-preference")
    suspend fun saveUserHintUsagePreference(
        @Header("Authorization") accessToken: String,
        @Body body: RequestSaveIdDTO
    ): Response<BaseResponse<ResponseBody>>

    @PUT("/profiles/device-lock-preference")
    suspend fun saveUserDeviceLockPreference(
        @Header("Authorization") accessToken: String,
        @Body body: RequestSaveIdDTO
    ): Response<BaseResponse<ResponseBody>>

    @PUT("/profiles/color")
    suspend fun saveUserColor(
        @Header("Authorization") accessToken: String,
        @Body body: RequestSaveIdDTO
    ): Response<BaseResponse<ResponseBody>>

    @PUT("/profiles/activity")
    suspend fun saveUserActivity(
        @Header("Authorization") accessToken: String,
        @Body body: RequestSaveIdDTO
    ): Response<BaseResponse<ResponseBody>>
}