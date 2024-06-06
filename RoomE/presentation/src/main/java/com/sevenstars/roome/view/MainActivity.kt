package com.sevenstars.roome.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.domain.repository.auth.AuthRepository
import com.sevenstars.domain.repository.auth.UserPreferencesRepository
import com.sevenstars.roome.base.RoomeApplication.Companion.app
import com.sevenstars.roome.custom.CustomToast
import com.sevenstars.roome.databinding.ActivityMainBinding
import com.sevenstars.roome.view.splash.StartActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogout.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                authRepository.unlink(app.userPreferences.getAccessToken().getOrNull().orEmpty(), code=null, provider = app.userPreferences.getLoginProvider().getOrNull().orEmpty())
                    .onSuccess {
                        launch {
                            userPreferencesRepository.clearData()
                                .onSuccess {
                                    val intent = Intent(this@MainActivity, StartActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }.onFailure {
                                    LoggerUtils.error(it.stackTraceToString())
                                }
                        }
                    }.onFailure { _, msg ->
                        LoggerUtils.error(msg)
                        CustomToast.makeToast(this@MainActivity, "회원탈퇴 실패").show()
                    }


            }
        }
    }
}