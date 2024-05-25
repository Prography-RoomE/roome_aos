package com.sevenstars.roome.view.profile.color

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sevenstars.domain.model.profile.info.Colors
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileColorBinding
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.generate.ProfileFragment
import com.sevenstars.roome.view.profile.ProfileViewModel


class ProfileColorFragment: BaseFragment<FragmentProfileColorBinding>(R.layout.fragment_profile_color) {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private lateinit var colorAdapter: ProfileColorRvAdapter

    override fun initView() {
        (requireActivity() as ProfileActivity).setStep(11)

        colorAdapter = ProfileColorRvAdapter().apply {
            this.setItemClickListener(object : ProfileColorRvAdapter.OnItemClickListener{
                override fun onClick(isChecked: Boolean, data: Colors) {
                    if(isChecked) {
                        profileViewModel.selectedProfileData.color = data
                        (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileFragment())
                    }
                }
            })
        }

        binding.rvColor.apply {
            adapter = colorAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
            addItemDecoration(SpaceColorItemDecoration(requireContext()))
        }

        colorAdapter.setData(profileViewModel.profileDefaultData.colors)
    }
}