package com.sevenstars.roome.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
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
                showRationaleDialog(launcher, permissionsToRequest)
            } else {
                launcher.launch(permissionsToRequest)
            }
        }
    }

    private fun showRationaleDialog(
        launcher: ActivityResultLauncher<Array<String>>,
        permissions: Array<String>
    ) {
        AlertDialog.Builder(activity)
            .setTitle("권한이 필요합니다")
            .setMessage("해당 기능을 사용하기 위해 권한이 필요합니다\n부여해주세요")
            .setPositiveButton("승인") { dialog, _ ->
                dialog.dismiss()
                launcher.launch(permissions)
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(activity)
            .setTitle("권한이 필요합니다")
            .setMessage("일부 권한을 거부했습니다\n설정에서 허용해주세요")
            .setPositiveButton("설정으로 이동") { dialog, _ ->
                dialog.dismiss()
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", activity.packageName, null)
                }
                activity.startActivity(intent)
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
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
            showSettingsDialog()
        }
    }
}
