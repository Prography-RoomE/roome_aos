package com.sevenstars.roome.view.profile.device

import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.domain.model.profile.info.DeviceLockPreferences
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.base.RoomeApplication
import com.sevenstars.roome.databinding.FragmentProfileDeviceBinding
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.activity.ProfileActivityFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileDeviceFragment: BaseFragment<FragmentProfileDeviceBinding>(R.layout.fragment_profile_device) {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val viewModel: ProfileDeviceViewModel by viewModels()
    private lateinit var deviceAdapter: ProfileDeviceRvAdapter

    override fun initView() {
        (requireActivity() as ProfileActivity).setStep(8)

        deviceAdapter = ProfileDeviceRvAdapter().apply {
            this.setItemClickListener(object : ProfileDeviceRvAdapter.OnItemClickListener{
                override fun onClick(isChecked: Boolean, data: DeviceLockPreferences) {
                    if(isChecked) viewModel.saveData(data.id)
                }
            })
        }

        binding.rvDevice.apply {
            adapter = deviceAdapter
            layoutManager = GridLayoutManager(requireContext(), 1)
        }

        deviceAdapter.setData(profileViewModel.profileDefaultData.deviceLockPreferences)
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
                    (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileActivityFragment())
                    viewModel.setLoadingState()
                }
            }
        }
    }
}