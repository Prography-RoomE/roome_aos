package com.sevenstars.roome.view.signup

import android.content.Intent
import android.net.Uri
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentSignupAgreeBinding

class SignupAgreeFragment: BaseFragment<FragmentSignupAgreeBinding>(R.layout.fragment_signup_agree) {
    private val viewModel: SignUpViewModel by activityViewModels()

    override fun initView() {}

    override fun initListener() {
        super.initListener()

        binding.btnBack.setOnClickListener {
            (requireActivity() as SignUpActivity).moveToSignIn()
        }

        binding.btnNext.setOnClickListener {
            viewModel.isMarketingAgree = binding.cbAgreeAd.isChecked

            if(binding.cbAgreeAge.isChecked && binding.cbAgreeService.isChecked && binding.cbAgreePrivacy.isChecked) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_signup, SignupNickFragment())
                    .addToBackStack(null)
                    .commit()
            }
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