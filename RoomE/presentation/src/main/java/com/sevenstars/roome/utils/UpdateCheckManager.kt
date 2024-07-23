package com.sevenstars.roome.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.sevenstars.roome.BuildConfig


class UpdateCheckManager(private val context: Context) {
    private val appUpdateManager = AppUpdateManagerFactory.create(context)

    fun checkForUpdate(callback: (Boolean) -> Unit) {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            val versionCode = appUpdateInfo.availableVersionCode()
            callback(BuildConfig.VERSION_CODE < versionCode)
        }.addOnFailureListener { _ ->
            callback(false)
        }
    }

    fun promptUpdate() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${context.packageName}"))
        context.startActivity(intent)
    }
}
