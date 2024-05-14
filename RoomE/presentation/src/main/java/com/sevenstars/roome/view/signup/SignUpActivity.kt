package com.sevenstars.roome.view.signup

import android.content.Intent
import androidx.activity.viewModels
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseActivity
import com.sevenstars.roome.databinding.ActivitySignUpBinding
import com.sevenstars.roome.view.MainActivity
import com.sevenstars.roome.view.signIn.SignInActivity

class SignUpActivity: BaseActivity<ActivitySignUpBinding>(R.layout.activity_sign_up) {
    private val viewModel: SignUpViewModel by viewModels()

    override fun initView() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_signup, SignupNickFragment())
            .commit()
    }

    fun moveToSignIn(){
        val intent = Intent(this, SignInActivity::class.java)
        // 호출하는 Activity가 스택에 있을 경우, 해당 Activity를 최상위로 올리면서, 그 위에 있던 Activity들을 모두 삭제
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    fun moveToMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}