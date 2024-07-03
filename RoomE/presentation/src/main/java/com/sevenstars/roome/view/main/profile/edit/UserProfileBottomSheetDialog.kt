package com.sevenstars.roome.view.main.profile.edit

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sevenstars.roome.R
import com.sevenstars.roome.custom.CustomToast
import com.sevenstars.roome.databinding.BottomSheetDialogUserProfileBinding
import com.sevenstars.roome.utils.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileBottomSheetDialog: BottomSheetDialogFragment() {

    interface ButtonClickListener {
        fun onButtonClick(buttonName: String)
    }

    companion object {
        private var buttonClickListener: ButtonClickListener? = null
        private var instance: UserProfileBottomSheetDialog? = null

        fun getInstance(listener: ButtonClickListener): UserProfileBottomSheetDialog {
            return instance ?: synchronized(this) {
                instance ?: UserProfileBottomSheetDialog().also {
                    instance = it
                    buttonClickListener = listener
                }
            }
        }
    }

    private var _binding: BottomSheetDialogUserProfileBinding? = null
    val binding get() = _binding!!

    override fun getTheme(): Int {
        return R.style.RoundedCornersDialog
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetDialogUserProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
    }

    private fun initListener(){
        binding.apply {
            btnDone.setOnClickListener {
                dismiss()
            }
            btnTakePicture.setOnClickListener {
                buttonClickListener?.onButtonClick("TakePicture")
                dismiss()
            }
            btnGallery.setOnClickListener {
                buttonClickListener?.onButtonClick("Gallery")
                dismiss()
            }
            btnDefault.setOnClickListener {
                buttonClickListener?.onButtonClick("Default")
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }
}
