package com.sevenstars.data.datasource.remote

import com.sevenstars.data.model.BaseResponse
import com.sevenstars.data.model.user.ResponseUserInfoDTO
import okhttp3.ResponseBody

interface UserRemoteDataSource {
    suspend fun getUserInfo(token: String): BaseResponse<ResponseUserInfoDTO>

    suspend fun validationNick(accessToken: String, nickname: String
    ): BaseResponse<ResponseBody>

    suspend fun saveNick(accessToken: String, nickname: String
    ): BaseResponse<ResponseBody>

    suspend fun saveTermsAgreement(accessToken: String, options: Map<String, String>
    ): BaseResponse<ResponseBody>
}