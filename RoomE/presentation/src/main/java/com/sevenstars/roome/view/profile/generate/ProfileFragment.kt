package com.sevenstars.roome.view.profile.generate

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.TextView
import android.widget.ToggleButton
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.domain.model.profile.SavedProfileData
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.base.RoomeApplication.Companion.userName
import com.sevenstars.roome.custom.CustomDialog
import com.sevenstars.roome.databinding.FragmentProfileBinding
import com.sevenstars.roome.exetnsion.setColorBackground
import com.sevenstars.roome.utils.ImageUtils
import com.sevenstars.roome.utils.ImageUtils.captureViewToCache
import com.sevenstars.roome.utils.PermissionManager
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.main.MainActivity
import com.sevenstars.roome.view.profile.ProfileActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {
    private val viewModel: ProfileGenerateViewModel by viewModels()
    private var squareProfileFile: File? = null
    private var verticalProfileFile: File? = null
    private lateinit var permissionManager: PermissionManager
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    override fun initView() {
        (requireActivity() as ProfileActivity).setToolbarVisibility(false)
        viewModel.fetchSaveData()
        setPermissionLauncher()
    }

    override fun observer() {
        super.observer()
        viewModel.uiState.observe(this) { handleUiState(it) }
    }

    override fun initListener() {
        super.initListener()
        with(binding) {
            tgSquare.setOnCheckedChangeListener { _, isChecked -> handleToggleChange(isChecked, squareProfileFile, tgSquare) }
            tgVertical.setOnCheckedChangeListener { _, isChecked -> handleToggleChange(isChecked, verticalProfileFile, tgVertical) }
            btnSaveProfile.setOnClickListener { handleSaveProfileClick() }
            btnMoveProfile.setOnClickListener { navigateToMainActivity() }
            ibBack.setOnClickListener { backPressed() }
        }
    }

    private fun handleUiState(uiState: UiState<SavedProfileData>) {
        when (uiState) {
            is UiState.Failure -> {
                LoggerUtils.error("Profile data fetch failed\n${uiState.message}")
                showToast("An error occurred while creating the profile. Please restart the app.")
                if(uiState.code == 0) showNoConnectionDialog(R.id.fl_profile)
            }
            is UiState.Loading -> { /* Show loading */ }
            is UiState.Success -> {
                LoggerUtils.debug("Profile data fetch successful: Stopping Lottie")
                pauseLottie(uiState.data)
            }
        }
    }

    private fun handleToggleChange(isChecked: Boolean, profileFile: File?, currentToggle: ToggleButton) {
        if (isChecked) {
            val otherToggle = if (currentToggle == binding.tgSquare) binding.tgVertical else binding.tgSquare

            otherToggle.isChecked = false

            val bitmap = BitmapFactory.decodeFile(profileFile?.absolutePath)
            binding.ivProfile.setImageBitmap(bitmap)
        } else {
            if (!binding.tgSquare.isChecked && !binding.tgVertical.isChecked) {
                currentToggle.isChecked = true
            }
        }
    }

    private fun handleSaveProfileClick() {
        if (permissionManager.checkPermissions(requiredPermissions)) {
            if (ImageUtils.saveViewToGallery(requireContext(), binding.ivProfile)) showSaveSuccessDialog()
        } else {
            permissionManager.requestPermissions(permissionLauncher, requiredPermissions)
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun backPressed(){
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun pauseLottie(data: SavedProfileData) {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.lottieProfile.pauseAnimation()
            binding.groupLoading.visibility = View.GONE
            binding.groupShow.visibility = View.VISIBLE
            binding.groupBaseProfile.visibility = View.VISIBLE
            setSquareProfile(data)
            setVerticalProfile(data)
        }, 2000)
    }

    private fun setSquareProfile(data: SavedProfileData) {
        with(binding.icProfileSquare) {
            bindProfileData(data, "square")
            setProfileBackground(data, llProfile)
        }
        binding.icProfileSquare.llProfile.viewTreeObserver.addOnGlobalLayoutListener {
            if (squareProfileFile == null) {
                squareProfileFile = captureViewToCache(requireContext(), "${userName}'s Square Profile", binding.icProfileSquare.root)
                handleProfileFileCreation(squareProfileFile, binding.icProfileSquare.root)
            }
        }
    }

    private fun setVerticalProfile(data: SavedProfileData) {
        with(binding.icProfileVertical) {
            bindProfileData(data, "vertical")
            setProfileBackground(data, llProfile)
        }
        binding.icProfileVertical.llProfile.viewTreeObserver.addOnGlobalLayoutListener {
            if (verticalProfileFile == null) {
                verticalProfileFile = captureViewToCache(requireContext(), "${userName}'s Vertical Profile", binding.icProfileVertical.root)
                handleProfileFileCreation(verticalProfileFile, binding.icProfileVertical.root)
            }
        }
    }

    private fun bindProfileData(data: SavedProfileData, target: String) {
        if(target == "square"){
            with(binding.icProfileSquare){
                name.text = userName
                experience.text = data.count
                mbti.text = if (data.mbti == "NONE") "" else data.mbti
                emptyViewRemove(mbti)

                genre1.text = data.preferredGenres.getOrNull(0)?.title.orEmpty()
                genre2.text = data.preferredGenres.getOrNull(1)?.title.orEmpty()
                emptyViewRemove(genre2)

                strength1.text = data.userStrengths.getOrNull(0)?.title.orEmpty()
                strength2.text = data.userStrengths.getOrNull(1)?.title.orEmpty()
                emptyViewRemove(strength2)

                important1.text = data.themeImportantFactors.getOrNull(0)?.title.orEmpty()
                important2.text = data.themeImportantFactors.getOrNull(1)?.title.orEmpty()
                emptyViewRemove(important2)

                position.text = data.horrorThemePosition?.title.orEmpty()
                if(data.horrorThemePosition?.title == "잘 모르겠어요") position.visibility = View.GONE
                hintUsage.text = data.hintUsagePreference?.title.orEmpty()
                devicePreference.text = data.deviceLockPreference?.title.orEmpty()
                activityPreference.text = data.activity?.title.orEmpty()

                dislike1.text = data.themeDislikedFactors.getOrNull(0)?.title.orEmpty()
                dislike2.text = data.themeDislikedFactors.getOrNull(1)?.title.orEmpty()
                emptyViewRemove(dislike2)
            }
        } else {
            with(binding.icProfileVertical){
                name.text = userName
                experience.text = data.count
                mbti.text = if (data.mbti == "NONE") "" else data.mbti
                emptyViewRemove(mbti)

                genre1.text = data.preferredGenres.getOrNull(0)?.title.orEmpty()
                genre2.text = data.preferredGenres.getOrNull(1)?.title.orEmpty()
                emptyViewRemove(genre2)

                strength1.text = data.userStrengths.getOrNull(0)?.title.orEmpty()
                strength2.text = data.userStrengths.getOrNull(1)?.title.orEmpty()
                emptyViewRemove(strength2)

                important1.text = data.themeImportantFactors.getOrNull(0)?.title.orEmpty()
                important2.text = data.themeImportantFactors.getOrNull(1)?.title.orEmpty()
                emptyViewRemove(important2)

                position.text = data.horrorThemePosition?.title.orEmpty()
                if(data.horrorThemePosition?.title == "잘 모르겠어요") position.visibility = View.GONE
                hintUsage.text = data.hintUsagePreference?.title.orEmpty()
                devicePreference.text = data.deviceLockPreference?.title.orEmpty()
                activityPreference.text = data.activity?.title.orEmpty()

                dislike1.text = data.themeDislikedFactors.getOrNull(0)?.title.orEmpty()
                dislike2.text = data.themeDislikedFactors.getOrNull(1)?.title.orEmpty()
                emptyViewRemove(dislike2)
            }
        }
    }

    private fun emptyViewRemove(view: TextView){
        if(view.text.isEmpty()) view.visibility = View.GONE
    }

    private fun setProfileBackground(data: SavedProfileData, view: View) {
        setColorBackground(
            view,
            mode = data.color?.mode.orEmpty(),
            shape = data.color?.shape.orEmpty(),
            orientation = data.color?.direction.orEmpty(),
            startColor = data.color?.startColor.orEmpty(),
            endColor = data.color?.endColor.orEmpty(),
            isRoundCorner = false
        )
    }

    private fun handleProfileFileCreation(file: File?, rootView: View) {
        if (file != null) {
            rootView.visibility = View.GONE
            handleToggleChange(true, squareProfileFile, binding.tgSquare)
        } else {
            LoggerUtils.error("Profile creation failed")
            binding.groupLoading.visibility = View.VISIBLE
            binding.groupBaseProfile.visibility = View.GONE
            binding.groupShow.visibility = View.GONE
            binding.lottieProfile.playAnimation()
            showToast("프로필 생성 중 문제가 발생했습니다\n재실행 해주세요")
        }
    }

    private fun setPermissionLauncher() {
        permissionManager = PermissionManager(requireActivity() as ProfileActivity)
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val grantResults = permissions.values.map { if (it) PackageManager.PERMISSION_GRANTED else PackageManager.PERMISSION_DENIED }.toIntArray()
            permissionManager.handlePermissionResult(
                permissions.keys.toTypedArray(),
                grantResults,
                onPermissionsGranted = { if (ImageUtils.saveViewToGallery(requireContext(), binding.ivProfile)) showSaveSuccessDialog() },
                onPermissionsDenied = {
                    CustomDialog.getInstance(CustomDialog.DialogType.DENIED_PERMISSION, null).apply {
                        setButtonClickListener(object : CustomDialog.OnButtonClickListener{
                            override fun onButton1Clicked() {}
                            override fun onButton2Clicked() {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", requireActivity().packageName, null)
                                }
                                requireActivity().startActivity(intent)
                                dismiss()
                            }
                        })
                    }.show(requireActivity().supportFragmentManager, "")
                }
            )
        }
    }

    private fun showSaveSuccessDialog() {
        CustomDialog.getInstance(CustomDialog.DialogType.SAVE_PROFILE, null).show(requireActivity().supportFragmentManager, "")
    }
}
