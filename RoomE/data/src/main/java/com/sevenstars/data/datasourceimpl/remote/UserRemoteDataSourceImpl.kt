package com.sevenstars.data.datasourceimpl.remote

import com.sevenstars.data.datasource.remote.UserRemoteDataSource
import com.sevenstars.data.model.BaseResponse
import com.sevenstars.data.model.user.ResponseUserInfoDTO
import com.sevenstars.data.service.UserService
import com.sevenstars.data.utils.LoggerUtils
import org.json.JSONObject
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val userService: UserService
): UserRemoteDataSource {

    override suspend fun getUserInfo(token: String): BaseResponse<ResponseUserInfoDTO> {
        return try {
            val res = userService.getUserInfo(token)

            if(res.isSuccessful){
                val body = res.body()!!
                BaseResponse(code = body.code, message = body.message, data = body.data)
            } else {
                val errorBody = JSONObject(res.errorBody()?.string()!!)
                BaseResponse(code = errorBody.getString("code").toInt(), message = errorBody.getString("message"), null)
            }
        } catch (e: Exception){
            LoggerUtils.error(e.stackTraceToString())
            BaseResponse(code = 0, message = e.message.toString(), null)
        }
    }
}