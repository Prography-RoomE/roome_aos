package com.sevenstars.roome.view.main.setting

import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentMainSettingBinding
import com.sevenstars.roome.view.main.MainActivity

class MainSettingFragment: BaseFragment<FragmentMainSettingBinding>(R.layout.fragment_main_setting) {
    override fun initView() {
        (requireActivity() as MainActivity).setBottomNaviVisibility(true)

    }
}