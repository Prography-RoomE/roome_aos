package com.sevenstars.roome.view.profile.dislike

import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.base.RoomeApplication
import com.sevenstars.roome.databinding.FragmentProfileDislikeBinding
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.SpaceItemDecoration
import com.sevenstars.roome.view.profile.activity.ProfileActivityFragment
import com.sevenstars.roome.view.profile.color.ProfileColorFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileDislikeFragment: BaseFragment<FragmentProfileDislikeBinding>(R.layout.fragment_profile_dislike) {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val viewModel: ProfileDislikeViewModel by viewModels()
    private lateinit var dislikeAdapter: ProfileDislikeRvAdapter

    override fun initView() {
        (requireActivity() as ProfileActivity).setStep(10)

        dislikeAdapter = ProfileDislikeRvAdapter().apply {
            this.setItemClickListener(object : ProfileDislikeRvAdapter.OnItemClickListener{
                override fun onClick(isFull: Boolean) {
                    if(isFull) showToast("최대 2개까지 선택할 수 있어요.")
                    setNextBtn()
                }
            })

            checked.addAll(profileViewModel.selectedProfileData.dislike)
        }

        setNextBtn()

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
                    profileViewModel.selectedProfileData.dislike = dislikeAdapter.checked
                    (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileColorFragment())
                    viewModel.setLoadingState()
                }
            }
        }
    }

    override fun initListener() {
        super.initListener()

        binding.btnNext.setOnClickListener {
            LoggerUtils.info(dislikeAdapter.checked.joinToString(", "))
            if(binding.btnNext.currentTextColor == ContextCompat.getColor(requireContext(), R.color.surface)){
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