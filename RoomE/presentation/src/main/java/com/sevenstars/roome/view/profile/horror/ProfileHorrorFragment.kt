package com.sevenstars.roome.view.profile.horror

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.domain.enums.ProfileState
import com.sevenstars.domain.model.profile.info.HorrorThemePositions
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileHorrorBinding
import com.sevenstars.roome.utils.AnalyticsHelper
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.main.MainActivity
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.VerticalSpaceItemDecoration
import com.sevenstars.roome.view.profile.hint.ProfileHintFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileHorrorFragment(
    private val horrorThemePositions: HorrorThemePositions? = null
): BaseFragment<FragmentProfileHorrorBinding>(R.layout.fragment_profile_horror) {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val viewModel: ProfileHorrorViewModel by viewModels()
    private lateinit var horrorAdapter: ProfileHorrorRvAdapter

    private fun isProfileActivity() = requireActivity().localClassName == "view.profile.ProfileActivity"

    override fun initView() {
        AnalyticsHelper.logScreenView("position")

        horrorAdapter = ProfileHorrorRvAdapter().apply {
            this.setItemClickListener(object : ProfileHorrorRvAdapter.OnItemClickListener{
                override fun onClick(isFull: Boolean) {
                    if(isFull && isProfileActivity()) viewModel.saveData(horrorAdapter.checked!!.id)
                    else if(!isProfileActivity()) setNextBtn()
                }
            })
        }

        if(isProfileActivity()) setupProfileActivity()
        else setupMainActivity()
    }

    private fun setupProfileActivity() {
        (requireActivity() as ProfileActivity).apply {
            setToolbarVisibility(true)
            setStep(6)
        }
        if(profileViewModel.selectedProfileData.horror != null) horrorAdapter.checkedItem(profileViewModel.selectedProfileData.horror!!)
        setHorrorRv()
    }

    private fun setupMainActivity() {
        (requireActivity() as MainActivity).apply {
            setBottomNaviVisibility(false)
            setToolbarVisibility(true)
        }
        profileViewModel.fetchDefaultData(ProfileState.COMPLETE)
        binding.flSaveBtn.visibility = View.VISIBLE
        horrorAdapter.checkedItem(horrorThemePositions!!)
    }

    private fun setHorrorRv(){
        binding.rvHorror.apply {
            adapter = horrorAdapter
            layoutManager = GridLayoutManager(requireContext(), 1)
            addItemDecoration(VerticalSpaceItemDecoration(requireContext(), 12, !isProfileActivity()))
        }

        horrorAdapter.setData(profileViewModel.profileDefaultData.horrorThemePositions)
    }

    override fun initListener() {
        super.initListener()

        binding.btnNext.setOnClickListener {
            if(horrorAdapter.checked != null){
                viewModel.saveData(horrorAdapter.checked!!.id)
            }
        }
    }

    private fun setNextBtn(){
        binding.btnNext.apply {
            if(horrorAdapter.checked != null){
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
                        profileViewModel.selectedProfileData.horror = horrorAdapter.checked
                        (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileHintFragment(), null)
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
                        setHorrorRv()
                    }
                }
            }
        }
    }
}