package com.sevenstars.roome.view.signup

import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentSignupAgreeBinding
import com.sevenstars.roome.utils.Constants.PRIVACY_POLICY
import com.sevenstars.roome.utils.Constants.TERMS_OF_SERVICE
import com.sevenstars.roome.view.main.setting.WebViewFragment

class SignupAgreeFragment : BaseFragment<FragmentSignupAgreeBinding>(R.layout.fragment_signup_agree) {
    private val viewModel: SignUpViewModel by activityViewModels()

    override fun initView() {}

    override fun onResume() {
        super.onResume()
        setAgreementChecked()
    }

    private fun setAgreementChecked() {
        when (viewModel.updateAgreementTitle) {
            "서비스 이용약관" -> binding.cbAgreeService.performClick()
            "개인정보처리방침" -> binding.cbAgreePrivacy.performClick()
            "광고성 정보 수신 및 마케팅 활용" -> binding.cbAgreeAd.performClick()
        }
        viewModel.updateAgreementTitle = ""
    }

    override fun initListener() {
        super.initListener()
        setupClickListener()
        setupCheckedChangeListener()
    }

    private fun setupClickListener() {
        binding.apply {
            btnBack.setOnClickListener { navigateToSignIn() }
            btnNext.setOnClickListener { navigateToSignupNick() }

            ibOpenService.setOnClickListener { moveWebView("서비스 이용약관", TERMS_OF_SERVICE) }
            ibOpenPrivacy.setOnClickListener { moveWebView("개인정보처리방침", PRIVACY_POLICY) }
            ibOpenAd.setOnClickListener { moveWebView("광고성 정보 수신 및 마케팅 활용", "https://www.notion.so/ko-kr/product") }

            tvAgreeAll.setOnClickListener { cbAgreeAll.performClick() }
            tvAgreeAge.setOnClickListener { cbAgreeAge.performClick() }
            tvAgreePrivacy.setOnClickListener { cbAgreePrivacy.performClick() }
            tvAgreeService.setOnClickListener { cbAgreeService.performClick() }
            tvAgreeAd.setOnClickListener { cbAgreeAd.performClick() }
        }
    }

    private fun setupCheckedChangeListener() {
        val checkedChangeListener = CompoundButton.OnCheckedChangeListener { _, _ -> updateNextButtonState() }

        binding.apply {
            cbAgreeAge.setOnCheckedChangeListener(checkedChangeListener)
            cbAgreeService.setOnCheckedChangeListener(checkedChangeListener)
            cbAgreePrivacy.setOnCheckedChangeListener(checkedChangeListener)
            cbAgreeAd.setOnCheckedChangeListener(checkedChangeListener)
        }
    }

    private fun updateNextButtonState() {
        with(binding) {
            if (cbAgreeAge.isChecked && cbAgreeService.isChecked && cbAgreePrivacy.isChecked) {
                btnNext.apply {
                    backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.primary_primary)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.surface))
                }
            } else {
                btnNext.apply {
                    backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.on_surface_a12)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.on_surface_variant))
                }
            }
        }
    }

    private fun navigateToSignIn() {
        (requireActivity() as SignUpActivity).moveToSignIn()
    }

    private fun navigateToSignupNick() {
        viewModel.isMarketingAgree = binding.cbAgreeAd.isChecked

        if (binding.cbAgreeAge.isChecked && binding.cbAgreeService.isChecked && binding.cbAgreePrivacy.isChecked) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_signup, SignupNickFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun moveWebView(title: String, url: String) {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_signup, WebViewFragment(title, url, true))
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}
