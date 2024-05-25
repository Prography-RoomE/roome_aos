package com.sevenstars.roome.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.databinding.DialogCustomBinding
import com.sevenstars.roome.utils.GetDisplayUtil

class CustomDialog private constructor(
    private val dialogType: DialogType,
    private val msg: String?
) : DialogFragment() {

    enum class DialogType {
        PROFILE_CONTINUE
    }

    companion object {
        @Volatile
        private var instance: CustomDialog? = null

        fun getInstance(dialogType: DialogType, msg: String?): CustomDialog {
            synchronized(this) {
                instance?.let {
                    try {
                        it.dismiss()
                    } catch (e: Exception) {
                        LoggerUtils.error("Error dismissing dialog: ${e.message}")
                    }
                }
                instance = CustomDialog(dialogType, msg)
                return instance!!
            }
        }
    }

    interface OnButtonClickListener {
        fun onButton1Clicked()
        fun onButton2Clicked()
    }

    private var buttonClickListener: OnButtonClickListener? = null

    fun setButtonClickListener(buttonClickListener: OnButtonClickListener) {
        this.buttonClickListener = buttonClickListener
    }

    private var _binding: DialogCustomBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        val size = GetDisplayUtil.getSize(requireContext())
        val width = (size.first * 0.85).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogCustomBinding.inflate(inflater, container, false)

        when (dialogType) {
            DialogType.PROFILE_CONTINUE -> setProfileContinue()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

    private fun setProfileContinue() {
        binding.tvDialogTitle.text = "제작 중인 프로필이 있습니다"
        binding.tvDialogContent.text = "이어서 만드시겠습니까?"
        binding.btnDialog1.text = "취소"
        binding.btnDialog2.text = "확인"

        binding.btnDialog1.setOnClickListener {
            buttonClickListener?.onButton1Clicked()
            dismiss()
        }

        binding.btnDialog2.setOnClickListener {
            buttonClickListener?.onButton2Clicked()
            dismiss()
        }
    }
}
