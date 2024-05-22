package com.sevenstars.data.service

import com.sevenstars.data.model.BaseResponse
import com.sevenstars.data.model.user.RequestNickDTO
import com.sevenstars.data.model.user.RequestTermsDTO
import com.sevenstars.data.model.user.ResponseUserInfoDTO
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

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
}