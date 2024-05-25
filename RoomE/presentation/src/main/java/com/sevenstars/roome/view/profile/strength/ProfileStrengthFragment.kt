package com.sevenstars.roome.view.profile.strength

import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileStrengthBinding
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.SpaceItemDecoration
import com.sevenstars.roome.view.profile.important.ProfileImportantFragment

class ProfileStrengthFragment: BaseFragment<FragmentProfileStrengthBinding>(R.layout.fragment_profile_strength) {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private lateinit var strengthAdapter: ProfileStrengthRvAdapter


    override fun initView() {
        (requireActivity() as ProfileActivity).setStep(4)

        strengthAdapter = ProfileStrengthRvAdapter().apply {
            this.setItemClickListener(object : ProfileStrengthRvAdapter.OnItemClickListener{
                override fun onClick() {
                    setNextBtn()
                }
            })

            checked.addAll(profileViewModel.selectedProfileData.strengths)
        }
        setNextBtn()

        binding.rvStrength.apply {
            adapter = strengthAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(SpaceItemDecoration(requireContext(), 6))
        }

        strengthAdapter.setData(profileViewModel.profileDefaultData.strengths)
    }

    override fun initListener() {
        super.initListener()

        binding.btnNext.setOnClickListener {
            LoggerUtils.info(strengthAdapter.checked.joinToString(", "))
            if(binding.btnNext.currentTextColor == ContextCompat.getColor(requireContext(), R.color.surface)){
                profileViewModel.selectedProfileData.strengths = strengthAdapter.checked
                (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileImportantFragment())
            }
        }
    }

    private fun setNextBtn(){
        binding.btnNext.apply {
            if(strengthAdapter.checked.isNotEmpty()){
                backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.primary_primary)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.surface))
            } else {
                backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.btn_disabled)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.on_surface_variant))
            }
        }
    }
}