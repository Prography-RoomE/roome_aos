package com.sevenstars.roome.view.profile

import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.domain.enums.ProfileState
import com.sevenstars.domain.model.profile.SelectedProfileData
import com.sevenstars.domain.model.profile.info.Genres
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.base.RoomeApplication
import com.sevenstars.roome.databinding.FragmentWelcomeBinding
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.CustomDialog
import com.sevenstars.roome.view.profile.activity.ProfileActivityFragment
import com.sevenstars.roome.view.profile.color.ProfileColorFragment
import com.sevenstars.roome.view.profile.count.ProfileCountFragment
import com.sevenstars.roome.view.profile.device.ProfileDeviceFragment
import com.sevenstars.roome.view.profile.dislike.ProfileDislikeFragment
import com.sevenstars.roome.view.profile.genres.ProfileGenresFragment
import com.sevenstars.roome.view.profile.horror.ProfileHorrorFragment
import com.sevenstars.roome.view.profile.important.ProfileImportantFragment
import com.sevenstars.roome.view.profile.mbti.ProfileMbtiFragment
import com.sevenstars.roome.view.profile.strength.ProfileStrengthFragment

class ProfileWelcomeFragment(private var step: Int): BaseFragment<FragmentWelcomeBinding>(R.layout.fragment_welcome) {
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun initView() {
        (requireActivity() as ProfileActivity).setToolbarVisibility(false)
        binding.tvTitle.text = getString(R.string.profile_welcome_title, "Roome")

        profileViewModel.updateProfileData()
    }

    override fun observer() {
        super.observer()

        profileViewModel.updateProfileDataState.observe(viewLifecycleOwner){
            when(it){
                is UiState.Failure -> {
                    LoggerUtils.error("프로필 데이터 조회 실패\n${it.message}")
                    Toast.makeText(RoomeApplication.app, "프로필 데이터 조회 실패\n${it.message}", Toast.LENGTH_SHORT).show()
                }
                is UiState.Loading -> {}
                is UiState.Success -> {
                    step = it.data.step
                }
            }
        }
    }

    override fun initListener() {
        super.initListener()

        binding.btnCreate.setOnClickListener {
            if(step == ProfileState.ROOM_COUNT.step) navigateToStep(1)
            else {
                CustomDialog.getInstance(CustomDialog.DialogType.PROFILE_CONTINUE, null).apply {
                    setButtonClickListener(object: CustomDialog.OnButtonClickListener{
                        override fun onButton1Clicked() { // 새로 하기
                            navigateToStep(1)
                        }

                        override fun onButton2Clicked() { // 이어서 하기
                            navigateToStep(step)
                        }
                    })
                }.show(requireActivity().supportFragmentManager, "")
            }
        }
    }

    private fun navigateToStep(targetStep: Int) {
        moveStep(1, targetStep)
    }

    private fun moveStep(currentStep: Int, targetStep: Int) {
        if (currentStep > targetStep) return

        LoggerUtils.debug("Profile STEP: $currentStep")

        val destinationFragment = when (currentStep) {
            1 -> ProfileCountFragment()
            2 -> ProfileGenresFragment()
            3 -> ProfileMbtiFragment()
            4 -> ProfileStrengthFragment()
            5 -> ProfileImportantFragment()
            6 -> ProfileHorrorFragment()
            7 -> ProfileDeviceFragment()
            8 -> ProfileActivityFragment()
            9 -> ProfileDislikeFragment()
            10 -> ProfileColorFragment()
            else -> null
        }

        destinationFragment?.let {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fl_profile, it)
                addToBackStack(null)
                commit()
            }
            if (currentStep < targetStep) {
                requireActivity().supportFragmentManager.executePendingTransactions()
                moveStep(currentStep + 1, targetStep)
            }
        }
    }
}