package com.sevenstars.roome.view.profile.important

import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileImportantFactorBinding
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.horror.ProfileHorrorFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileImportantFragment: BaseFragment<FragmentProfileImportantFactorBinding>(R.layout.fragment_profile_important_factor) {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val viewModel: ProfileImportantViewModel by viewModels()
    private lateinit var importantAdapter: ProfileImportantRvAdapter


    override fun initView() {
        (requireActivity() as ProfileActivity).setStep(5)

        importantAdapter = ProfileImportantRvAdapter().apply {
            this.setItemClickListener(object : ProfileImportantRvAdapter.OnItemClickListener{
                override fun onClick(isFull: Boolean) {
                    if(isFull) showToast("최대 2개까지 선택할 수 있어요.")
                   setNextBtn()
                }
            })

            checked.addAll(profileViewModel.selectedProfileData.important)
        }

        setNextBtn()

        binding.rvImportant.apply {
            adapter = importantAdapter
            layoutManager = GridLayoutManager(requireContext(), 1)
        }

        importantAdapter.setData(profileViewModel.profileDefaultData.importantFactors)
    }

    override fun initListener() {
        super.initListener()

        binding.btnNext.setOnClickListener {
            LoggerUtils.info(importantAdapter.checked.joinToString(", "))
            if(binding.btnNext.currentTextColor == ContextCompat.getColor(requireContext(), R.color.surface)){
                viewModel.saveData(importantAdapter.checked.map { it.id })
            }
        }
    }

    private fun setNextBtn(){
        binding.btnNext.apply {
            if(importantAdapter.checked.isNotEmpty()){
                backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.primary_primary)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.surface))
            } else {
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
                    profileViewModel.selectedProfileData.important = importantAdapter.checked
                    (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileHorrorFragment(), null)
                    viewModel.setLoadingState()
                }
            }
        }
    }
}