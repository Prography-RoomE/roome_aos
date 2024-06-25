package com.sevenstars.roome.utils

import android.content.Context
import com.kakao.sdk.share.ShareClient
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.base.RoomeApplication.Companion.userName
import java.io.File

class KakaoShareManager(
    private val context: Context,
) {
    private val PROFILE_TEMPLATE_ID: Long = 109406

    fun doProfileShare(imageFile: File) {
        if (!ShareClient.instance.isKakaoTalkSharingAvailable(context)) {
            LoggerUtils.error("카카오톡 미설치")
            return
        }

        uploadImageAndShare(imageFile)
    }

    private fun uploadImageAndShare(imageFile: File) {
        ShareClient.instance.uploadImage(imageFile) { result, error ->
            when {
                error != null -> {
                    LoggerUtils.error("이미지 업로드 실패: $error")
                }
                result != null -> {
                    LoggerUtils.debug("이미지 업로드 성공 ${result.infos}")
                    shareCustomTemplate(result.infos.original.url)
                }
            }
        }
    }

    private fun shareCustomTemplate(imageUrl: String) {
        val templateArgs = mapOf(
            "TYPE" to "profile",
            "VALUE" to userName,
            "PROFILE_IMAGE" to imageUrl,
            "NICK" to userName
        )

        ShareClient.instance.shareCustom(
            context,
            PROFILE_TEMPLATE_ID,
            templateArgs
        ) { sharingResult, sharingError ->
            when {
                sharingError != null -> {
                    LoggerUtils.error("카카오톡 공유 실패: $sharingError")
                }
                sharingResult != null -> {
                    LoggerUtils.debug("카카오톡 공유 성공 ${sharingResult.intent}")
                    context.startActivity(sharingResult.intent)

                    logWarnings(sharingResult.warningMsg, sharingResult.argumentMsg)
                }
            }
        }
    }

    private fun logWarnings(warningMsg: Map<String, String>, argumentMsg: Map<String, String>) {
        if (warningMsg.isNotEmpty()) LoggerUtils.warning("Warning Msg: $warningMsg")
        if (argumentMsg.isNotEmpty()) LoggerUtils.warning("Argument Msg: $argumentMsg")
    }
}
