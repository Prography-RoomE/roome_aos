package com.sevenstars.data.datasource.remote

import com.sevenstars.data.model.BaseResponse
import com.sevenstars.data.model.common.VersionDTO
import com.sevenstars.data.model.profile.info.ResponseProfileInfoDTO

interface CommonRemoteDataSource {
    suspend fun getMinUpdateVersion(): BaseResponse<VersionDTO>
}