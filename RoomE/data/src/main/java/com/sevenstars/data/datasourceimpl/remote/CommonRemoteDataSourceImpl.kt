package com.sevenstars.data.datasourceimpl.remote

import com.sevenstars.data.datasource.remote.AuthRemoteDataSource
import com.sevenstars.data.datasource.remote.CommonRemoteDataSource
import com.sevenstars.data.model.BaseResponse
import com.sevenstars.data.model.common.VersionDTO
import com.sevenstars.data.service.CommonService
import com.sevenstars.data.service.auth.AuthService
import com.sevenstars.data.utils.LoggerUtils
import org.json.JSONObject
import javax.inject.Inject

class CommonRemoteDataSourceImpl @Inject constructor(
    private val commonService: CommonService
): CommonRemoteDataSource {

    override suspend fun getMinUpdateVersion(): BaseResponse<VersionDTO> {
        return try {
            val res = commonService.getMinUpdateVersion()

            if(res.isSuccessful){
                val responseBody = res.body()!!
                BaseResponse(code = responseBody.code, message = responseBody.message, data = responseBody.data)
            } else {
                val errorBody = JSONObject(res.errorBody()?.string()!!)
                BaseResponse(code = errorBody.getString("code").toInt(), message = errorBody.getString("message"), null)
            }
        } catch (e: Exception){
            LoggerUtils.error(e.message.toString())
            BaseResponse(code = 0, message = e.message.toString(), null)
        }
    }
}