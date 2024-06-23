package com.sevenstars.roome.view.main

import android.content.Intent
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseActivity
import com.sevenstars.roome.base.RoomeApplication
import com.sevenstars.roome.custom.CustomDialog
import com.sevenstars.roome.databinding.ActivityMainBinding
import com.sevenstars.roome.view.main.profile.MainProfileFragment
import com.sevenstars.roome.view.main.setting.MainSettingFragment
import com.sevenstars.roome.view.splash.StartActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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

        binding.bottomNavi.setOnItemReselectedListener {  } // 재요청 방지용
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_main, fragment)
        fragmentTransaction.commit()
    }

    fun setBottomNaviVisibility(p: Boolean){
        binding.bottomNavi.isVisible = p
    }

    fun removeData(){
        CoroutineScope(Dispatchers.IO).launch {
            RoomeApplication.app.userPreferences.clearData()
                .onSuccess {
                    launch(Dispatchers.Main){
                        restartApplication()
                    }
                }.onFailure { _ ->
                    launch(Dispatchers.Main){
                        LoggerUtils.error("데이터 삭제 실패")
                    }
                }
        }
    }

    private fun restartApplication() {
        val intent = Intent(this, StartActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtra("isUnlink", true)
        startActivity(intent)
        finish()
    }
}