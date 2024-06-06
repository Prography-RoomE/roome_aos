package com.sevenstars.data.service.auth

import com.sevenstars.data.model.BaseResponse
import com.sevenstars.data.model.auth.RequestRefreshTokenDTO
import com.sevenstars.data.model.auth.RequestSignInDTO
import com.sevenstars.data.model.auth.RequestUnlinkDTO
import com.sevenstars.data.model.auth.ResponseSignInDTO
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("/signin")
    suspend fun signIn(
        @Body body: RequestSignInDTO
    ): Response<BaseResponse<ResponseSignInDTO>>

    @POST("/withdrawal")
    suspend fun unlink(
        @Header("Authorization") accessToken: String,
        @Body body: RequestUnlinkDTO
    ): Response<BaseResponse<ResponseBody>>

    @POST("/signout")
    suspend fun signOut(
        @Header("Authorization") accessToken: String,
    ): Response<BaseResponse<ResponseBody>>

    @POST("/token")
    suspend fun refreshToken(
        @Body body: RequestRefreshTokenDTO
    ): Response<BaseResponse<ResponseSignInDTO>>
}