package com.sevenstars.roome.view.profile.count

import androidx.fragment.app.activityViewModels
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileCountBinding
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.genres.ProfileGenresFragment

class ProfileCountFragment: BaseFragment<FragmentProfileCountBinding>(R.layout.fragment_profile_count) {
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun initView() {
        (requireActivity() as ProfileActivity).apply {
            setToolbarVisibility(true)
            setStep(1)
        }

        binding.etCount.setText(profileViewModel.selectedProfileData.count.toString())
    }

    override fun initListener() {
        super.initListener()

        binding.btnNext.setOnClickListener {
            profileViewModel.selectedProfileData.count = binding.etCount.text.toString().toInt()
            (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileGenresFragment())
        }
    }
}