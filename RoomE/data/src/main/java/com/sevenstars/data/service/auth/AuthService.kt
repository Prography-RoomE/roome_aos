package com.sevenstars.data.service.auth

import com.sevenstars.data.model.BaseResponse
import com.sevenstars.data.model.auth.RequestSignInDTO
import com.sevenstars.data.model.auth.ResponseSignInDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/signin")
    suspend fun signIn(
        @Body body: RequestSignInDTO
    ): Response<BaseResponse<ResponseSignInDTO>>
}