package com.sevenstars.roome.view.signIn

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sevenstars.data.service.auth.KakaoAuthService
import com.sevenstars.roome.databinding.ActivitySignInBinding
import com.sevenstars.roome.base.RoomeApplication.Companion.logger
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private val viewModel: SignInViewModel by viewModels()

    @Inject
    lateinit var kakaoAuthService: KakaoAuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observer()

        binding.btnKakaoLogin.setOnClickListener {
            kakaoAuthService.signInKakao(viewModel::signIn)
        }
    }

    private fun observer() {
        viewModel.loginState.observe(this){
            when(it){
                is UiState.Failure -> {
                    Toast.makeText(this, it.error ?: "로그인 실패", Toast.LENGTH_SHORT).show()
                    logger.debug(it.error ?: "로그인 실패")
                }
                is UiState.Loading -> {}
                is UiState.Success -> moveToMain()
            }
        }
    }

    private fun moveToMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}