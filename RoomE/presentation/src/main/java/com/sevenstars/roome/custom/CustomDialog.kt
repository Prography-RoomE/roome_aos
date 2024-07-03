package com.sevenstars.roome.custom

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.R
import com.sevenstars.roome.databinding.DialogCustomBinding
import com.sevenstars.roome.utils.GetDisplayUtil

class CustomDialog private constructor(
    private val dialogType: DialogType,
    private val msg: String?
) : DialogFragment() {

    enum class DialogType {
        PROFILE_CONTINUE,
        SAVE_PROFILE,
        SIGN_OUT,
        UNLINK,
        UNLINK_SUCCESS,
        NO_CONNECTION,
        DENIED_PERMISSION,
        PROFILE_EDIT
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
            DialogType.SAVE_PROFILE -> setSaveProfile()
            DialogType.SIGN_OUT -> setSignOut()
            DialogType.UNLINK -> setUnlink()
            DialogType.UNLINK_SUCCESS -> setUnlinkSuccess()
            DialogType.NO_CONNECTION -> setNoConnection()
            DialogType.DENIED_PERMISSION -> setDeniedPermission()
            DialogType.PROFILE_EDIT -> setProfileEdit()
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
        binding.tvDialogTitle.text = "제작 중인 프로필이 있어요"
        binding.tvDialogContent.text = "이어서 만드시겠어요?"
        binding.btnDialog1.text = "처음부터 하기"
        binding.btnDialog2.text = "이어서 하기"

        binding.btnDialog1.setOnClickListener {
            buttonClickListener?.onButton1Clicked()
            dismiss()
        }

        binding.btnDialog2.setOnClickListener {
            buttonClickListener?.onButton2Clicked()
            dismiss()
        }
    }

    private fun setSaveProfile() {
        binding.tvDialogTitle.text = "저장 완료"
        binding.tvDialogContent.text = "내 사진에 저장되었어요."
        binding.btnDialog1.text = "확인"
        binding.btnDialog1.backgroundTintList = AppCompatResources.getColorStateList(requireContext(), R.color.primary_primary)
        binding.btnDialog1.setTextColor(requireContext().getColor(R.color.surface))

        binding.btnDialog1.setOnClickListener {
            dismiss()
        }

        binding.btnDialog2.visibility = View.GONE
    }

    private fun setSignOut() {
        binding.tvDialogTitle.text = "로그아웃"
        binding.tvDialogContent.text = "정말 로그아웃하시겠어요?"
        binding.btnDialog1.text = "취소"
        binding.btnDialog2.text = "로그아웃"

        binding.btnDialog1.setOnClickListener {
            dismiss()
        }

        binding.btnDialog2.setOnClickListener {
            buttonClickListener?.onButton2Clicked()
            dismiss()
        }
    }

    private fun setUnlink() {
        binding.tvDialogTitle.text = "정말로 탈퇴하시겠어요?"
        binding.tvDialogContent.text = "지금까지 작성한 정보가 모두 삭제되고,\n복구할 수 없어요."
        binding.btnDialog1.text = "취소"
        binding.btnDialog2.text = "탈퇴"

        binding.btnDialog1.setOnClickListener {
            dismiss()
        }

        binding.btnDialog2.setOnClickListener {
            buttonClickListener?.onButton2Clicked()
            dismiss()
        }
    }

    private fun setUnlinkSuccess(){
        binding.tvDialogTitle.text = "탈퇴 완료"
        binding.tvDialogContent.text = "탈퇴 처리가 성공적으로 완료되었습니다."
        binding.btnDialog1.text = "확인"
        binding.btnDialog1.backgroundTintList = AppCompatResources.getColorStateList(requireContext(), R.color.primary_primary)
        binding.btnDialog1.setTextColor(requireContext().getColor(R.color.surface))

        binding.btnDialog1.setOnClickListener {
            dismiss()
        }

        binding.btnDialog2.visibility = View.GONE
    }

    private fun setNoConnection(){
        binding.tvDialogTitle.text = "인터넷에 연결할 수 없어요"
        binding.tvDialogContent.text = "다시 시도하거나 네트워크 설정을 확인해주세요."
        binding.btnDialog1.text = "다시 시도"
        binding.btnDialog2.text = "설정"

        binding.btnDialog1.setOnClickListener {
            buttonClickListener?.onButton1Clicked()
        }

        binding.btnDialog2.setOnClickListener {
            buttonClickListener?.onButton2Clicked()
        }
    }

    private fun setDeniedPermission(){
        binding.tvDialogTitle.text = "권한을 허용해 주세요"
        binding.tvDialogContent.text = "사진 권한을 허용해야\n이미지를 저장할 수 있어요."
        binding.btnDialog1.text = "취소"
        binding.btnDialog2.text = "확인"

        binding.btnDialog1.setOnClickListener {
            dismiss()
        }

        binding.btnDialog2.setOnClickListener {
            buttonClickListener?.onButton2Clicked()
        }
    }

    private fun setProfileEdit(){
        binding.tvDialogTitle.text = "변경사항이 있어요"
        binding.tvDialogContent.text = "변경사항을 저장하지 않고 나가시겠어요?"
        binding.btnDialog1.text = "취소"
        binding.btnDialog2.text = "나가기"

        binding.btnDialog1.setOnClickListener {
            dismiss()
        }

        binding.btnDialog2.setOnClickListener {
            buttonClickListener?.onButton2Clicked()
        }
    }
}
