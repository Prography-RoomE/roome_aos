package com.sevenstars.roome.view.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.R
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.MainActivity
import com.sevenstars.roome.view.signIn.SignInActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity: AppCompatActivity() {
    private val viewModel: StartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        viewModel.doValidation()

        observer()
    }

    private fun moveActivity(p: Activity){
        val intent = Intent(this, p::class.java)
        startActivity(intent)
        finish()
    }

    private fun observer() {
        viewModel.loginState.observe(this){
            when(it){
                is UiState.Failure -> {
                    LoggerUtils.error("유효성 검사 실패: ${it.message}")
                    moveActivity(SignInActivity())
                }
                is UiState.Loading -> {}
                is UiState.Success -> moveActivity(MainActivity())
            }
        }
    }
}