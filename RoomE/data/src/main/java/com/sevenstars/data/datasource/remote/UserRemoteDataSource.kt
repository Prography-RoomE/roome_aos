package com.sevenstars.data.datasource.remote

import android.net.Uri
import com.sevenstars.data.model.BaseResponse
import com.sevenstars.data.model.profile.ResponseProfileDTO
import com.sevenstars.data.model.user.ResponsePostImageDTO
import com.sevenstars.data.model.user.ResponseUserInfoDTO
import com.sevenstars.domain.utils.RoomeResult
import okhttp3.ResponseBody

interface UserRemoteDataSource {
    suspend fun getUserInfo(token: String): BaseResponse<ResponseUserInfoDTO>

    suspend fun validationNick(accessToken: String, nickname: String
    ): BaseResponse<ResponseBody>

    suspend fun saveNick(accessToken: String, nickname: String
    ): BaseResponse<ResponseBody>

    suspend fun saveTermsAgreement(accessToken: String, options: Map<String, Boolean>
    ): BaseResponse<ResponseBody>

    suspend fun getUserProfile(nickname: String
    ): BaseResponse<ResponseProfileDTO>

    suspend fun postProfileImage(
        accessToken: String,
        realPath: String
    ): BaseResponse<ResponsePostImageDTO>

    suspend fun deleteProfileImage(
        accessToken: String
    ): BaseResponse<ResponseBody>
}