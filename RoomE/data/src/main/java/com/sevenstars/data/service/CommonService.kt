package com.sevenstars.data.service

import com.sevenstars.data.model.BaseResponse
import com.sevenstars.data.model.common.VersionDTO
import com.sevenstars.data.model.profile.info.ResponseProfileInfoDTO
import retrofit2.Response
import retrofit2.http.GET

interface CommonService {
    @GET("/versions/aos")
    suspend fun getMinUpdateVersion(): Response<BaseResponse<VersionDTO>>
}