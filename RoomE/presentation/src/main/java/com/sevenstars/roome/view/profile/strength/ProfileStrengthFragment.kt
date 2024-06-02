package com.sevenstars.roome.view.profile.strength

import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileStrengthBinding
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.SpaceItemDecoration
import com.sevenstars.roome.view.profile.important.ProfileImportantFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileStrengthFragment: BaseFragment<FragmentProfileStrengthBinding>(R.layout.fragment_profile_strength) {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val viewModel: ProfileStrengthViewModel by viewModels()
    private lateinit var strengthAdapter: ProfileStrengthRvAdapter

    override fun initView() {
        (requireActivity() as ProfileActivity).setStep(4)

        strengthAdapter = ProfileStrengthRvAdapter().apply {
            this.setItemClickListener(object : ProfileStrengthRvAdapter.OnItemClickListener{
                override fun onClick(isFull: Boolean) {
                    if(isFull) showToast("최대 2개까지 선택할 수 있어요.")
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
                viewModel.saveData(strengthAdapter.checked.map { it.id })
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

    override fun observer() {
        super.observer()

        viewModel.uiState.observe(viewLifecycleOwner){

            when(it){
                is UiState.Failure -> {
                    LoggerUtils.error("저장 실패\n${it.message}")
                    showToast("저장 실패\n${it.message}")
                }
                is UiState.Loading -> {}
                is UiState.Success -> {
                    profileViewModel.selectedProfileData.strengths = strengthAdapter.checked
                    (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileImportantFragment(), null)
                    viewModel.setLoadingState()
                }
            }
        }
    }
}