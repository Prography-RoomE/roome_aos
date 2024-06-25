package com.sevenstars.data.datasourceimpl.remote

import com.sevenstars.data.datasource.remote.UserRemoteDataSource
import com.sevenstars.data.mapper.user.TermsAgreementMapper
import com.sevenstars.data.model.BaseResponse
import com.sevenstars.data.model.profile.ResponseProfileDTO
import com.sevenstars.data.model.user.RequestNickDTO
import com.sevenstars.data.model.user.ResponseUserInfoDTO
import com.sevenstars.data.service.UserService
import com.sevenstars.data.utils.LoggerUtils
import okhttp3.ResponseBody
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

    override suspend fun validationNick(
        accessToken: String,
        nickname: String
    ): BaseResponse<ResponseBody> {
        return try {
            val res = userService.validationNick(accessToken, RequestNickDTO(nickname))

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

    override suspend fun saveNick(
        accessToken: String,
        nickname: String
    ): BaseResponse<ResponseBody> {
        return try {
            val res = userService.saveNick(accessToken, RequestNickDTO(nickname))

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

    override suspend fun saveTermsAgreement(
        accessToken: String,
        options: Map<String, Boolean>
    ): BaseResponse<ResponseBody> {
        return try {
            val res = userService.saveTermsAgreement(accessToken, TermsAgreementMapper.mapperToRequestDTO(options))

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

    override suspend fun getUserProfile(nickname: String): BaseResponse<ResponseProfileDTO> {
        return try {
            val res = userService.getUserProfile(nickname)

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