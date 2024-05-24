package com.sevenstars.roome.view.profile.color

import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileActivitiyBinding
import com.sevenstars.roome.databinding.FragmentProfileColorBinding
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.ProfileWelcomeFragment
import com.sevenstars.roome.view.profile.SpaceItemDecoration
import com.sevenstars.roome.view.profile.VerticalSpaceItemDecoration
import com.sevenstars.roome.view.profile.dislike.ProfileDislikeFragment

class ProfileColorFragment: BaseFragment<FragmentProfileColorBinding>(R.layout.fragment_profile_color) {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private lateinit var colorAdapter: ProfileColorRvAdapter

    override fun initView() {
        (requireActivity() as ProfileActivity).setStep(11)

        colorAdapter = ProfileColorRvAdapter().apply {
            this.setItemClickListener(object : ProfileColorRvAdapter.OnItemClickListener{
                override fun onClick(isChecked: Boolean) {
                    if(isChecked) (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileWelcomeFragment())
                }
            })
        }

        binding.rvColor.apply {
            adapter = colorAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
            addItemDecoration(SpaceColorItemDecoration(requireContext()))
        }

        colorAdapter.setData(profileViewModel.profileData.colors)
    }

    override fun initListener() {
        super.initListener()

    }
}