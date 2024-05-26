package com.sevenstars.roome.base

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.sevenstars.domain.repository.auth.UserPreferencesRepository
import com.sevenstars.roome.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class RoomeApplication: Application() {
    companion object {
        lateinit var app: RoomeApplication
        lateinit var userName: String
    }

    @Inject
    lateinit var userPreferences: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        app = this
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)

//        val keyHash = Utility.getKeyHash(this)
//        LoggerUtils.debug("해시 키 값 : $keyHash")
    }
}