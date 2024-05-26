package com.sevenstars.roome.view.profile.count

import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.base.RoomeApplication
import com.sevenstars.roome.databinding.FragmentProfileCountBinding
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.genres.ProfileGenresFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileCountFragment: BaseFragment<FragmentProfileCountBinding>(R.layout.fragment_profile_count) {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val viewModel: ProfileCountViewModel by viewModels()

    override fun initView() {
        (requireActivity() as ProfileActivity).apply {
            setToolbarVisibility(true)
            setStep(1)
        }
        binding.etCount.setText(profileViewModel.selectedProfileData.count.toString())
    }

    override fun initListener() {
        super.initListener()

        binding.btnNext.setOnClickListener {
            viewModel.saveData(binding.etCount.text.toString().toInt(), false)
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
                    profileViewModel.selectedProfileData.count = binding.etCount.text.toString().toInt()
                    (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileGenresFragment())
                    viewModel.setLoadingState()
                }
            }
        }
    }
}