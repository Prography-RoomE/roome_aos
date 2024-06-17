package com.sevenstars.roome.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.domain.repository.auth.AuthRepository
import com.sevenstars.domain.repository.auth.UserPreferencesRepository
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseActivity
import com.sevenstars.roome.base.RoomeApplication.Companion.app
import com.sevenstars.roome.custom.CustomToast
import com.sevenstars.roome.databinding.ActivityMainBinding
import com.sevenstars.roome.view.main.profile.MainProfileFragment
import com.sevenstars.roome.view.main.setting.MainSettingFragment
import com.sevenstars.roome.view.splash.StartActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun initView() {
        replaceFragment(MainProfileFragment())
        setBottomNavi()
    }

    private fun setBottomNavi(){


        binding.bottomNavi.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.navi_item_profile -> {
                    replaceFragment(MainProfileFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.navi_item_setting -> {
                    replaceFragment(MainSettingFragment())
                    return@setOnItemSelectedListener true
                }
                else -> return@setOnItemSelectedListener false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fc_main, fragment)
        fragmentTransaction.commit()
    }
}