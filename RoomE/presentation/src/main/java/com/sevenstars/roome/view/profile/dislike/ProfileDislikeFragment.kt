package com.sevenstars.roome.view.profile.dislike

import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileDislikeBinding
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.ProfileWelcomeFragment
import com.sevenstars.roome.view.profile.SpaceItemDecoration
import com.sevenstars.roome.view.profile.color.ProfileColorFragment

class ProfileDislikeFragment: BaseFragment<FragmentProfileDislikeBinding>(R.layout.fragment_profile_dislike) {
    private val viewModel: ProfileViewModel by activityViewModels()
    private lateinit var dislikeAdapter: ProfileDislikeRvAdapter

    override fun initView() {
        (requireActivity() as ProfileActivity).setStep(10)

        dislikeAdapter = ProfileDislikeRvAdapter().apply {
            this.setItemClickListener(object : ProfileDislikeRvAdapter.OnItemClickListener{
                override fun onClick() {
                    binding.btnNext.apply {
                        if(dislikeAdapter.checked.isNotEmpty()){
                            backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.primary_primary)
                            setTextColor(ContextCompat.getColor(requireContext(), R.color.surface))
                        } else {
                            backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.btn_disabled)
                            setTextColor(ContextCompat.getColor(requireContext(), R.color.on_surface_variant))
                        }
                    }
                }
            })
        }

        binding.rvDislike.apply {
            adapter = dislikeAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(SpaceItemDecoration(requireContext(), 6))
        }

        dislikeAdapter.setData(viewModel.profileData.dislikedFactors)
    }

    override fun observer() {
        super.observer()
    }

    override fun initListener() {
        super.initListener()

        binding.btnNext.setOnClickListener {
            LoggerUtils.info(dislikeAdapter.checked.joinToString(", "))
            if(binding.btnNext.currentTextColor == ContextCompat.getColor(requireContext(), R.color.surface)){
                (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileColorFragment())
            }
        }
    }

}