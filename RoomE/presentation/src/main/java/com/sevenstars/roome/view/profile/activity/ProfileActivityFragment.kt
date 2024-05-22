package com.sevenstars.roome.view.profile.activity

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileActivitiyBinding
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.VerticalSpaceItemDecoration
import com.sevenstars.roome.view.profile.dislike.ProfileDislikeFragment

class ProfileActivityFragment: BaseFragment<FragmentProfileActivitiyBinding>(R.layout.fragment_profile_activitiy) {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private lateinit var activityAdapter: ProfileActivityRvAdapter

    override fun initView() {
        (requireActivity() as ProfileActivity).setStep(9)

        activityAdapter = ProfileActivityRvAdapter().apply {
            this.setItemClickListener(object : ProfileActivityRvAdapter.OnItemClickListener{
                override fun onClick(isChecked: Boolean) {
                    if(isChecked) (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileDislikeFragment())
                }
            })
        }

        binding.rvActivity.apply {
            adapter = activityAdapter
            layoutManager = GridLayoutManager(requireContext(), 1)
            addItemDecoration(VerticalSpaceItemDecoration(requireContext(), 12))
        }

        activityAdapter.setData(profileViewModel.profileData.activities)
    }

    override fun initListener() {
        super.initListener()

    }
}