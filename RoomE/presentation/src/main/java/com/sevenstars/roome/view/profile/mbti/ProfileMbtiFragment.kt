package com.sevenstars.roome.view.profile.mbti

import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.domain.enums.ProfileState
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileMbtiBinding
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.main.MainActivity
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.strength.ProfileStrengthFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileMbtiFragment(
    private val mbti: String? = null
): BaseFragment<FragmentProfileMbtiBinding>(R.layout.fragment_profile_mbti) {
    private lateinit var mbtiAdapter: ProfileMbtiGvAdapter
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val viewModel: ProfileMbtiViewModel by viewModels()

    private fun isProfileActivity() = requireActivity().localClassName == "view.profile.ProfileActivity"


    override fun initView() {
        mbtiAdapter = ProfileMbtiGvAdapter().apply {
            this.setItemClickListener(object : ProfileMbtiGvAdapter.OnItemClickListener{
                override fun onClick() {
                    setNextBtn()
                    if(mbtiAdapter.checkedItems.isNotEmpty()) binding.cbOption.isChecked = false
                }
            })
        }
        binding.gvMbit.adapter = mbtiAdapter

        if(isProfileActivity()) setupProfileActivity()
        else setupMainActivity()
    }

    private fun setupProfileActivity(){
        (requireActivity() as ProfileActivity).setStep(3)

        if(profileViewModel.selectedProfileData.mbti == null) mbtiAdapter.mbtiDisabled()
        else mbtiAdapter.checkedItems.putAll(profileViewModel.selectedProfileData.mbti!!)
        setNextBtn()
    }

    private fun setupMainActivity(){
        (requireActivity() as MainActivity).apply {
            setBottomNaviVisibility(false)
            setToolbarVisibility(true)
        }

        binding.btnNext.apply {
            setText(R.string.btn_save)
            backgroundTintList = AppCompatResources.getColorStateList(requireContext(), R.color.primary_primary)
            setTextColor(ContextCompat.getColor(requireContext(), R.color.surface))
        }

        mbtiAdapter.setCheckedItems(mbti!!)
        if(mbti == "-") binding.cbOption.isChecked = true
    }

    override fun initListener() {
        super.initListener()

        binding.btnNext.setOnClickListener {
            if(binding.btnNext.currentTextColor == ContextCompat.getColor(requireContext(), R.color.surface)){
                viewModel.saveData(buildString {
                    for(key in 1..4) append(mbtiAdapter.checkedItems[key] ?: "")
                })
            }
        }

        binding.tvOption.setOnClickListener {
            binding.cbOption.isChecked = !binding.cbOption.isChecked
        }

        binding.cbOption.setOnCheckedChangeListener { _, b ->
            if(b){
                mbtiAdapter.mbtiDisabled()

                binding.btnNext.apply {
                    backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.primary_primary)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.surface))
                }
            } else {
                mbtiAdapter.mbtiActivate()

                binding.btnNext.apply {
                    backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.btn_disabled)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.on_surface_variant))
                }
            }
        }
    }

    private fun setNextBtn(){
        binding.btnNext.apply {
            if(mbtiAdapter.checkedItems.size == 4){
                backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.primary_primary)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.surface))
            } else if (!mbtiAdapter.disabledState){
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
                    showToast("저장 실패\n${it.message}")
                    if(it.code == 0) showNoConnectionDialog(R.id.fl_profile)
                }
                is UiState.Loading -> {}
                is UiState.Success -> {
                    if (isProfileActivity()) {
                        profileViewModel.selectedProfileData.mbti = if(mbtiAdapter.checkedItems.isEmpty()) null else mbtiAdapter.checkedItems.toList()
                        (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileStrengthFragment(), null)
                        viewModel.setLoadingState()
                    } else {
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                }
            }
        }
    }
}