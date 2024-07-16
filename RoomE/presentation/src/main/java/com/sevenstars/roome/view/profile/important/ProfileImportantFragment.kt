package com.sevenstars.roome.view.profile.important

import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.domain.enums.ProfileState
import com.sevenstars.domain.model.profile.info.ImportantFactors
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileImportantFactorBinding
import com.sevenstars.roome.utils.AnalyticsHelper
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.main.MainActivity
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.horror.ProfileHorrorFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileImportantFragment(
    private val importantFactors: List<ImportantFactors>? = null
): BaseFragment<FragmentProfileImportantFactorBinding>(R.layout.fragment_profile_important_factor) {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val viewModel: ProfileImportantViewModel by viewModels()
    private lateinit var importantAdapter: ProfileImportantRvAdapter

    private fun isProfileActivity() = requireActivity().localClassName == "view.profile.ProfileActivity"

    override fun initView() {
        AnalyticsHelper.logScreenView("important_factor")

        importantAdapter = ProfileImportantRvAdapter().apply {
            this.setItemClickListener(object : ProfileImportantRvAdapter.OnItemClickListener{
                override fun onClick(isFull: Boolean) {
                    if(isFull) showToast("최대 2개까지 선택할 수 있어요.")
                   setNextBtn()
                }
            })
        }

        if(isProfileActivity()) setupProfileActivity()
        else setupMainActivity()
    }

    private fun setupProfileActivity(){
        (requireActivity() as ProfileActivity).setStep(5)

        importantAdapter.checked.addAll(profileViewModel.selectedProfileData.important)
        setImportantFactorRv()
        setNextBtn()
    }

    private fun setupMainActivity(){
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

        importantAdapter.setCheckedItem(importantFactors!!)
    }

    private fun setImportantFactorRv(){
        binding.rvImportant.apply {
            adapter = importantAdapter
            layoutManager = GridLayoutManager(requireContext(), 1)
        }

        importantAdapter.setData(profileViewModel.profileDefaultData.importantFactors)
    }

    override fun initListener() {
        super.initListener()

        binding.btnNext.setOnClickListener {
            if(binding.btnNext.currentTextColor == ContextCompat.getColor(requireContext(), R.color.surface)){
                AnalyticsHelper.logButtonClick("important_factor_next")
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
                    if(isProfileActivity()){
                        profileViewModel.selectedProfileData.important = importantAdapter.checked
                        (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileHorrorFragment(), null)
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
                        setImportantFactorRv()
                    }
                }
            }
        }
    }
}