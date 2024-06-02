package com.sevenstars.data.datasourceimpl.remote

import com.sevenstars.data.datasource.remote.ProfileRemoteDataSource
import com.sevenstars.data.model.BaseResponse
import com.sevenstars.data.model.profile.RequestMbtiDTO
import com.sevenstars.data.model.profile.RequestRoomCountDTO
import com.sevenstars.data.model.profile.RequestRoomCountRangeDTO
import com.sevenstars.data.model.profile.RequestSaveIdsDTO
import com.sevenstars.data.model.profile.ResponseProfileDTO
import com.sevenstars.data.model.profile.info.RequestSaveIdDTO
import com.sevenstars.data.model.profile.info.ResponseProfileInfoDTO
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

    override suspend fun getProfileData(accessToken: String): BaseResponse<ResponseProfileDTO> {
        return try {
            val res = profileService.getProfileData(accessToken)

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

    override suspend fun deleteProfileData(accessToken: String): BaseResponse<Boolean> {
        return try {
            val res = profileService.deleteProfileData(accessToken)

            if (res.isSuccessful) {
                val body = res.body()!!
                BaseResponse(code = body.code, message = body.message, data = true)
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

    override suspend fun saveUserStrengths(
        accessToken: String,
        body: RequestSaveIdsDTO
    ): BaseResponse<Boolean> {
        return try {
            val res = profileService.saveUserStrengths(accessToken, body)

            if (res.isSuccessful) {
                val resBody = res.body()!!
                BaseResponse(code = resBody.code, message = resBody.message, data = true)
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

    override suspend fun saveUserImportantFactors(
        accessToken: String,
        body: RequestSaveIdsDTO
    ): BaseResponse<Boolean> {
        return try {
            val res = profileService.saveUserImportantFactors(accessToken, body)

            if (res.isSuccessful) {
                val resBody = res.body()!!
                BaseResponse(code = resBody.code, message = resBody.message, data = true)
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

    override suspend fun saveUserDislikeFactors(
        accessToken: String,
        body: RequestSaveIdsDTO
    ): BaseResponse<Boolean> {
        return try {
            val res = profileService.saveUserDislikeFactors(accessToken, body)

            if (res.isSuccessful) {
                val resBody = res.body()!!
                BaseResponse(code = resBody.code, message = resBody.message, data = true)
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

    override suspend fun saveUserRoomCount(
        accessToken: String,
        body: RequestRoomCountDTO
    ): BaseResponse<Boolean> {
        return try {
            val res = profileService.saveUserRoomCount(accessToken, body)

            if (res.isSuccessful) {
                val resBody = res.body()!!
                BaseResponse(code = resBody.code, message = resBody.message, data = true)
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

    override suspend fun saveUserRoomCountRange(
        accessToken: String,
        body: RequestRoomCountRangeDTO
    ): BaseResponse<Boolean> {
        return try {
            val res = profileService.saveUserRoomCountRange(accessToken, body)

            if (res.isSuccessful) {
                val resBody = res.body()!!
                BaseResponse(code = resBody.code, message = resBody.message, data = true)
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

    override suspend fun saveUserPreferredGenres(
        accessToken: String,
        body: RequestSaveIdsDTO
    ): BaseResponse<Boolean> {
        return try {
            val res = profileService.saveUserPreferredGenres(accessToken, body)

            if (res.isSuccessful) {
                val resBody = res.body()!!
                BaseResponse(code = resBody.code, message = resBody.message, data = true)
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

    override suspend fun saveUserMbti(
        accessToken: String,
        body: RequestMbtiDTO
    ): BaseResponse<Boolean> {
        return try {
            val res = profileService.saveUserMbti(accessToken, body)

            if (res.isSuccessful) {
                val resBody = res.body()!!
                BaseResponse(code = resBody.code, message = resBody.message, data = true)
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

    override suspend fun saveUserHorrorThemePos(
        accessToken: String,
        body: RequestSaveIdDTO
    ): BaseResponse<Boolean> {
        return try {
            val res = profileService.saveUserHorrorThemePos(accessToken, body)

            if (res.isSuccessful) {
                val resBody = res.body()!!
                BaseResponse(code = resBody.code, message = resBody.message, data = true)
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

    override suspend fun saveUserHintUsagePreference(
        accessToken: String,
        body: RequestSaveIdDTO
    ): BaseResponse<Boolean> {
        return try {
            val res = profileService.saveUserHintUsagePreference(accessToken, body)

            if (res.isSuccessful) {
                val resBody = res.body()!!
                BaseResponse(code = resBody.code, message = resBody.message, data = true)
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

    override suspend fun saveUserDeviceLockPreference(
        accessToken: String,
        body: RequestSaveIdDTO
    ): BaseResponse<Boolean> {
        return try {
            val res = profileService.saveUserDeviceLockPreference(accessToken, body)

            if (res.isSuccessful) {
                val resBody = res.body()!!
                BaseResponse(code = resBody.code, message = resBody.message, data = true)
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

    override suspend fun saveUserColor(
        accessToken: String,
        body: RequestSaveIdDTO
    ): BaseResponse<Boolean> {
        return try {
            val res = profileService.saveUserColor(accessToken, body)

            if (res.isSuccessful) {
                val resBody = res.body()!!
                BaseResponse(code = resBody.code, message = resBody.message, data = true)
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

    override suspend fun saveUserActivity(
        accessToken: String,
        body: RequestSaveIdDTO
    ): BaseResponse<Boolean> {
        return try {
            val res = profileService.saveUserActivity(accessToken, body)

            if (res.isSuccessful) {
                val resBody = res.body()!!
                BaseResponse(code = resBody.code, message = resBody.message, data = true)
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