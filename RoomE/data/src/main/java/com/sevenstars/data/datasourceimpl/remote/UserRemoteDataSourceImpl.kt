package com.sevenstars.data.datasourceimpl.remote

import android.net.Uri
import com.sevenstars.data.datasource.remote.UserRemoteDataSource
import com.sevenstars.data.mapper.user.TermsAgreementMapper
import com.sevenstars.data.model.BaseResponse
import com.sevenstars.data.model.profile.ResponseProfileDTO
import com.sevenstars.data.model.user.RequestNickDTO
import com.sevenstars.data.model.user.ResponsePostImageDTO
import com.sevenstars.data.model.user.ResponseUserInfoDTO
import com.sevenstars.data.service.UserService
import com.sevenstars.data.utils.LoggerUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.File
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val userService: UserService
): UserRemoteDataSource {

    override suspend fun getUserInfo(token: String): BaseResponse<ResponseUserInfoDTO> {
        return try {
            val res = userService.getUserInfo(token)

            if(res.isSuccessful) res.body()!!
            else {
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

            if(res.isSuccessful) res.body()!!
            else {
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

            if(res.isSuccessful) res.body()!!
            else {
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

            if(res.isSuccessful)res.body()!!
            else {
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

            if (res.isSuccessful)res.body()!!
            else {
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

    override suspend fun postProfileImage(
        accessToken: String,
        realPath: String
    ): BaseResponse<ResponsePostImageDTO> {
        return try {
            val file = File(realPath)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            val res = userService.postProfileImage(accessToken, body)

            if (res.isSuccessful) res.body()!!
            else {
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

    override suspend fun deleteProfileImage(accessToken: String): BaseResponse<ResponseBody> {
        return try {
            val res = userService.deleteProfileImage(accessToken)

            if (res.isSuccessful) res.body()!!
            else {
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