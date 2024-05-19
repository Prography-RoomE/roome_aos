package com.sevenstars.roome.view.profile

import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileCountBinding

class ProfileCountFragment: BaseFragment<FragmentProfileCountBinding>(R.layout.fragment_profile_count) {
    override fun initView() {
        (requireActivity() as ProfileActivity).apply {
            setToolbarVisibility(true)
            setStep(1)
        }
    }

    override fun initListener() {
        super.initListener()

        binding.btnNext.setOnClickListener {  }
    }
}