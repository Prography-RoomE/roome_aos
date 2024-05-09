package com.sevenstars.roome.base

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.sevenstars.roome.BuildConfig
import com.sevenstars.roome.utils.LoggerUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RoomeApplication: Application() {
    companion object {
        val logger = LoggerUtils().getInstance()
    }

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)

        val keyHash = Utility.getKeyHash(this)
        logger.error("해시 키 값 : $keyHash")
    }
}