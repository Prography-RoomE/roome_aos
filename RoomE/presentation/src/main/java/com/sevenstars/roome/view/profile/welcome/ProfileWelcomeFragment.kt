package com.sevenstars.roome.view.profile.welcome

import android.content.Context
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.domain.enums.ProfileState
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.base.RoomeApplication
import com.sevenstars.roome.base.RoomeApplication.Companion.userName
import com.sevenstars.roome.databinding.FragmentWelcomeBinding
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.CustomDialog
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.activity.ProfileActivityFragment
import com.sevenstars.roome.view.profile.color.ProfileColorFragment
import com.sevenstars.roome.view.profile.count.ProfileCountFragment
import com.sevenstars.roome.view.profile.device.ProfileDeviceFragment
import com.sevenstars.roome.view.profile.dislike.ProfileDislikeFragment
import com.sevenstars.roome.view.profile.genres.ProfileGenresFragment
import com.sevenstars.roome.view.profile.hint.ProfileHintFragment
import com.sevenstars.roome.view.profile.horror.ProfileHorrorFragment
import com.sevenstars.roome.view.profile.important.ProfileImportantFragment
import com.sevenstars.roome.view.profile.mbti.ProfileMbtiFragment
import com.sevenstars.roome.view.profile.strength.ProfileStrengthFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileWelcomeFragment(private var step: Int): BaseFragment<FragmentWelcomeBinding>(R.layout.fragment_welcome) {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val viewModel: ProfileWelcomeViewModel by viewModels()
    private lateinit var callback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { /* Back Pressed 방지용 */}
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    override fun initView() {
        (requireActivity() as ProfileActivity).setToolbarVisibility(false)
        binding.tvTitle.text = getString(R.string.profile_welcome_title, userName)
        profileViewModel.updateProfileData()
    }

    override fun observer() {
        super.observer()

        profileViewModel.updateProfileDataState.observe(viewLifecycleOwner){
            when(it){
                is UiState.Failure -> {
                    LoggerUtils.error("프로필 데이터 조회 실패\n${it.message}")
                    showToast("프로필 데이터 조회 실패\n${it.message}")
                }
                is UiState.Loading -> {}
                is UiState.Success -> {
                    step = it.data.step
                }
            }
        }

        viewModel.uiState.observe(viewLifecycleOwner){
            when(it){
                is UiState.Failure -> {
                    LoggerUtils.error("새로하기 실패\n${it.message}")
                    showToast("새로하기 실패\n${it.message}")
                }
                is UiState.Loading -> {}
                is UiState.Success -> {
                    profileViewModel.resetSelectedProfileData()
                    profileViewModel.updateProfileData()
                    viewModel.setLoadingState()
                    navigateToStep(1)
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
                            viewModel.deleteProfileData()
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
            7 -> ProfileHintFragment()
            8 -> ProfileDeviceFragment()
            9 -> ProfileActivityFragment()
            10 -> ProfileDislikeFragment()
            11 -> ProfileColorFragment()
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