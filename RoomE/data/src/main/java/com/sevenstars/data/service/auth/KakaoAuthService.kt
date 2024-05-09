package com.sevenstars.data.service.auth

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.sevenstars.data.utils.LoggerUtils
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject
import kotlin.reflect.KFunction3

class KakaoAuthService @Inject constructor(
    @ActivityContext private val context: Context,
    private val client: UserApiClient,
) {
    private val logger = LoggerUtils().getInstance()

    private val isKakaoTalkLoginAvailable: Boolean
        get() = client.isKakaoTalkLoginAvailable(context)

    fun signInKakao(
        signInListener: KFunction3<String, String?, String?, Unit>
    ) {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                signInError(error)
            } else if (token != null) signInSuccess(token, signInListener)
        }

        if (isKakaoTalkLoginAvailable) {
            client.loginWithKakaoTalk(context, callback = callback)
        } else {
            client.loginWithKakaoAccount(context, callback = callback)
        }
    }

    private fun signInError(throwable: Throwable) {
        val kakaoType = if (isKakaoTalkLoginAvailable) KAKAO_TALK else KAKAO_ACCOUNT
        logger.error("{$kakaoType}으로 로그인 실패 ${throwable.message}")
    }

    private fun signInSuccess(
        oAuthToken: OAuthToken,
        signInListener: KFunction3<String, String?, String?, Unit>
    ) {
        logger.debug("$KAKAO_ID_TOKEN ${oAuthToken.idToken}")
        client.me { _, error ->
            signInListener("kakao", null, oAuthToken.idToken)
            if (error != null) {
                logger.error("사용자 정보 요청 실패 $error")
            }
        }
    }

    companion object {
        const val KAKAO_TALK = "카카오톡"
        const val KAKAO_ACCOUNT = "카카오계정"
        const val KAKAO_ID_TOKEN = "카카오 ID 토큰"
    }
}