package com.sevenstars.roome.base

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.sevenstars.roome.BuildConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RoomeApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)

//        val keyHash = Utility.getKeyHash(this)
//        LoggerUtils.debug("해시 키 값 : $keyHash")
    }
}