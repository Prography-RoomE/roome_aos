package com.sevenstars.roome.view.profile.hint

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileHintBinding
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.VerticalSpaceItemDecoration
import com.sevenstars.roome.view.profile.device.ProfileDeviceFragment

class ProfileHintFragment: BaseFragment<FragmentProfileHintBinding>(R.layout.fragment_profile_hint) {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private lateinit var hintAdapter: ProfileHintRvAdapter

    override fun initView() {
        (requireActivity() as ProfileActivity).setStep(7)

        hintAdapter = ProfileHintRvAdapter().apply {
            this.setItemClickListener(object : ProfileHintRvAdapter.OnItemClickListener{
                override fun onClick(isChecked: Boolean) {
                    if(isChecked) (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileDeviceFragment())
                }
            })
        }

        binding.rvHint.apply {
            adapter = hintAdapter
            layoutManager = GridLayoutManager(requireContext(), 1)
            addItemDecoration(VerticalSpaceItemDecoration(requireContext(), 12))
        }

        hintAdapter.setData(profileViewModel.profileDefaultData.hintUsagePreferences)
    }

    override fun initListener() {
        super.initListener()

    }
}