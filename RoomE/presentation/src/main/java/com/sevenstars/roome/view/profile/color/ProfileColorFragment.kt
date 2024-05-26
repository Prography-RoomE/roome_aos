package com.sevenstars.roome.view.profile.color

import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.domain.model.profile.info.Colors
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.base.RoomeApplication
import com.sevenstars.roome.databinding.FragmentProfileColorBinding
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.generate.ProfileFragment
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.dislike.ProfileDislikeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileColorFragment: BaseFragment<FragmentProfileColorBinding>(R.layout.fragment_profile_color) {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val viewModel: ProfileColorViewModel by viewModels()
    private lateinit var colorAdapter: ProfileColorRvAdapter

    override fun initView() {
        (requireActivity() as ProfileActivity).setStep(11)

        colorAdapter = ProfileColorRvAdapter().apply {
            this.setItemClickListener(object : ProfileColorRvAdapter.OnItemClickListener{
                override fun onClick(isChecked: Boolean, data: Colors) {
                    if(isChecked) {
                        profileViewModel.selectedProfileData.color = data
                        viewModel.saveData(data.id)
                    }
                }
            })
        }

        binding.rvColor.apply {
            adapter = colorAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
            addItemDecoration(SpaceColorItemDecoration(requireContext()))
        }

        colorAdapter.setData(profileViewModel.profileDefaultData.colors)
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
                    (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileFragment())
                    viewModel.setLoadingState()
                }
            }
        }
    }
}