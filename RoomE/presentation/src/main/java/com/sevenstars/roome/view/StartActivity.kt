package com.sevenstars.roome.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sevenstars.roome.R
import com.sevenstars.roome.view.signIn.SignInActivity

class StartActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        getMyInfo()
    }

    private fun getMyInfo() { // 유효성 검사
        val result = false
        if(result) moveActivity(MainActivity()) else moveActivity(SignInActivity())
    }

    private fun moveActivity(p: Activity){
        val intent = Intent(this, p::class.java)
        startActivity(intent)
        finish()
    }
}