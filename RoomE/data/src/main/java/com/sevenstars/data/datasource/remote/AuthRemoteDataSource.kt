package com.sevenstars.data.datasource.remote

import com.sevenstars.data.model.BaseResponse
import com.sevenstars.data.model.auth.RequestRefreshTokenDTO
import com.sevenstars.data.model.auth.RequestSignInDTO
import com.sevenstars.data.model.auth.RequestUnlinkDTO
import com.sevenstars.data.model.auth.ResponseSignInDTO
import okhttp3.ResponseBody

interface AuthRemoteDataSource {
    suspend fun signIn(body: RequestSignInDTO): BaseResponse<ResponseSignInDTO>

    suspend fun unlink(
        accessToken: String,
        body: RequestUnlinkDTO
    ): BaseResponse<ResponseBody>

    suspend fun signOut(
        accessToken: String,
    ): BaseResponse<ResponseBody>

    suspend fun refreshToken(
       body: RequestRefreshTokenDTO
    ): BaseResponse<ResponseSignInDTO>
}