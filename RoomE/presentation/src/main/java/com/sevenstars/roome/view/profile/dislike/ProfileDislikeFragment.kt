package com.sevenstars.roome.view.profile.dislike

import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.domain.enums.ProfileState
import com.sevenstars.domain.model.profile.info.DislikedFactors
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileDislikeBinding
import com.sevenstars.roome.utils.AnalyticsHelper
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.main.MainActivity
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.SpaceItemDecoration
import com.sevenstars.roome.view.profile.color.ProfileColorFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileDislikeFragment(
    private val dislikedFactors: List<DislikedFactors>? = null
): BaseFragment<FragmentProfileDislikeBinding>(R.layout.fragment_profile_dislike) {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val viewModel: ProfileDislikeViewModel by viewModels()
    private lateinit var dislikeAdapter: ProfileDislikeRvAdapter

    private fun isProfileActivity() = requireActivity().localClassName == "view.profile.ProfileActivity"

    override fun initView() {
        AnalyticsHelper.logScreenView("dislike")

        dislikeAdapter = ProfileDislikeRvAdapter().apply {
            this.setItemClickListener(object : ProfileDislikeRvAdapter.OnItemClickListener{
                override fun onClick(isFull: Boolean) {
                    if(isFull) showToast("최대 2개까지 선택할 수 있어요.")
                    setNextBtn()
                }
            })
        }

        if(isProfileActivity()) setupProfileActivity()
        else setupMainActivity()
    }

    private fun setupProfileActivity() {
        (requireActivity() as ProfileActivity).apply {
            setToolbarVisibility(true)
            setStep(10)
        }
        dislikeAdapter.checked.addAll(profileViewModel.selectedProfileData.dislike)
        setDislikeRv()
        setNextBtn()
    }

    private fun setupMainActivity() {
        (requireActivity() as MainActivity).apply {
            setBottomNaviVisibility(false)
            setToolbarVisibility(true)
        }
        profileViewModel.fetchDefaultData(ProfileState.COMPLETE)

        binding.btnNext.apply {
            setText(R.string.btn_save)
            backgroundTintList = AppCompatResources.getColorStateList(requireContext(), R.color.primary_primary)
            setTextColor(ContextCompat.getColor(requireContext(), R.color.surface))
        }
        dislikeAdapter.setChecked(dislikedFactors!!)
    }

    private fun setDislikeRv(){
        binding.rvDislike.apply {
            adapter = dislikeAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(SpaceItemDecoration(requireContext(), 6))
        }

        dislikeAdapter.setData(profileViewModel.profileDefaultData.dislikedFactors)
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
                    if(isProfileActivity()){
                        profileViewModel.selectedProfileData.dislike = dislikeAdapter.checked
                        (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileColorFragment(), null)
                    } else {
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                    viewModel.setLoadingState()
                }
            }
        }

        if (requireActivity().localClassName == "view.main.MainActivity") {
            profileViewModel.profileDataState.observe(this) {
                when (it) {
                    is UiState.Failure -> {
                        LoggerUtils.error("프로필 생성에 필요한 데이터 조회 실패\n${it.message}")
                        showToast("프로필 생성에 필요한 데이터 조회 실패")
                    }
                    is UiState.Loading -> {}
                    is UiState.Success -> {
                        setDislikeRv()
                    }
                }
            }
        }
    }

    override fun initListener() {
        super.initListener()

        binding.btnNext.setOnClickListener {
            if(binding.btnNext.currentTextColor == ContextCompat.getColor(requireContext(), R.color.surface)){
                AnalyticsHelper.logButtonClick("dislike_factor")
                viewModel.saveData(dislikeAdapter.checked.map { it.id })
            }
        }
    }

    private fun setNextBtn(){
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
}