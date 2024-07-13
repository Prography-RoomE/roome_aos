package com.sevenstars.roome.view.profile.color

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.domain.enums.ProfileState
import com.sevenstars.domain.model.profile.info.Colors
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileColorBinding
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.main.MainActivity
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.generate.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileColorFragment(
    private val color: Colors? = null
): BaseFragment<FragmentProfileColorBinding>(R.layout.fragment_profile_color) {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val viewModel: ProfileColorViewModel by viewModels()
    private lateinit var colorAdapter: ProfileColorRvAdapter

    private fun isProfileActivity() = requireActivity().localClassName == "view.profile.ProfileActivity"

    override fun initView() {
        colorAdapter = ProfileColorRvAdapter().apply {
            this.setItemClickListener(object : ProfileColorRvAdapter.OnItemClickListener{
                override fun onClick(isFull: Boolean) {
                    if(isFull && isProfileActivity()) {
                        profileViewModel.selectedProfileData.color = colorAdapter.checked
                        viewModel.saveData(colorAdapter.checked!!.id)
                    } else {
                        setNextBtn()
                    }
                }
            })
        }

        if(isProfileActivity()) setupProfileActivity()
        else setupMainActivity()
    }

    private fun setupProfileActivity() {
        (requireActivity() as ProfileActivity).apply {
            setToolbarVisibility(true)
            setStep(11)
        }
        if(profileViewModel.selectedProfileData.color != null) colorAdapter.checkedItem(profileViewModel.selectedProfileData.color!!)
        setColorRv()
    }

    private fun setupMainActivity() {
        (requireActivity() as MainActivity).apply {
            setBottomNaviVisibility(false)
            setToolbarVisibility(true)
        }
        profileViewModel.fetchDefaultData(ProfileState.COMPLETE)
        binding.flSaveBtn.visibility = View.VISIBLE
        colorAdapter.checkedItem(color!!)
    }

    private fun setColorRv(){
        binding.rvColor.apply {
            adapter = colorAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
            addItemDecoration(SpaceColorItemDecoration(requireContext()))
        }

        colorAdapter.setData(profileViewModel.profileDefaultData.colors)
    }

    private fun setNextBtn(){
        binding.btnNext.apply {
            if(colorAdapter.checked != null){
                backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.primary_primary)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.surface))
            } else {
                backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.btn_disabled)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.on_surface_variant))
            }
        }
    }

    override fun initListener() {
        super.initListener()

        binding.btnNext.setOnClickListener {
            if(colorAdapter.checked != null){
                viewModel.saveData(colorAdapter.checked!!.id)
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
                        (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileFragment(), "color")
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
                        setColorRv()
                    }
                }
            }
        }
    }
}