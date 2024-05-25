package com.sevenstars.roome.view.profile

import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentWelcomeBinding
import com.sevenstars.roome.view.profile.count.ProfileCountFragment

class ProfileWelcomeFragment: BaseFragment<FragmentWelcomeBinding>(R.layout.fragment_welcome) {
    override fun initView() {
        (requireActivity() as ProfileActivity).setToolbarVisibility(false)
        binding.tvTitle.text = getString(R.string.profile_welcome_title, "Roome")
    }

    override fun initListener() {
        super.initListener()

        binding.btnCreate.setOnClickListener {
            (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileCountFragment())
        }
    }
}