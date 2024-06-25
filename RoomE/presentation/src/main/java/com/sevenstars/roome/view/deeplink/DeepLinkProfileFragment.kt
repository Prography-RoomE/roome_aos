package com.sevenstars.roome.view.deeplink

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import com.sevenstars.domain.model.profile.info.Colors
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.base.RoomeApplication.Companion.userName
import com.sevenstars.roome.databinding.FragmentDeelinkProfileBinding
import com.sevenstars.roome.databinding.FragmentMainProfileBinding
import com.sevenstars.roome.databinding.ItemMainProfileChipBinding
import com.sevenstars.roome.exetnsion.setColorBackground
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.main.MainActivity
import com.sevenstars.roome.view.main.profile.MainProfileViewModel
import com.sevenstars.roome.view.main.profile.ProfileCardFragment
import com.sevenstars.roome.view.signIn.SignInActivity
import com.sevenstars.roome.view.splash.StartActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeepLinkProfileFragment(private val nickname: String) : BaseFragment<FragmentDeelinkProfileBinding>(R.layout.fragment_deelink_profile) {
    private val viewModel: DeepLinkProfileViewModel by viewModels()

    override fun initView() {
        viewModel.fetchData(nickname)
        binding.btnMove.text = if(nickname == (userName ?: "")) "내 프로필로 이동" else "나만의 프로필 만들기"
    }

    override fun initListener() {
        super.initListener()
        binding.apply {
            btnProfileCard.setOnClickListener {
                val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fl_main, ProfileCardFragment())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }

            btnMove.setOnClickListener {
                if(nickname == userName){
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.putExtra("type", "profile")
                    startActivity(intent)
                } else {
                    val intent = Intent(requireActivity(), SignInActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        }
    }

    override fun observer() {
        super.observer()
        with(viewModel) {
            uiState.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is UiState.Failure -> {}
                    is UiState.Loading -> {}
                    is UiState.Success -> {
                        binding.tvNick.text = nickname
                        updateChip(binding.chipProfileCount, "방탈출 횟수", state.data.count)
                        updateChip(binding.chipProfileGenres, "선호 장르", state.data.preferredGenres.map { it.text!! })
                        updateChip(binding.chipProfileMBTI, "MBTI", state.data.mbti)
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
