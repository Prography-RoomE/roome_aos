package com.sevenstars.data.datasourceimpl.remote

import com.sevenstars.data.datasource.remote.AuthRemoteDataSource
import com.sevenstars.data.model.BaseResponse
import com.sevenstars.data.model.auth.RequestRefreshTokenDTO
import com.sevenstars.data.model.auth.RequestSignInDTO
import com.sevenstars.data.model.auth.RequestUnlinkDTO
import com.sevenstars.data.model.auth.ResponseSignInDTO
import com.sevenstars.data.service.auth.AuthService
import com.sevenstars.data.utils.LoggerUtils
import okhttp3.ResponseBody
import org.json.JSONObject
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val authService: AuthService
): AuthRemoteDataSource {
    override suspend fun signIn(body: RequestSignInDTO): BaseResponse<ResponseSignInDTO> {
        return try {
            val res = authService.signIn(body)

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

    override suspend fun unlink(
        accessToken: String,
        body: RequestUnlinkDTO
    ): BaseResponse<ResponseBody> {
        return try {
            val res = authService.unlink(accessToken, body)

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

    override suspend fun signOut(accessToken: String): BaseResponse<ResponseBody> {
        return try {
            val res = authService.signOut(accessToken)

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

    override suspend fun refreshToken(body: RequestRefreshTokenDTO): BaseResponse<ResponseSignInDTO> {
        return try {
            val res = authService.refreshToken(body)

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