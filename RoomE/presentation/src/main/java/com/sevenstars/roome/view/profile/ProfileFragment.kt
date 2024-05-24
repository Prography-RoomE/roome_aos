package com.sevenstars.roome.view.profile

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.activityViewModels

import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileBinding
import com.sevenstars.roome.exetnsion.setColorBackground
import com.sevenstars.roome.utils.ImageUtils
import com.sevenstars.roome.utils.PermissionManager

class ProfileFragment: BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    private lateinit var permissionManager: PermissionManager
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    private val REQUIRED_PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    override fun initView() {
        (requireActivity() as ProfileActivity).setToolbarVisibility(false)
        permissionManager = PermissionManager(requireActivity() as ProfileActivity)

        Handler(Looper.getMainLooper()).postDelayed({
            binding.lottieProfile.pauseAnimation()
            binding.groupLoading.visibility = View.GONE
            binding.groupShow.visibility = View.VISIBLE
        }, 2000)

        setColorBackground(
            binding.ivProfile,
            mode = "gradient",
            shape = "linear",
            orientation = "topLeftToBottomRight",
            startColor = "#FF453C",
            endColor = "#FFACB3",
            isRoundCorner = false
        )

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val grantResults = permissions.values.map { if (it) PackageManager.PERMISSION_GRANTED else PackageManager.PERMISSION_DENIED }.toIntArray()
            permissionManager.handlePermissionResult(
                permissions.keys.toTypedArray(),
                grantResults,
                onPermissionsGranted = {
                    ImageUtils.saveViewToGallery(requireContext(), binding.ivProfile)
                },
                onPermissionsDenied = {}
            )
        }
    }

    override fun initListener() {
        super.initListener()
        with(binding){

            tgForward.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    tgVertical.isChecked = false
                    setAspectRatio("1:1")
                }
            }

            tgVertical.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    tgForward.isChecked = false
                    setAspectRatio("3:4")
                }
            }

            btnSaveProfile.setOnClickListener {
                if (permissionManager.checkPermissions(REQUIRED_PERMISSIONS)) {
                    ImageUtils.saveViewToGallery(requireContext(), ivProfile)
                } else {
                    permissionManager.requestPermissions(permissionLauncher, REQUIRED_PERMISSIONS)
                }
            }
        }
    }

    private fun setAspectRatio(ratio: String){
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.clProfile)

        if (ratio == "3:4") {
            constraintSet.constrainWidth(binding.ivProfile.id, (binding.ivProfile.height/4)*3)
            constraintSet.constrainHeight(binding.ivProfile.id, binding.ivProfile.height)
        } else {
            constraintSet.constrainWidth(binding.ivProfile.id, MATCH_PARENT)
        }

        constraintSet.applyTo(binding.clProfile)
    }
}