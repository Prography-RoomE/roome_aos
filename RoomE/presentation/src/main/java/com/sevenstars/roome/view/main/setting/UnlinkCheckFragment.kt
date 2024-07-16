package com.sevenstars.roome.view.main.setting

import android.text.SpannableStringBuilder
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.sevenstars.data.service.auth.KakaoAuthService
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.domain.enums.Provider
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.base.RoomeApplication.Companion.app
import com.sevenstars.roome.custom.CustomDialog
import com.sevenstars.roome.custom.IndentLeadingMarginSpan
import com.sevenstars.roome.databinding.FragmentUnlinkCheckBinding
import com.sevenstars.roome.utils.AnalyticsHelper
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UnlinkCheckFragment: BaseFragment<FragmentUnlinkCheckBinding>(R.layout.fragment_unlink_check) {
    private val viewModel: UnlinkCheckViewModel by viewModels()

    @Inject
    lateinit var kakaoAuthService: KakaoAuthService

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    override fun initView() {
        AnalyticsHelper.logScreenView("exit_confirm")

        binding.tbUnlink.btnNext.visibility = View.GONE
        setIndentTextView()
    }

    override fun initListener() {
        super.initListener()

        binding.apply {
            tbUnlink.btnBack.setOnClickListener { requireActivity().supportFragmentManager.popBackStack() }
            binding.btnUnlink.setOnClickListener { if(cbOption.isChecked) unlink() }

            tvOption.setOnClickListener { cbOption.performClick() }
            cbOption.setOnCheckedChangeListener { _, isChecked ->
                btnUnlink.apply {
                    if(isChecked) {
                        backgroundTintList = AppCompatResources.getColorStateList(requireContext(), R.color.primary_primary)
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.surface))
                    } else {
                        backgroundTintList = AppCompatResources.getColorStateList(requireContext(), R.color.on_surface_a12)
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.on_surface_variant))
                    }
                }
            }
        }
    }

    override fun observer() {
        super.observer()

        viewModel.unlinkState.observe(viewLifecycleOwner){
            when(it){
                is UiState.Failure -> {
                    showToast("회원탈퇴 실패: ${it.message}")
                    showNoConnectionDialog(R.id.fl_main, isReplace = false)
                }
                is UiState.Loading -> {}
                is UiState.Success -> {
                    (requireActivity() as MainActivity).removeData()
                }
            }
        }
    }

    private fun setIndentTextView(){
        binding.tvCheckContext.text = SpannableStringBuilder(binding.tvCheckContext.text).apply {
            setSpan(IndentLeadingMarginSpan(), 0, length, 0)
        }
    }

    private fun unlink(){
        AnalyticsHelper.logButtonClick("exit_confirm")

        CustomDialog.getInstance(CustomDialog.DialogType.UNLINK, null).apply {
            setButtonClickListener(object: CustomDialog.OnButtonClickListener{
                override fun onButton1Clicked() { }
                override fun onButton2Clicked() {
                    AnalyticsHelper.logButtonClick("exit_final")

                    CoroutineScope(Dispatchers.IO).launch {
                        val provider = app.userPreferences.getLoginProvider().getOrNull().orEmpty()
                        launch(Dispatchers.Main){
                            if(provider == Provider.KAKAO.provider) unlinkKakao()
                            else unlinkGoogle()
                        }
                    }
                }
            })
        }.show(requireActivity().supportFragmentManager, "Unlink")
    }

    private fun unlinkKakao(){
        kakaoAuthService.withdraw { error ->
            if (error != null) {
                showToast("카카오 회원탈퇴 실패: ${error.message}")
            } else {
                LoggerUtils.info("카카오 회원탈퇴 성공")
                viewModel.unlink()
            }
        }
    }

    private fun unlinkGoogle(){
        googleSignInClient.revokeAccess()
            .addOnCompleteListener(requireActivity()){
                LoggerUtils.info("구글 회원탈퇴 성공")
                viewModel.unlink()
            }
            .addOnFailureListener{
                showToast("구글 회원탈퇴 실패: ${it.message}")
            }
    }
}