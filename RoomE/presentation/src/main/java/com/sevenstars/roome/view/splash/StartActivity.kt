package com.sevenstars.roome.view.splash

import android.content.Intent
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
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.profile.ProfileActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

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

        viewModel.doValidation()

        observer()
    }

    private fun moveActivity(p: Boolean){
//        val destination = if(p) {
//            if(viewModel.isRegister) { MainActivity::class.java }
//            else { SignUpActivity::class.java }
//        } else {
//            SignInActivity::class.java
//        }
//
//        val intent = Intent(this, destination)
//        startActivity(intent)
//        finish()

        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun observer() {
        viewModel.loginState.observe(this){
            when(it){
                is UiState.Failure -> {
                    LoggerUtils.error("유효성 검사 실패: ${it.message}")
                    moveActivity(false)
                }
                is UiState.Loading -> {}
                is UiState.Success -> {
                    when(runBlocking(Dispatchers.IO) { app.userPreferences.getLoginProvider().getOrNull().orEmpty() }){
                        Provider.KAKAO.provider -> isValidKakaoToken()
                        Provider.GOOGLE.provider -> isValidGoogleToken()
                        else -> moveActivity(false)
                    }
                }
            }
        }
    }

    private fun isValidKakaoToken(){
        if (!AuthApiClient.instance.hasToken()) {
            LoggerUtils.error("카카오 토큰 유효성 검사: 토큰 없음")
            moveActivity(false)
        }

        UserApiClient.instance.accessTokenInfo { _, error ->
            if (error != null) {
                LoggerUtils.error("카카오 토큰 유효성 검사: \n${error.stackTraceToString()}")
                moveActivity(false)
            } else {
                LoggerUtils.debug("카카오 토큰 유효성 검사: 성공")
                moveActivity(true)
            }
        }
    }

    private fun isValidGoogleToken(){
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if(account == null){
            LoggerUtils.error("구글 토큰 유효성 검사: 토큰 없음")
            moveActivity(false)
        }

        if(account != null && !account.isExpired){
            LoggerUtils.debug("구글 토큰 유효성 검사: 성공 <${account.displayName}>")
            moveActivity(true)
        } else {
            LoggerUtils.error("구글 토큰 유효성 검사: 실패")
            moveActivity(false)
        }
    }
}