package com.sevenstars.roome.view.signIn

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.sevenstars.data.service.auth.KakaoAuthService
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.domain.enums.Provider
import com.sevenstars.roome.custom.CustomDialog
import com.sevenstars.roome.custom.CustomToast
import com.sevenstars.roome.databinding.ActivitySignInBinding
import com.sevenstars.roome.utils.AnalyticsHelper
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.splash.StartActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private val viewModel: SignInViewModel by viewModels()

    @Inject
    lateinit var kakaoAuthService: KakaoAuthService

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    private val googleAuthLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

        try {
            val account = task.getResult(ApiException::class.java)
            LoggerUtils.debug("구글 ID 토큰 ${account.idToken}")
            viewModel.signIn(Provider.GOOGLE, null, account.idToken)
        } catch (e: ApiException){
            LoggerUtils.error(e.stackTraceToString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observer()
        initListener()
        showUnlinkDialog()

        AnalyticsHelper.logScreenView("sign_in")
    }

    private fun showUnlinkDialog(){
        if(intent.hasExtra("isUnlink")){
            CustomDialog.getInstance(CustomDialog.DialogType.UNLINK_SUCCESS, null).show(supportFragmentManager, "")
        }
    }

    private fun initListener(){
        binding.btnKakaoLogin.setOnClickListener {
            AnalyticsHelper.logButtonClick("sign_up_kakao")

            kakaoAuthService.signInKakao(viewModel::signIn)
        }

        binding.btnGoogleLogin.setOnClickListener {
            AnalyticsHelper.logButtonClick("sign_up_google")

            val signInIntent = googleSignInClient.signInIntent
            googleAuthLauncher.launch(signInIntent)
        }
    }

    private fun observer() {
        viewModel.loginState.observe(this){
            when(it){
                is UiState.Failure -> {
                    CustomToast.makeToast(this, it.message).show()
                    LoggerUtils.error("로그인 실패: ${it.message}")
                }
                is UiState.Loading -> {}
                is UiState.Success -> {
                    moveToStart()
                }
            }
        }
    }

    private fun moveToStart(){
        val intent = Intent(this, StartActivity::class.java)
        startActivity(intent)
        finish()
    }
}