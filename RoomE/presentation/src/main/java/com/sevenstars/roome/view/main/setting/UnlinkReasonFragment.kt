package com.sevenstars.roome.view.main.setting

import androidx.fragment.app.Fragment
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentUnlinkReasonBinding
import com.sevenstars.roome.utils.AnalyticsHelper
import com.sevenstars.roome.view.main.MainActivity

class UnlinkReasonFragment: BaseFragment<FragmentUnlinkReasonBinding>(R.layout.fragment_unlink_reason) {
    override fun initView() {
        AnalyticsHelper.logScreenView("exit_reason")
        (requireActivity() as MainActivity).setBottomNaviVisibility(false)
    }

    override fun initListener() {
        super.initListener()

        binding.apply {
            tbUnlink.btnBack.setOnClickListener { requireActivity().supportFragmentManager.popBackStack() }
            tbUnlink.btnNext.setOnClickListener { moveFragment(UnlinkCheckFragment("")) }

            tvReason1.setOnClickListener { moveFragment(UnlinkCheckFragment(binding.tvReason1.text.toString())) }
            tvReason2.setOnClickListener { moveFragment(UnlinkCheckFragment(binding.tvReason2.text.toString())) }
            tvReason3.setOnClickListener { moveFragment(UnlinkCheckFragment(binding.tvReason3.text.toString())) }
            tvReason4.setOnClickListener { moveFragment(UnlinkCheckFragment(binding.tvReason4.text.toString())) }
            tvReason5.setOnClickListener { moveFragment(UnlinkCheckFragment(binding.tvReason5.text.toString())) }
            tvReason6.setOnClickListener { moveFragment(UnlinkOpinionFragment()) }
        }
    }

    private fun moveFragment(fragment: Fragment){
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_main, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}