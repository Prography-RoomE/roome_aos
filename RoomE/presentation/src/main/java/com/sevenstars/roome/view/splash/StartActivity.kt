package com.sevenstars.roome.view.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.user.UserApiClient
import com.sevenstars.data.service.auth.KakaoAuthService
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.domain.enums.Provider
import com.sevenstars.roome.R
import com.sevenstars.roome.base.RoomeApplication.Companion.app
import com.sevenstars.roome.custom.CustomDialog
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.utils.UpdateCheckManager
import com.sevenstars.roome.view.deeplink.DeepLinkActivity
import com.sevenstars.roome.view.main.profile.edit.UserProfileBottomSheetDialog
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.signIn.SignInActivity
import com.sevenstars.roome.view.signup.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.exitProcess

@AndroidEntryPoint
class StartActivity: AppCompatActivity() {
    private val viewModel: StartViewModel by viewModels()

    @Inject
    lateinit var kakaoAuthService: KakaoAuthService

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        observer()

        viewModel.checkForceUpdate()
    }

    private fun observer() {
        viewModel.loginState.observe(this) { state ->
            when (state) {
                is UiState.Failure -> {
                    LoggerUtils.error("유효성 검사 실패: ${state.message}")
                    moveActivity(false)
                }
                is UiState.Loading -> {
                    // Loading 상태 처리
                }
                is UiState.Success -> {
                    handleLoginSuccess()
                }
            }
        }

        viewModel.checkState.observe(this) { state ->
            when (state) {
                is UiState.Failure -> {
                    LoggerUtils.error("강제 업데이트 버전 체크 실패: ${state.message}")

                    // 앱 종료
                    finishAffinity()
                    System.runFinalization()
                    exitProcess(0)
                }
                is UiState.Loading -> {
                    // Loading 상태 처리
                }
                is UiState.Success -> {
                    if(state.data) {
                        CustomDialog.getInstance(CustomDialog.DialogType.FORCE_UPDATE, null).apply {
                            setButtonClickListener(object : CustomDialog.OnButtonClickListener {
                                override fun onButton1Clicked() {
                                    UpdateCheckManager(this@StartActivity).promptUpdate()
                                }
                                override fun onButton2Clicked() {}
                            })
                        }.show(supportFragmentManager, "")
                    }
                    else viewModel.doValidation()
                }
            }
        }
    }

    private fun handleLoginSuccess() {
        GlobalScope.launch(Dispatchers.IO) {
            when (app.userPreferences.getLoginProvider().getOrElse { "" }) {
                Provider.KAKAO.provider -> checkKakaoToken()
                Provider.GOOGLE.provider -> checkGoogleToken()
                else -> moveActivity(false)
            }
        }
    }

    private fun checkKakaoToken() {
        if (!AuthApiClient.instance.hasToken()) {
            LoggerUtils.error("카카오 토큰 유효성 검사: 토큰 없음")
            moveActivity(false)
            return
        }

        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null || tokenInfo == null) {
                LoggerUtils.error("카카오 토큰 유효성 검사 실패: ${error?.stackTraceToString()}")
                moveActivity(false)
            } else {
                LoggerUtils.debug("카카오 토큰 유효성 검사 성공")
                moveActivity(true)
            }
        }
    }

    private fun checkGoogleToken() {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account == null) {
            LoggerUtils.error("구글 토큰 유효성 검사: 토큰 없음")
            moveActivity(false)
            return
        }

        if (account.isExpired) {
            LoggerUtils.error("구글 토큰 유효성 검사 실패")
            moveActivity(false)
        } else {
            LoggerUtils.debug("구글 토큰 유효성 검사 성공 <${account.displayName}>")
            moveActivity(true)
        }
    }

    private fun moveActivity(isValid: Boolean) {
        val destination = if (isValid) {
            if (viewModel.isRegister) ProfileActivity::class.java else SignUpActivity::class.java
        } else {
            SignInActivity::class.java
        }

        val intent = createIntent(destination)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun createIntent(destination: Class<*>) : Intent {
        val intent = Intent(this, destination)

        val action: String? = this.intent.action
        val data: Uri? = this.intent.data

        if (action == Intent.ACTION_VIEW) {
            val type = data?.getQueryParameter("type")

            if (!type.isNullOrEmpty()) {
                intent.setClass(this, DeepLinkActivity::class.java)
                intent.putExtra("type", type)
                intent.putExtra("value", data.getQueryParameter("value"))
            }
        }

        if (intent.hasExtra("isUnlink")) {
            intent.putExtra("isUnlink", true)
        }

        return intent
    }
}
