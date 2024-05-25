package com.sevenstars.roome.view.profile.mbti

import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileMbtiBinding
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.strength.ProfileStrengthFragment

class ProfileMbtiFragment: BaseFragment<FragmentProfileMbtiBinding>(R.layout.fragment_profile_mbti) {
    private lateinit var mbtiAdatper: ProfileMbtiGvAdapter
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun initView() {
        (requireActivity() as ProfileActivity).setStep(3)

        mbtiAdatper = ProfileMbtiGvAdapter().apply {
            this.setItemClickListener(object : ProfileMbtiGvAdapter.OnItemClickListener{
                override fun onClick() {
                    setNextBtn()
                }
            })
        }

        if(profileViewModel.selectedProfileData.mbti == null) mbtiAdatper.mbtiDisabled()
        else mbtiAdatper.checkedItems.putAll(profileViewModel.selectedProfileData.mbti!!)
        setNextBtn()
        binding.gvMbit.adapter = mbtiAdatper
    }

    override fun initListener() {
        super.initListener()

        binding.btnNext.setOnClickListener {
            if(binding.btnNext.currentTextColor == ContextCompat.getColor(requireContext(), R.color.surface)){
                profileViewModel.selectedProfileData.mbti = if(mbtiAdatper.checkedItems.isEmpty()) null else mbtiAdatper.checkedItems.toList()
                (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileStrengthFragment())
            }
        }

        binding.cbOption.setOnCheckedChangeListener { _, b ->
            LoggerUtils.debug(b.toString())
            if(b){
                mbtiAdatper.mbtiDisabled()

                binding.btnNext.apply {
                    backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.primary_primary)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.surface))
                }
            } else {
                mbtiAdatper.mbtiActivate()

                binding.btnNext.apply {
                    backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.btn_disabled)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.on_surface_variant))
                }
            }
        }
    }

    private fun setNextBtn(){
        binding.btnNext.apply {
            if(mbtiAdatper.checkedItems.size == 4){
                backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.primary_primary)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.surface))
            } else if (!mbtiAdatper.disabledState){
                backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.btn_disabled)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.on_surface_variant))
            }
        }
    }
}