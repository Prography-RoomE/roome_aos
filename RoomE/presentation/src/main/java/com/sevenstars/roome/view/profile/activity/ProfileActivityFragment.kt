package com.sevenstars.roome.view.profile.activity

import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.domain.model.profile.info.Activities
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.base.RoomeApplication
import com.sevenstars.roome.databinding.FragmentProfileActivitiyBinding
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.VerticalSpaceItemDecoration
import com.sevenstars.roome.view.profile.dislike.ProfileDislikeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivityFragment: BaseFragment<FragmentProfileActivitiyBinding>(R.layout.fragment_profile_activitiy) {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val viewModel: ProfileActivityViewModel by viewModels()
    private lateinit var activityAdapter: ProfileActivityRvAdapter

    override fun initView() {
        (requireActivity() as ProfileActivity).setStep(9)

        activityAdapter = ProfileActivityRvAdapter().apply {
            this.setItemClickListener(object : ProfileActivityRvAdapter.OnItemClickListener{
                override fun onClick(isChecked: Boolean, data: Activities) {
                    if(isChecked) viewModel.saveData(data.id)
                }
            })
        }

        binding.rvActivity.apply {
            adapter = activityAdapter
            layoutManager = GridLayoutManager(requireContext(), 1)
            addItemDecoration(VerticalSpaceItemDecoration(requireContext(), 12))
        }

        activityAdapter.setData(profileViewModel.profileDefaultData.activities)
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
                    (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileDislikeFragment())
                    viewModel.setLoadingState()
                }
            }
        }
    }
}