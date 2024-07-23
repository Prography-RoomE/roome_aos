package com.sevenstars.roome.view.main.setting

import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.sevenstars.data.service.auth.KakaoAuthService
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.domain.enums.Provider
import com.sevenstars.roome.BuildConfig
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.base.RoomeApplication.Companion.app
import com.sevenstars.roome.custom.CustomDialog
import com.sevenstars.roome.databinding.FragmentMainSettingBinding
import com.sevenstars.roome.utils.AnalyticsHelper
import com.sevenstars.roome.utils.Constants.PRIVACY_POLICY
import com.sevenstars.roome.utils.Constants.TERMS_OF_SERVICE
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.utils.UpdateCheckManager
import com.sevenstars.roome.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainSettingFragment : BaseFragment<FragmentMainSettingBinding>(R.layout.fragment_main_setting) {

    private val viewModel: MainSettingViewModel by viewModels()
    private lateinit var updateCheckManager: UpdateCheckManager

    @Inject
    lateinit var kakaoAuthService: KakaoAuthService

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    override fun initView() {
        AnalyticsHelper.logScreenView("setting")
        updateCheckManager = UpdateCheckManager(requireContext())
        (requireActivity() as MainActivity).setBottomNaviVisibility(true)
        setupAppVersion()
        checkForUpdates()
    }

    private fun setupAppVersion() {
        binding.tvCurrentVersion.text = "앱 버전 ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
    }

    private fun checkForUpdates() {
        updateCheckManager.checkForUpdate {
            LoggerUtils.info("Check AvailableVersion: $it")
            if (it) {
                binding.ibUpdate.apply {
                    visibility = View.VISIBLE
                    setOnClickListener { updateCheckManager.promptUpdate() }
                }
                binding.tvUpdate.apply {
                    text = "업데이트 하기"
                    setOnClickListener { updateCheckManager.promptUpdate() }
                }
            } else {
                binding.ibUpdate.apply {
                    visibility = View.GONE
                    setOnClickListener {  }
                }
                binding.tvUpdate.apply {
                    text = "최신 버전"
                    setOnClickListener {  }
                }
            }
        }
    }

    override fun initListener() {
        super.initListener()

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.apply {
            tvTos.setOnClickListener { moveWebView("서비스 이용약관", TERMS_OF_SERVICE) }
            ibTos.setOnClickListener { moveWebView("서비스 이용약관", TERMS_OF_SERVICE) }

            tvPrivacy.setOnClickListener { moveWebView("개인정보처리방침", PRIVACY_POLICY) }
            ibPrivacy.setOnClickListener { moveWebView("개인정보처리방침", PRIVACY_POLICY) }

            tvLogout.setOnClickListener { signOut() }
            ibLogout.setOnClickListener { signOut() }

            tvUnlink.setOnClickListener { unlink() }
            ibUnlink.setOnClickListener { unlink() }
        }
    }

    private fun moveWebView(title: String, url: String) {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_main, WebViewFragment(title, url, false))
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun signOut() {
        CustomDialog.getInstance(CustomDialog.DialogType.SIGN_OUT, null).apply {
            setButtonClickListener(object : CustomDialog.OnButtonClickListener {
                override fun onButton1Clicked() { }
                override fun onButton2Clicked() {
                    CoroutineScope(Dispatchers.IO).launch {
                        val provider = app.userPreferences.getLoginProvider().getOrNull().orEmpty()
                        launch(Dispatchers.Main) {
                            if (provider == Provider.KAKAO.provider) signOutKakao() else signOutGoogle()
                        }
                    }
                }
            })
        }.show(requireActivity().supportFragmentManager, "SignOut")
    }

    private fun unlink() {
        AnalyticsHelper.logButtonClick("exit")

        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_main, UnlinkReasonFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun signOutKakao() {
        kakaoAuthService.signOut { error ->
            if (error != null) {
                showToast("카카오 로그아웃 실패: ${error.message}")
            } else {
                LoggerUtils.info("카카오 로그아웃 성공")
                viewModel.signOut()
            }
        }
    }

    private fun signOutGoogle() {
        googleSignInClient.signOut()
            .addOnCompleteListener(requireActivity()) {
                LoggerUtils.info("구글 로그아웃 성공")
                viewModel.signOut()
            }
            .addOnFailureListener {
                showToast("구글 로그아웃 실패: ${it.message}")
            }
    }

    override fun observer() {
        super.observer()

        viewModel.signOutState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    showNoConnectionDialog(R.id.fl_main, isReplace = false)
                    showToast("로그아웃 실패: ${it.message}")
                }
                is UiState.Loading -> { }
                is UiState.Success -> {
                    (requireActivity() as MainActivity).removeData()
                }
            }
        }
    }
}
