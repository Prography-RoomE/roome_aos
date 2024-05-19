package com.sevenstars.roome.view.profile

import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentWelcomeBinding

class ProfileWelcomeFragment: BaseFragment<FragmentWelcomeBinding>(R.layout.fragment_welcome) {
    override fun initView() {
        (requireActivity() as ProfileActivity).setToolbarVisibility(false)
        binding.tvTitle.text = getString(R.string.profile_welcome_title, "Roome")
    }

    override fun initListener() {
        super.initListener()

        binding.btnCreate.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_profile, ProfileCountFragment())
                .commit()
        }
    }
}