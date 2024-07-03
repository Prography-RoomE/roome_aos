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
import android.view.RoundedCorner
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.custom.CustomDialog
import com.sevenstars.roome.databinding.FragmentUserProfileEditBinding
import com.sevenstars.roome.utils.Constants.CAMERA_REQUEST_CODE
import com.sevenstars.roome.utils.Constants.GALLERY_REQUEST_CODE
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
): BaseFragment<FragmentUserProfileEditBinding>(R.layout.fragment_user_profile_edit) {
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

        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImageUri: Uri? = result.data?.data
                selectedImageUri?.let { viewModel.postUserImage(ImageUtils.getRealPathFromURI(requireContext(), selectedImageUri)) }
            }
        }

        cameraLauncher =   registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val photo = result.data?.extras?.get("data") as Bitmap
                val photoUri = ImageUtils.getImageUri(requireContext(), photo)
                viewModel.postUserImage(ImageUtils.getRealPathFromURI(requireContext(), photoUri))
            }
        }
    }

    override fun initView() {
        (requireActivity() as MainActivity).setBottomNaviVisibility(false)
        binding.btnClear.visibility = View.VISIBLE
        binding.etNickname.setText(nickname)
        setUserImage()
        setPermissionLauncher()
    }

    private fun setUserImage(){
        if(imageUrl.isNotEmpty()){
            Glide.with(requireContext())
                .load(imageUrl)
                .optionalCircleCrop()
                .into(binding.ivUserProfile)
        }
    }

    override fun initListener() {
        super.initListener()

        binding.ibUserProfileEdit.setOnClickListener {
            UserProfileBottomSheetDialog.getInstance(object : UserProfileBottomSheetDialog.ButtonClickListener{
                override fun onButtonClick(buttonName: String) {
                    when(buttonName) {
                        "TakePicture" -> {
                            openCamera()
                        }
                        "Gallery" -> {
                            openGallery()
                        }
                        "Default" -> {
                            viewModel.deleteUserImage()
                        }
                    }
                }
            }).show(requireActivity().supportFragmentManager, "")
        }

        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btnNext.setOnClickListener {
            if(binding.etNickname.text.toString() != nickname){
                viewModel.checkNick(binding.etNickname.text.toString())
            } else {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }

        binding.btnClear.setOnClickListener {
            binding.etNickname.text.clear()
        }

        binding.etNickname.apply {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    val length = p0.toString().length

                    binding.btnClear.visibility = if (length != 0) View.VISIBLE else View.GONE
                    binding.tvNickname.setTextColor(requireContext().getColor(R.color.on_surface))
                    binding.tvNicknameFeedback.apply {
                        text = getText(R.string.signup_nickname_default)
                        setTextColor(requireContext().getColor(R.color.on_surface))
                    }
                }
            })

            filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
                val ps: Pattern =
                    Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$")
                if (source == "" || ps.matcher(source).matches()) return@InputFilter source
                ""
            }, InputFilter.LengthFilter(8))
        }
    }

    override fun observer() {
        super.observer()

        viewModel.checkState.observe(viewLifecycleOwner){
            when(it){
                is UiState.Failure -> {
                    LoggerUtils.error(it.message)
                    if(it.code == 0) showNoConnectionDialog(R.id.fl_signup)

                    binding.tvNickname.setTextColor(requireContext().getColor(R.color.error))
                    binding.btnClear.visibility = View.VISIBLE
                    binding.btnClear.setImageResource(R.drawable.ic_error)
                    binding.tvNicknameFeedback.apply {
                        text = it.message
                        setTextColor(requireContext().getColor(R.color.error))
                    }
                }
                is UiState.Loading -> {}
                is UiState.Success -> {
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        }

        viewModel.postState.observe(viewLifecycleOwner){
            when(it){
                is UiState.Failure -> {
                    showToast(it.message)
                }
                is UiState.Loading -> {}
                is UiState.Success -> {
                    if(it.data.isNotEmpty()){
                        Glide.with(requireContext())
                            .load(it.data)
                            .optionalCircleCrop()
                            .into(binding.ivUserProfile)
                    }
                }
            }
        }

        viewModel.deleteState.observe(viewLifecycleOwner){
            when(it){
                is UiState.Failure -> {
                    showToast(it.message)
                }
                is UiState.Loading -> {}
                is UiState.Success -> {
                    binding.ivUserProfile.setImageResource(R.drawable.png_default_profile)
                }
            }
        }
    }

    private fun setPermissionLauncher() {
        permissionManager = PermissionManager(requireActivity() as MainActivity)
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val grantResults = permissions.values.map { if (it) PackageManager.PERMISSION_GRANTED else PackageManager.PERMISSION_DENIED }.toIntArray()
            permissionManager.handlePermissionResult(
                permissions.keys.toTypedArray(),
                grantResults,
                onPermissionsGranted = { },
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

    private fun openGallery() {
        if(permissionManager.checkPermissions(requiredGalleryPermissions)){
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            galleryLauncher.launch(intent)
        } else {
            permissionManager.requestPermissions(permissionLauncher, requiredGalleryPermissions)
        }
    }

    private fun openCamera() {
        if(permissionManager.checkPermissions(requiredCameraPermission)){
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(intent)
        } else {
            permissionManager.requestPermissions(permissionLauncher, requiredCameraPermission)
        }
    }
}