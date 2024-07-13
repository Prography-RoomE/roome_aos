package com.sevenstars.roome.view.profile.hint

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.domain.model.profile.info.HintUsagePreferences
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileHintBinding
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.VerticalSpaceItemDecoration
import com.sevenstars.roome.view.profile.device.ProfileDeviceFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileHintFragment: BaseFragment<FragmentProfileHintBinding>(R.layout.fragment_profile_hint) {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val viewModel: ProfileHintViewModel by viewModels()
    private lateinit var hintAdapter: ProfileHintRvAdapter

    override fun initView() {
        (requireActivity() as ProfileActivity).setStep(7)

        hintAdapter = ProfileHintRvAdapter().apply {
            this.setItemClickListener(object : ProfileHintRvAdapter.OnItemClickListener{
                override fun onClick(isChecked: Boolean, data: HintUsagePreferences) {
                    if(isChecked) viewModel.saveData(data.id)
                }
            })
        }

        binding.rvHint.apply {
            adapter = hintAdapter
            layoutManager = GridLayoutManager(requireContext(), 1)
            addItemDecoration(VerticalSpaceItemDecoration(requireContext(), 12, false))
        }

        hintAdapter.setData(profileViewModel.profileDefaultData.hintUsagePreferences)
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
                    (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileDeviceFragment(), null)
                    viewModel.setLoadingState()
                }
            }
        }
    }
}