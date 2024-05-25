package com.sevenstars.data.datasourceimpl.remote

import com.sevenstars.data.datasource.remote.ProfileRemoteDataSource
import com.sevenstars.data.model.BaseResponse
import com.sevenstars.data.model.profile.ResponseProfileInfoDTO
import com.sevenstars.data.service.ProfileService
import com.sevenstars.data.utils.LoggerUtils
import org.json.JSONObject
import javax.inject.Inject

class ProfileRemoteDataSourceImpl @Inject constructor(
    private val profileService: ProfileService
): ProfileRemoteDataSource {
    override suspend fun getDefaultProfileData(token: String): BaseResponse<ResponseProfileInfoDTO> {
        return try {
            val res = profileService.getDefaultProfileData(token)

            if (res.isSuccessful) {
                val body = res.body()!!
                BaseResponse(code = body.code, message = body.message, data = body.data)
            } else {
                val errorBody = JSONObject(res.errorBody()?.string()!!)
                BaseResponse(
                    code = errorBody.getString("code").toInt(),
                    message = errorBody.getString("message"),
                    null
                )
            }
        } catch (e: Exception) {
            LoggerUtils.error(e.stackTraceToString())
            BaseResponse(code = 0, message = e.message.toString(), null)
        }
    }
}