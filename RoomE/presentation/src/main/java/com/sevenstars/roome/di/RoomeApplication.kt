package com.sevenstars.roome.di

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class RoomeApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "{NATIVE_APP_KEY}")

        val keyHash = Utility.getKeyHash(this)
        Timber.e("해시 키 값 : $keyHash")
    }
}