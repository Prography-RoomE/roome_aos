package com.sevenstars.roome.view.profile.mbti

import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.base.RoomeApplication
import com.sevenstars.roome.databinding.FragmentProfileMbtiBinding
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.strength.ProfileStrengthFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileMbtiFragment: BaseFragment<FragmentProfileMbtiBinding>(R.layout.fragment_profile_mbti) {
    private lateinit var mbtiAdatper: ProfileMbtiGvAdapter
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val viewModel: ProfileMbtiViewModel by viewModels()

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
                viewModel.saveData(buildString {
                    for(key in 1..4) append(mbtiAdatper.checkedItems[key] ?: "")
                })
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

    override fun observer() {
        super.observer()

        viewModel.uiState.observe(viewLifecycleOwner){
            when(it){
                is UiState.Failure -> {
                    LoggerUtils.error("저장 실패\n${it.message}")
                    Toast.makeText(RoomeApplication.app, "저장 실패\n${it.message}", Toast.LENGTH_SHORT).show()
                }
                is UiState.Loading -> {}
                is UiState.Success -> {
                    profileViewModel.selectedProfileData.mbti = if(mbtiAdatper.checkedItems.isEmpty()) null else mbtiAdatper.checkedItems.toList()
                    (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileStrengthFragment())
                    viewModel.setLoadingState()
                }
            }
        }
    }
}