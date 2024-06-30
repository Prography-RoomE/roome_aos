package com.sevenstars.roome.utils

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionManager(private val activity: Activity) {

    fun checkPermissions(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun requestPermissions(
        launcher: ActivityResultLauncher<Array<String>>,
        permissions: Array<String>
    ) {
        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsToRequest.isNotEmpty()) {
            val showRationale = permissionsToRequest.any {
                ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
            }
            if (showRationale) {
                launcher.launch(permissions)
            } else {
                launcher.launch(permissionsToRequest)
            }
        }
    }

    fun handlePermissionResult(
        permissions: Array<String>,
        grantResults: IntArray,
        onPermissionsGranted: () -> Unit,
        onPermissionsDenied: () -> Unit
    ) {
        val deniedPermissions = permissions.filterIndexed { index, _ ->
            grantResults[index] != PackageManager.PERMISSION_GRANTED
        }

        if (deniedPermissions.isEmpty()) {
            onPermissionsGranted()
        } else {
            onPermissionsDenied()
        }
    }
}
