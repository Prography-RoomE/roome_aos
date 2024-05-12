package com.sevenstars.data.service

import com.sevenstars.data.model.BaseResponse
import com.sevenstars.data.model.user.ResponseUserInfoDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface UserService {
    @GET("/users")
    suspend fun getUserInfo(
        @Header("Authorization") accessToken: String
    ): Response<BaseResponse<ResponseUserInfoDTO>>
}