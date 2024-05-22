package com.sevenstars.roome.view.profile.device

import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileHintBinding
import com.sevenstars.roome.databinding.FragmentProfileHorrorBinding
import com.sevenstars.roome.databinding.FragmentProfileImportantFactorBinding
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.ProfileWelcomeFragment
import com.sevenstars.roome.view.profile.VerticalSpaceItemDecoration

class ProfileDeviceFragment: BaseFragment<FragmentProfileHintBinding>(R.layout.fragment_profile_hint) {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private lateinit var deviceAdapter: ProfileDeviceRvAdapter

    override fun initView() {
        (requireActivity() as ProfileActivity).setStep(8)

        deviceAdapter = ProfileDeviceRvAdapter().apply {
            this.setItemClickListener(object : ProfileDeviceRvAdapter.OnItemClickListener{
                override fun onClick(isChecked: Boolean) {
                    if(isChecked) (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileWelcomeFragment())
                }
            })
        }

        binding.rvHint.apply {
            adapter = deviceAdapter
            layoutManager = GridLayoutManager(requireContext(), 1)
        }

        deviceAdapter.setData(profileViewModel.profileData.deviceLockPreferences)
    }

    override fun initListener() {
        super.initListener()

    }
}