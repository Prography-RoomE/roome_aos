package com.sevenstars.roome.view.signup

import android.content.Intent
import android.net.Uri
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentSignupAgreeBinding
import com.sevenstars.roome.utils.UiState

class SignupAgreeFragment: BaseFragment<FragmentSignupAgreeBinding>(R.layout.fragment_signup_agree) {
    private val viewModel: SignUpViewModel by activityViewModels()

    override fun initView() {

    }

    override fun initListener() {
        super.initListener()

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btnNext.setOnClickListener {
            if(binding.cbAgreeAge.isChecked && binding.cbAgreeService.isChecked && binding.cbAgreePrivacy.isChecked) (requireActivity() as SignUpActivity).moveToMain()
        }

        binding.apply {
            ibOpenService.setOnClickListener{openBrowser("Service")}
            ibOpenPrivacy.setOnClickListener{openBrowser("Privacy")}
            ibOpenAd.setOnClickListener{openBrowser("Ad")}
        }

        val checkedChangeListener = CompoundButton.OnCheckedChangeListener { _, _ ->
            with(binding){
                if(cbAgreeAge.isChecked && cbAgreeService.isChecked && cbAgreePrivacy.isChecked){
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

        binding.apply {
            cbAgreeAge.setOnCheckedChangeListener(checkedChangeListener)
            cbAgreeService.setOnCheckedChangeListener(checkedChangeListener)
            cbAgreePrivacy.setOnCheckedChangeListener(checkedChangeListener)
            cbAgreeAd.setOnCheckedChangeListener(checkedChangeListener)
        }
    }

    override fun observer() {
        super.observer()

        viewModel.uiState.observe(this){
            when(it){
                is UiState.Failure -> {
                    showToast(it.message)
                    LoggerUtils.error(it.message)
                }
                is UiState.Loading -> {}
                is UiState.Success -> {

                }
            }
        }
    }

    private fun openBrowser(target: String){
        val webpage = when(target){
            "Service" -> "https://google.com"
            "Privacy" -> "https://google.com"
            "Ad" -> "https://google.com"
            else -> "https://google.com"
        }

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webpage))
        if(intent.resolveActivity(requireActivity().packageManager) != null) startActivity(intent)
    }
}