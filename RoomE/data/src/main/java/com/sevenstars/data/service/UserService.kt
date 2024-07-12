package com.sevenstars.data.service

import com.sevenstars.data.model.BaseResponse
import com.sevenstars.data.model.profile.ResponseProfileDTO
import com.sevenstars.data.model.user.RequestNickDTO
import com.sevenstars.data.model.user.RequestTermsDTO
import com.sevenstars.data.model.user.ResponsePostImageDTO
import com.sevenstars.data.model.user.ResponseUserInfoDTO
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface UserService {
    @GET("/users")
    suspend fun getUserInfo(
        @Header("Authorization") accessToken: String
    ): Response<BaseResponse<ResponseUserInfoDTO>>

    @POST("/users/nickname/validation")
    suspend fun validationNick(
        @Header("Authorization") accessToken: String,
        @Body nickname: RequestNickDTO
    ): Response<BaseResponse<ResponseBody>>

    @PUT("/users/nickname")
    suspend fun saveNick(
        @Header("Authorization") accessToken: String,
        @Body nickname: RequestNickDTO
    ): Response<BaseResponse<ResponseBody>>

    @PUT("/users/terms-agreement")
    suspend fun saveTermsAgreement(
        @Header("Authorization") accessToken: String,
        @Body options: RequestTermsDTO
    ): Response<BaseResponse<ResponseBody>>

    @GET("/users/profile")
    suspend fun getUserProfile(
        @Query("nickname") nickname: String
    ): Response<BaseResponse<ResponseProfileDTO>>

    @Multipart
    @POST("/users/image")
    suspend fun postProfileImage(
        @Header("Authorization") accessToken: String,
        @Part image: MultipartBody.Part
    ): Response<BaseResponse<ResponsePostImageDTO>>

    @DELETE("/users/image")
    suspend fun deleteProfileImage(
        @Header("Authorization") accessToken: String
    ): Response<BaseResponse<ResponseBody>>
}