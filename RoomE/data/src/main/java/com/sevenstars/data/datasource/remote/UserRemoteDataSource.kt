package com.sevenstars.data.datasource.remote

import com.sevenstars.data.model.BaseResponse
import com.sevenstars.data.model.user.ResponseUserInfoDTO

interface UserRemoteDataSource {
    suspend fun getUserInfo(token: String): BaseResponse<ResponseUserInfoDTO>
}