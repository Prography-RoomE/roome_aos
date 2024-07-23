package com.sevenstars.data.interceptor

import android.app.Application
import android.content.Intent
import com.google.gson.Gson
import com.sevenstars.data.model.auth.ResponseSignInDTO
import com.sevenstars.data.repository.UserPreferencesRepositoryImpl
import com.sevenstars.data.utils.LoggerUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import javax.inject.Inject

/*
    401: 인증 필요
    1000: 토큰이 유효하지 않은 경우
    1001: 토큰이 만료된 경우
    1006: refresh token 유효하지 않은 경우
 */

class TokenAuthInterceptor @Inject constructor(
    private val application: Application,
    private val baseUrl: String,
    private val userPreferencesRepositoryImpl: UserPreferencesRepositoryImpl
): Interceptor {

    private val refreshCodeList = listOf(
        401, 1000, 1001, 1006
    )

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalResponse = chain.proceed(originalRequest)

        when(originalResponse.code){
            200 -> return originalResponse
            401 -> {
                LoggerUtils.info("originalResponse Code: 401")
                val responseBody = originalResponse.peekBody(Long.MAX_VALUE).string()
                val jsonObject = JSONObject(responseBody)
                val code = jsonObject.optInt("code")

                if(refreshCodeList.contains(code)){
                    LoggerUtils.info("토큰 갱신 진행")
                    val refreshToken = runBlocking(Dispatchers.IO){ userPreferencesRepositoryImpl.getRefreshToken().getOrNull() }

                    if(refreshToken.isNullOrEmpty()){
                        LoggerUtils.info("토큰 갱신 실패: 리프레시 토큰 없음")
                        handelTokenRefreshTokenFailure()
                        return originalResponse
                    }

                    val requestBody = JSONObject().apply {
                        put("refreshToken", refreshToken)
                    }.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

                    val refreshRequest = runBlocking {
                        Request.Builder()
                            .url("${baseUrl}/auth/token")
                            .post(requestBody)
                            .build()
                    }

                    val refreshResponse = chain.proceed(refreshRequest)

                    if(refreshResponse.code == 200) {
                        val refreshResponseBody = refreshResponse.peekBody(Long.MAX_VALUE)
                        val data = JSONObject(refreshResponseBody.string()).getString("data")
                        val refreshData = Gson().fromJson(data, ResponseSignInDTO::class.java)

                        LoggerUtils.info("토큰 갱신 성공")

                        CoroutineScope(Dispatchers.IO).launch {
                            userPreferencesRepositoryImpl.setAccessToken(refreshData.accessToken)
                            userPreferencesRepositoryImpl.setRefreshToken(refreshData.refreshToken)
                        }

                        LoggerUtils.info("API 재요청 진행")

                        val newRequest = originalRequest.newBuilder()
                            .removeHeader("Authorization")
                            .addHeader("Authorization", "Bearer ${refreshData.accessToken}")
                            .build()

                        originalResponse.close()
                        return chain.proceed(newRequest)
                    } else {
                        handelTokenRefreshTokenFailure()
                    }
                }
            }
        }
        return originalResponse
    }

    private fun handelTokenRefreshTokenFailure() {
        CoroutineScope(Dispatchers.IO).launch {
            userPreferencesRepositoryImpl.clearData()

            val intent = Intent.makeRestartActivityTask(application.packageManager.getLaunchIntentForPackage(application.packageName)?.component)
            application.startActivity(intent)
        }
    }
}