package com.sevenstars.roome.view.main.profile

import android.view.View
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.sevenstars.domain.model.profile.info.Colors
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentMainProfileBinding
import com.sevenstars.roome.databinding.ItemMainProfileChipBinding
import com.sevenstars.roome.exetnsion.setColorBackground
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.main.MainActivity
import com.sevenstars.roome.view.main.profile.edit.UserProfileEditFragment
import com.sevenstars.roome.view.profile.count.ProfileCountFragment
import com.sevenstars.roome.view.profile.genres.ProfileGenresFragment
import com.sevenstars.roome.view.profile.hint.ProfileHintFragment
import com.sevenstars.roome.view.profile.horror.ProfileHorrorFragment
import com.sevenstars.roome.view.profile.important.ProfileImportantFragment
import com.sevenstars.roome.view.profile.mbti.ProfileMbtiFragment
import com.sevenstars.roome.view.profile.strength.ProfileStrengthFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainProfileFragment : BaseFragment<FragmentMainProfileBinding>(R.layout.fragment_main_profile) {
    private val viewModel: MainProfileViewModel by viewModels()

    override fun initView() {
        (requireActivity() as MainActivity).setBottomNaviVisibility(true)
        (requireActivity() as MainActivity).setToolbarVisibility(false)
        viewModel.fetchData()
        viewModel.fetchUserInfo()
    }

    override fun initListener() {
        super.initListener()
        binding.apply {
            btnShareKakao.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .add(R.id.fl_main, SquareProfileCardGenerate(viewModel.nickname))
                    .commit()
            }

            btnProfileCard.setOnClickListener {
                (requireActivity() as MainActivity).replaceFragment(ProfileCardFragment(viewModel.nickname), true)
            }

            ibEdit.setOnClickListener {
                (requireActivity() as MainActivity).replaceFragment(UserProfileEditFragment(viewModel.nickname, viewModel.imageUrl), true)
            }

            chipProfileCount.btnChip.setOnClickListener{ (requireActivity() as MainActivity).replaceFragment(ProfileCountFragment(viewModel.savedProfileData.count), true)}
            chipProfileGenres.btnChip.setOnClickListener{ (requireActivity() as MainActivity).replaceFragment(ProfileGenresFragment(viewModel.savedProfileData.preferredGenres), true) }
            chipProfileMBTI.btnChip.setOnClickListener{ (requireActivity() as MainActivity).replaceFragment(ProfileMbtiFragment(viewModel.savedProfileData.mbti), true) }
            chipProfileStrength.btnChip.setOnClickListener{ (requireActivity() as MainActivity).replaceFragment(ProfileStrengthFragment(viewModel.savedProfileData.userStrengths), true) }
            chipProfileImportantFactor.btnChip.setOnClickListener{ (requireActivity() as MainActivity).replaceFragment(ProfileImportantFragment(viewModel.savedProfileData.themeImportantFactors), true) }
            chipProfileHorror.btnChip.setOnClickListener{ (requireActivity() as MainActivity).replaceFragment(ProfileHorrorFragment(viewModel.savedProfileData.horrorThemePosition), true) }
            chipProfileHint.btnChip.setOnClickListener{ (requireActivity() as MainActivity).replaceFragment(ProfileHintFragment(viewModel.savedProfileData.hintUsagePreference), true) }
            chipProfileDevice.btnChip.setOnClickListener{}
            chipProfileActivity.btnChip.setOnClickListener{}
            chipProfileDislikeFactor.btnChip.setOnClickListener{}
            chipProfileColor.btnChip.setOnClickListener{}
        }
    }

    override fun observer() {
        super.observer()
        with(viewModel) {
            userState.observe(viewLifecycleOwner){
                when(it){
                    is UiState.Failure -> {
                        if(it.code == 0) showNoConnectionDialog(R.id.fl_main, this@MainProfileFragment, isReplace = false)
                        showToast("내 정보 조회 실패")
                    }
                    is UiState.Loading -> {}
                    is UiState.Success -> {
                        binding.tvNick.text = it.data.nickname
                        if(it.data.imageUrl.isNotEmpty()){
                            Glide.with(requireContext())
                                .load(it.data.imageUrl)
                                .optionalCircleCrop()
                                .into(binding.ivUserProfile)
                        }
                    }
                }
            }

            uiState.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is UiState.Failure -> {
                        if(state.code == 0) showNoConnectionDialog(R.id.fl_main, this@MainProfileFragment, isReplace = false)
                    }
                    is UiState.Loading -> {}
                    is UiState.Success -> {
                        updateChip(binding.chipProfileCount, "방탈출 횟수", state.data.count)
                        updateChip(binding.chipProfileGenres, "선호 장르", state.data.preferredGenres.map { it.text!! })
                        updateChip(binding.chipProfileMBTI, "MBTI", if(state.data.mbti == "NONE") "-" else state.data.mbti)
                        updateChip(binding.chipProfileStrength, "강점", state.data.userStrengths.map { it.text!! })
                        updateChip(binding.chipProfileImportantFactor, "테마 중요 요소", state.data.themeImportantFactors.map { it.text!! })
                        updateChip(binding.chipProfileHorror, "공포테마 포지션", listOf(state.data.horrorThemePosition!!.text!!))
                        updateChip(binding.chipProfileHint, "힌트 선호도", listOf(state.data.hintUsagePreference!!.text!!))
                        updateChip(binding.chipProfileDevice, "장치/자물쇠 선호도", listOf(state.data.deviceLockPreference!!.text!!))
                        updateChip(binding.chipProfileActivity, "활동성 선호도", listOf(state.data.activity!!.text!!))
                        updateChip(binding.chipProfileDislikeFactor, "싫어하는 요소", state.data.themeDislikedFactors.map { it.text!! })
                        updateColorChip(state.data.color!!)
                    }
                }
            }
        }
    }

    private fun updateColorChip(color: Colors){
        binding.chipProfileColor.apply {
            groupContextType3.visibility = View.VISIBLE
            tvChipTitle.text = "프로필 색상"
            setColorBackground(
                view = ivProfileColor,
                mode = color.mode,
                shape = color.shape,
                orientation = color.direction,
                startColor = color.startColor,
                endColor = color.endColor,
                isRoundCorner = true,
                radius = 360f,
                hasStroke = true
            )
        }
    }

    private fun updateChip(chip: ItemMainProfileChipBinding, title: String, text: String) {
        chip.apply {
            groupContextType1.visibility = View.VISIBLE
            tvChipTitle.text = title
            tvChipContext.text = text
        }
    }

    private fun updateChip(chip: ItemMainProfileChipBinding, title: String, texts: List<String>) {
        chip.apply {
            groupContextType2.visibility = View.VISIBLE
            tvChipTitle.text = title
            tvChipContext1.text = texts[0]
            if (texts.size > 1) tvChipContext2.text = texts[1]
        }
    }
}
