package com.sevenstars.roome.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.domain.repository.auth.UserPreferencesRepository
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogout.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                userPreferencesRepository.clearData()
                    .onSuccess {
                        val intent = Intent(this@MainActivity, StartActivity::class.java)
                        startActivity(intent)
                        finish()
                    }.onFailure {
                        LoggerUtils.error(it.stackTraceToString())
                    }
            }
        }
    }
}