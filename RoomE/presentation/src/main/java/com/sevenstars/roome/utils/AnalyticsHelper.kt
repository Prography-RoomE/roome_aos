package com.sevenstars.roome.utils

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.sevenstars.roome.BuildConfig

object AnalyticsHelper {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    fun initialize(context: Context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)
    }

    fun logScreenView(screenName: String) {
        val params = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        }
        if(!BuildConfig.DEBUG) firebaseAnalytics.logEvent("view_${screenName}_page", params)
    }

    fun logButtonClick(buttonName: String) {
        val params = Bundle().apply {
            putString("button_name", buttonName)
        }
        if(!BuildConfig.DEBUG) firebaseAnalytics.logEvent("tap_${buttonName}_button", params)
    }

    fun logEvent(event: String) {
        if(!BuildConfig.DEBUG) firebaseAnalytics.logEvent(event, null)
    }
}
