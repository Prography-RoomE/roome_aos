package com.sevenstars.roome.view.main

import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseActivity
import com.sevenstars.roome.databinding.ActivityMainBinding
import com.sevenstars.roome.view.main.profile.MainProfileFragment
import com.sevenstars.roome.view.main.setting.MainSettingFragment
import dagger.hilt.android.AndroidEntryPoint

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
        fragmentTransaction.replace(R.id.fl_main, fragment)
        fragmentTransaction.commit()
    }

    fun setBottomNaviVisibility(p: Boolean){
        binding.bottomNavi.isVisible = p
    }
}