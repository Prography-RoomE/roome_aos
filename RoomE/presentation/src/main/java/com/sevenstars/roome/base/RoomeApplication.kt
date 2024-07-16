package com.sevenstars.roome.base

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.common.KakaoSdk
import com.sevenstars.domain.repository.auth.UserPreferencesRepository
import com.sevenstars.roome.BuildConfig
import com.sevenstars.roome.utils.AnalyticsHelper
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class RoomeApplication: Application() {

    companion object {
        lateinit var app: RoomeApplication
        var userName: String? = null
    }

    @Inject
    lateinit var userPreferences: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        app = this
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)
        AnalyticsHelper.initialize(this)

//        val keyHash = Utility.getKeyHash(this)
//        LoggerUtils.debug("해시 키 값 : $keyHash")
    }
}