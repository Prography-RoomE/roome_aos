package com.sevenstars.roome.view.profile.count

import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileCountBinding
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.genres.ProfileGenresFragment

class ProfileCountFragment: BaseFragment<FragmentProfileCountBinding>(R.layout.fragment_profile_count) {
    override fun initView() {
        (requireActivity() as ProfileActivity).apply {
            setToolbarVisibility(true)
            setStep(1)
        }
    }

    override fun initListener() {
        super.initListener()

        binding.btnNext.setOnClickListener {
            (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileGenresFragment())
        }
    }
}