package com.sevenstars.roome.view.profile.mbti

import androidx.core.content.ContextCompat
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileMbtiBinding
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileWelcomeFragment
import com.sevenstars.roome.view.profile.strength.ProfileStrengthFragment

class ProfileMbtiFragment: BaseFragment<FragmentProfileMbtiBinding>(R.layout.fragment_profile_mbti) {
    private lateinit var mbtiAdatper: ProfileMbtiGvAdapter

    override fun initView() {
        (requireActivity() as ProfileActivity).setStep(3)

        mbtiAdatper = ProfileMbtiGvAdapter().apply {
            this.setItemClickListener(object : ProfileMbtiGvAdapter.OnItemClickListener{
                override fun onClick() {
                    binding.btnNext.apply {
                        if(checkedItems.size == 4){
                            backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.primary_primary)
                            setTextColor(ContextCompat.getColor(requireContext(), R.color.surface))
                        } else if (!disabledState){
                            backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.btn_disabled)
                            setTextColor(ContextCompat.getColor(requireContext(), R.color.on_surface_variant))
                        }
                    }
                }
            })
        }
        binding.gvMbit.adapter = mbtiAdatper
    }

    override fun initListener() {
        super.initListener()

        binding.btnNext.setOnClickListener {
            if(binding.btnNext.currentTextColor == ContextCompat.getColor(requireContext(), R.color.surface)){
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
}