package com.sevenstars.roome.view.main.profile.edit

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.custom.CustomDialog
import com.sevenstars.roome.databinding.FragmentUserProfileEditBinding
import com.sevenstars.roome.utils.ImageUtils
import com.sevenstars.roome.utils.PermissionManager
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class UserProfileEditFragment(
    private val nickname: String,
    private val imageUrl: String
) : BaseFragment<FragmentUserProfileEditBinding>(R.layout.fragment_user_profile_edit) {
    private val viewModel: UserProfileEditViewModel by viewModels()

    private lateinit var permissionManager: PermissionManager
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>

    private val requiredCameraPermission = arrayOf(Manifest.permission.CAMERA)
    private val requiredGalleryPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLaunchers()
    }

    private fun setupLaunchers() {
        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleGalleryResult(result.resultCode, result.data)
        }

        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleCameraResult(result.resultCode, result.data)
        }
    }

    private fun handleGalleryResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                val realPath = ImageUtils.getRealPathFromURI(requireContext(), it)
                viewModel.updateImage = realPath
                setUserImage(realPath)
            }
        }
    }

    private fun handleCameraResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val photo = data?.extras?.get("data") as Bitmap
            val photoUri = ImageUtils.getImageUri(requireContext(), photo)
            val realPath = ImageUtils.getRealPathFromURI(requireContext(), photoUri)
            viewModel.updateImage = realPath
            setUserImage(realPath)
        }
    }

    override fun initView() {
        (requireActivity() as MainActivity).setBottomNaviVisibility(false)
        binding.btnClear.visibility = View.VISIBLE
        binding.etNickname.setText(nickname)
        setUserImage(imageUrl)
        setPermissionLauncher()
    }

    private fun setUserImage(image: String?) {
        image?.takeIf { it.isNotEmpty() }?.let {
            Glide.with(requireContext())
                .load(it)
                .optionalCircleCrop()
                .into(binding.ivUserProfile)
        }
    }

    override fun initListener() {
        super.initListener()
        setupProfileEditListener()
        setupBackButtonListener()
        setupNextButtonListener()
        setupClearButtonListener()
        setupNicknameEditTextListener()
    }

    private fun setupProfileEditListener() {
        binding.ibUserProfileEdit.setOnClickListener {
            UserProfileBottomSheetDialog.getInstance(object : UserProfileBottomSheetDialog.ButtonClickListener {
                override fun onButtonClick(buttonName: String) {
                    handleProfileEditButtonClick(buttonName)
                }
            }).show(requireActivity().supportFragmentManager, "")
        }
    }

    private fun handleProfileEditButtonClick(buttonName: String) {
        when (buttonName) {
            "TakePicture" -> openCamera()
            "Gallery" -> openGallery()
            "Default" -> setDefaultUserImage()
        }
    }

    private fun setDefaultUserImage() {
        viewModel.updateImage = ""
        binding.ivUserProfile.setImageResource(R.drawable.png_default_profile)
    }

    private fun setupBackButtonListener() {
        binding.ibBack.setOnClickListener {
            if (viewModel.updateImage != null || binding.etNickname.text.toString() != nickname) {
                showProfileEditDialog()
            } else {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun showProfileEditDialog() {
        CustomDialog.getInstance(CustomDialog.DialogType.PROFILE_EDIT, null).apply {
            setButtonClickListener(object : CustomDialog.OnButtonClickListener {
                override fun onButton1Clicked() {}
                override fun onButton2Clicked() {
                    dismiss()
                    requireActivity().supportFragmentManager.popBackStack()
                }
            })
        }.show(requireActivity().supportFragmentManager, "")
    }

    private fun setupNextButtonListener() {
        binding.btnNext.setOnClickListener {
            if (binding.etNickname.text.toString() != nickname) {
                viewModel.checkNick(binding.etNickname.text.toString())
            } else if (viewModel.updateImage != null) {
                handleImageUpdate()
            } else {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun handleImageUpdate() {
        if (viewModel.updateImage!!.isEmpty()) {
            viewModel.deleteUserImage()
        } else {
            viewModel.postUserImage(viewModel.updateImage!!)
        }
    }

    private fun setupClearButtonListener() {
        binding.btnClear.setOnClickListener {
            binding.etNickname.text.clear()
        }
    }

    private fun setupNicknameEditTextListener() {
        binding.etNickname.apply {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    handleNicknameTextChanged(p0)
                }
            })
            filters = arrayOf(
                InputFilter { source, _, _, _, _, _ ->
                    val ps: Pattern = Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$")
                    if (source == "" || ps.matcher(source).matches()) source else ""
                },
                InputFilter.LengthFilter(8)
            )
        }
    }

    private fun handleNicknameTextChanged(p0: Editable?) {
        val length = p0.toString().length
        binding.btnClear.visibility = if (length != 0) View.VISIBLE else View.GONE
        binding.tvNickname.setTextColor(requireContext().getColor(R.color.on_surface))
        binding.tvNicknameFeedback.apply {
            text = getText(R.string.signup_nickname_default)
            setTextColor(requireContext().getColor(R.color.on_surface))
        }
    }

    override fun observer() {
        super.observer()
        viewModel.checkState.observe(viewLifecycleOwner) { handleCheckState(it) }
        viewModel.postState.observe(viewLifecycleOwner) { handlePostState(it) }
        viewModel.deleteState.observe(viewLifecycleOwner) { handleDeleteState(it) }
    }

    private fun handleCheckState(state: UiState<Unit>) {
        when (state) {
            is UiState.Failure -> handleFailureState(state)
            is UiState.Loading -> {}
            is UiState.Success -> handleSuccessState()
        }
    }

    private fun handleFailureState(state: UiState.Failure) {
        LoggerUtils.error(state.message)
        if (state.code == 0) showNoConnectionDialog(R.id.fl_signup)

        binding.tvNickname.setTextColor(requireContext().getColor(R.color.error))
        binding.btnClear.visibility = View.VISIBLE
        binding.btnClear.setImageResource(R.drawable.ic_error)
        binding.tvNicknameFeedback.apply {
            text = state.message
            setTextColor(requireContext().getColor(R.color.error))
        }
    }

    private fun handleSuccessState() {
        handleImageUpdate()
    }

    private fun handlePostState(state: UiState<String>) {
        when (state) {
            is UiState.Failure -> showToast(state.message)
            is UiState.Loading -> {}
            is UiState.Success -> requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun handleDeleteState(state: UiState<Unit>) {
        when (state) {
            is UiState.Failure -> showToast(state.message)
            is UiState.Loading -> {}
            is UiState.Success -> requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setPermissionLauncher() {
        permissionManager = PermissionManager(requireActivity() as MainActivity)
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            handlePermissionResult(permissions)
        }
    }

    private fun handlePermissionResult(permissions: Map<String, Boolean>) {
        val grantResults = permissions.values.map { if (it) PackageManager.PERMISSION_GRANTED else PackageManager.PERMISSION_DENIED }.toIntArray()
        permissionManager.handlePermissionResult(
            permissions.keys.toTypedArray(),
            grantResults,
            onPermissionsGranted = {},
            onPermissionsDenied = { showDeniedPermissionDialog() }
        )
    }

    private fun showDeniedPermissionDialog() {
        CustomDialog.getInstance(CustomDialog.DialogType.DENIED_PERMISSION, null).apply {
            setButtonClickListener(object : CustomDialog.OnButtonClickListener {
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

    private fun openGallery() {
        if (permissionManager.checkPermissions(requiredGalleryPermissions)) {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            galleryLauncher.launch(intent)
        } else {
            permissionManager.requestPermissions(permissionLauncher, requiredGalleryPermissions)
        }
    }

    private fun openCamera() {
        if (permissionManager.checkPermissions(requiredCameraPermission)) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(intent)
        } else {
            permissionManager.requestPermissions(permissionLauncher, requiredCameraPermission)
        }
    }
}
