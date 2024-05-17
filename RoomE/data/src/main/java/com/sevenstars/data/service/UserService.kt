package com.sevenstars.data.service

import com.sevenstars.data.model.BaseResponse
import com.sevenstars.data.model.user.ResponseUserInfoDTO
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface UserService {
    @GET("/users")
    suspend fun getUserInfo(
        @Header("Authorization") accessToken: String
    ): Response<BaseResponse<ResponseUserInfoDTO>>

    @POST("/users/nickname/validation")
    suspend fun validationNick(
        @Header("Authorization") accessToken: String,
        @Query("nickname") nickname: String
    ): Response<BaseResponse<ResponseBody>>

    @PUT("/users/nickname")
    suspend fun saveNick(
        @Header("Authorization") accessToken: String,
        @Query("nickname") nickname: String
    ): Response<BaseResponse<ResponseBody>>

    @PUT("/users/terms-agreement")
    suspend fun saveTermsAgreement(
        @Header("Authorization") accessToken: String,
        @QueryMap options: Map<String, Boolean>
    ): Response<BaseResponse<ResponseBody>>
}