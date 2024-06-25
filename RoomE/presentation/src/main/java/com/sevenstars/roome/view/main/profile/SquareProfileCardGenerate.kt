package com.sevenstars.roome.view.main.profile

import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.domain.model.profile.SavedProfileData
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.base.RoomeApplication.Companion.userName
import com.sevenstars.roome.databinding.GenerateProfileCardBinding
import com.sevenstars.roome.exetnsion.setColorBackground
import com.sevenstars.roome.utils.ImageUtils.captureViewToCache
import com.sevenstars.roome.utils.KakaoShareManager
import com.sevenstars.roome.utils.UiState
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class SquareProfileCardGenerate : BaseFragment<GenerateProfileCardBinding>(R.layout.generate_profile_card) {
    private val viewModel: ProfileCardViewModel by viewModels()
    private var squareProfileFile: File? = null

    override fun initView() {
        viewModel.fetchSaveData()
        binding.root.visibility = View.INVISIBLE
    }

    override fun observer() {
        super.observer()
        viewModel.uiState.observe(this) { handleUiState(it) }
    }

    private fun handleUiState(uiState: UiState<SavedProfileData>) {
        when (uiState) {
            is UiState.Failure -> handleFailure(uiState.message)
            is UiState.Loading -> { /* Loading */}
            is UiState.Success -> handleSuccess(uiState.data)
        }
    }

    private fun handleFailure(message: String) {
        LoggerUtils.error("Profile data fetch failed\n$message")
        showToast("An error occurred while creating the profile.")
    }

    private fun handleSuccess(data: SavedProfileData) {
        LoggerUtils.debug("Profile data fetch successful")
        setSquareProfile(data)
    }

    private fun setSquareProfile(data: SavedProfileData) {
        with(binding.icProfileSquare) {
            bindProfileData(data)
            setProfileBackground(data, llProfile)
        }

        binding.icProfileSquare.llProfile.post {
            createProfileFileIfNeeded()
        }
    }

    private fun createProfileFileIfNeeded() {
        if (squareProfileFile == null) {
            squareProfileFile = captureViewToCache(requireContext(), "${userName}'s Shared Profile", binding.icProfileSquare.root)
                .also {
                    KakaoShareManager(requireContext()).doProfileShare(it!!)
                    requireActivity().supportFragmentManager.beginTransaction().remove(this@SquareProfileCardGenerate).commit()
                }
        }
    }

    private fun bindProfileData(data: SavedProfileData) {
        with(binding.icProfileSquare) {
            name.text = userName
            experience.text = data.count
            mbti.text = if (data.mbti == "NONE") "" else data.mbti
            emptyViewRemove(mbti)

            genre1.text = data.preferredGenres.getOrNull(0)?.title.orEmpty()
            genre2.text = data.preferredGenres.getOrNull(1)?.title.orEmpty()
            emptyViewRemove(genre2)

            strength1.text = data.userStrengths.getOrNull(0)?.title.orEmpty()
            strength2.text = data.userStrengths.getOrNull(1)?.title.orEmpty()
            emptyViewRemove(strength2)

            important1.text = data.themeImportantFactors.getOrNull(0)?.title.orEmpty()
            important2.text = data.themeImportantFactors.getOrNull(1)?.title.orEmpty()
            emptyViewRemove(important2)

            position.text = data.horrorThemePosition?.title.orEmpty()
            if (data.horrorThemePosition?.title == "잘 모르겠어요") position.visibility = View.GONE
            hintUsage.text = data.hintUsagePreference?.title.orEmpty()
            devicePreference.text = data.deviceLockPreference?.title.orEmpty()
            activityPreference.text = data.activity?.title.orEmpty()

            dislike1.text = data.themeDislikedFactors.getOrNull(0)?.title.orEmpty()
            dislike2.text = data.themeDislikedFactors.getOrNull(1)?.title.orEmpty()
            emptyViewRemove(dislike2)
        }
    }

    private fun emptyViewRemove(view: TextView) {
        if (view.text.isEmpty()) view.visibility = View.GONE
    }

    private fun setProfileBackground(data: SavedProfileData, view: View) {
        setColorBackground(
            view,
            mode = data.color?.mode.orEmpty(),
            shape = data.color?.shape.orEmpty(),
            orientation = data.color?.direction.orEmpty(),
            startColor = data.color?.startColor.orEmpty(),
            endColor = data.color?.endColor.orEmpty(),
            isRoundCorner = false
        )
    }
}
