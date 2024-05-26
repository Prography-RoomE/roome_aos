package com.sevenstars.roome.view.profile.generate

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit

import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileBinding
import com.sevenstars.roome.exetnsion.setColorBackground
import com.sevenstars.roome.utils.ImageUtils
import com.sevenstars.roome.utils.PermissionManager
import com.sevenstars.roome.custom.CustomDialog
import com.sevenstars.roome.view.MainActivity
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.welcome.ProfileWelcomeFragment

class ProfileFragment: BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    private val profileViewModel: ProfileViewModel by activityViewModels()
    private lateinit var callback: OnBackPressedCallback

    private lateinit var permissionManager: PermissionManager
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    private val REQUIRED_PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { /* Back Pressed 방지용 */}
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    override fun initView() {
        (requireActivity() as ProfileActivity).setToolbarVisibility(false)
        permissionManager = PermissionManager(requireActivity() as ProfileActivity)

        Handler(Looper.getMainLooper()).postDelayed({
            binding.lottieProfile.pauseAnimation()
            binding.groupLoading.visibility = View.GONE
            binding.groupShow.visibility = View.VISIBLE
        }, 2000)

        profileViewModel.selectedProfileData.color?.run {
            setColorBackground(
                binding.ivProfile,
                mode = mode,
                shape = shape,
                orientation = direction,
                startColor = startColor,
                endColor = endColor,
                isRoundCorner = false
            )
        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val grantResults = permissions.values.map { if (it) PackageManager.PERMISSION_GRANTED else PackageManager.PERMISSION_DENIED }.toIntArray()
            permissionManager.handlePermissionResult(
                permissions.keys.toTypedArray(),
                grantResults,
                onPermissionsGranted = {
                    if(ImageUtils.saveViewToGallery(requireContext(), binding.ivProfile)) showSaveSuccessDialog()
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
                    if(ImageUtils.saveViewToGallery(requireContext(), ivProfile)) showSaveSuccessDialog()
                } else {
                    permissionManager.requestPermissions(permissionLauncher, REQUIRED_PERMISSIONS)
                }
            }

            btnMoveProfile.setOnClickListener {
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }

            ibCancel.setOnClickListener {
                navigateToProfileWelcomeFragment()
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

    private fun navigateToProfileWelcomeFragment() {
        requireActivity().supportFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
        requireActivity().supportFragmentManager.commit {
            replace(R.id.fl_profile, ProfileWelcomeFragment(11))
            addToBackStack(null)
        }
    }

    private fun showSaveSuccessDialog(){
        CustomDialog.getInstance(CustomDialog.DialogType.SAVE_PROFILE, null).show(requireActivity().supportFragmentManager, "")
    }
}