package com.sevenstars.roome.view.main.profile

import android.view.View
import androidx.fragment.app.viewModels
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.base.RoomeApplication.Companion.userName
import com.sevenstars.roome.databinding.FragmentMainProfileBinding
import com.sevenstars.roome.exetnsion.setColorBackground
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.main.MainActivity
import com.sevenstars.roome.view.profile.generate.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainProfileFragment: BaseFragment<FragmentMainProfileBinding>(R.layout.fragment_main_profile) {
    private val viewModel: MainProfileViewModel by viewModels()

    override fun initView() {
        (requireActivity() as MainActivity).setBottomNaviVisibility(true)
        binding.tvNick.text = userName
        viewModel.fetchData()
    }

    override fun initListener() {
        super.initListener()

        binding.btnShareKakao.setOnClickListener{

        }

        binding.btnProfileCard.setOnClickListener {
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fc_main, ProfileCardFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    override fun observer() {
        super.observer()
        with(viewModel){
            uiState.observe(viewLifecycleOwner){
                when(it){
                    is UiState.Failure -> {}
                    is UiState.Loading -> {}
                    is UiState.Success -> {}
                }
            }
        }

        viewModel.count.observe(viewLifecycleOwner){
            binding.chipProfileCount.apply {
                groupContextType1.visibility = View.VISIBLE
                tvChipTitle.text = "방탈출 횟수"
                tvChipContext.text = it
            }
        }

        viewModel.preferredGenres.observe(viewLifecycleOwner){
            binding.chipProfileGenres.apply {
                groupContextType2.visibility = View.VISIBLE
                tvChipTitle.text = "선호 장르"
                tvChipContext1.text = it[0].title
                if(it.size == 2) tvChipContext2.text = it[1].title
            }
        }

        viewModel.mbti.observe(viewLifecycleOwner){
            binding.chipProfileMBTI.apply {
                groupContextType1.visibility = View.VISIBLE
                tvChipTitle.text = "MBTI"
                tvChipContext.text = it
            }
        }

        viewModel.userStrengths.observe(viewLifecycleOwner){
            binding.chipProfileStrength.apply {
                groupContextType2.visibility = View.VISIBLE
                tvChipTitle.text = "강점"
                tvChipContext1.text = it[0].title
                if(it.size == 2) tvChipContext2.text = it[1].title
            }
        }

        viewModel.themeImportantFactors.observe(viewLifecycleOwner){
            binding.chipProfileImportantFactor.apply {
                groupContextType2.visibility = View.VISIBLE
                tvChipTitle.text = "테마 중요 요소"
                tvChipContext1.text = it[0].title
                if(it.size == 2) tvChipContext2.text = it[1].title
            }
        }

        viewModel.horrorThemePosition.observe(viewLifecycleOwner){
            binding.chipProfileHorror.apply {
                groupContextType2.visibility = View.VISIBLE
                tvChipTitle.text = "공포테마 포지션"
                tvChipContext1.text = it.title
            }
        }

        viewModel.hintUsagePreference.observe(viewLifecycleOwner){
            binding.chipProfileHint.apply {
                groupContextType2.visibility = View.VISIBLE
                tvChipTitle.text = "힌트 선호도"
                tvChipContext1.text = it.title
            }
        }

        viewModel.deviceLockPreference.observe(viewLifecycleOwner){
            binding.chipProfileDevice.apply {
                groupContextType2.visibility = View.VISIBLE
                tvChipTitle.text = "장치/자물쇠 선호도"
                tvChipContext1.text = it.title
            }
        }

        viewModel.activity.observe(viewLifecycleOwner){
            binding.chipProfileActivity.apply {
                groupContextType2.visibility = View.VISIBLE
                tvChipTitle.text = "활동성 선호도"
                tvChipContext1.text = it.title
            }
        }

        viewModel.themeDislikedFactors.observe(viewLifecycleOwner){
            binding.chipProfileDislikeFactor.apply {
                groupContextType2.visibility = View.VISIBLE
                tvChipTitle.text = "싫어하는 요소"
                tvChipContext1.text = it[0].title
                if(it.size == 2) tvChipContext2.text = it[1].title
            }
        }

        viewModel.color.observe(viewLifecycleOwner){
            binding.chipProfileColor.apply {
                groupContextType3.visibility = View.VISIBLE
                tvChipTitle.text = "프로필 색상"
                setColorBackground(
                    view = ivProfileColor,
                    mode = it.mode,
                    shape = it.shape,
                    orientation = it.direction,
                    startColor = it.startColor,
                    endColor = it.endColor,
                    isRoundCorner = true,
                    radius = 360f,
                    hasStroke = true
                )
            }
        }
    }
}