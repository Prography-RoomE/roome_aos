package com.sevenstars.data.service

import com.sevenstars.data.model.BaseResponse
import com.sevenstars.data.model.profile.ResponseProfileInfoDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ProfileService {
    @GET("/profiles/defaults")
    suspend fun getDefaultProfileData(
        @Header("Authorization") accessToken: String
    ): Response<BaseResponse<ResponseProfileInfoDTO>>
}