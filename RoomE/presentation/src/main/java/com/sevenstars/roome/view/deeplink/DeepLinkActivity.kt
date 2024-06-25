package com.sevenstars.roome.view.deeplink

import androidx.fragment.app.Fragment
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseActivity
import com.sevenstars.roome.databinding.ActivityDeeplinkBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeepLinkActivity: BaseActivity<ActivityDeeplinkBinding>(R.layout.activity_deeplink) {
    override fun initView() {
        val type = intent?.getStringExtra("type")
        val value = intent?.getStringExtra("value")

        when(type){
            "profile" -> replaceFragment(DeepLinkProfileFragment(value ?: ""), false)
            else -> {}
        }
    }

    fun replaceFragment(fragment: Fragment, isAddToBackStack: Boolean){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_deeplink, fragment)
        if(isAddToBackStack) fragmentTransaction.addToBackStack("")
        fragmentTransaction.commit()
    }
}