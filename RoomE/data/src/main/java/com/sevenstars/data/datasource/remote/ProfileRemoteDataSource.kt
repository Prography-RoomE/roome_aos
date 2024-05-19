package com.sevenstars.data.datasource.remote

import com.sevenstars.data.model.BaseResponse
import com.sevenstars.data.model.profile.ResponseProfileInfoDTO

interface ProfileRemoteDataSource {
    suspend fun getProfileInfo(token: String): BaseResponse<ResponseProfileInfoDTO>
}