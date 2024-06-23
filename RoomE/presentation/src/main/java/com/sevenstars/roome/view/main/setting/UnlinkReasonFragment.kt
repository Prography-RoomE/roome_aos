package com.sevenstars.roome.view.main.setting

import androidx.fragment.app.Fragment
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentUnlinkReasonBinding
import com.sevenstars.roome.view.main.MainActivity

class UnlinkReasonFragment: BaseFragment<FragmentUnlinkReasonBinding>(R.layout.fragment_unlink_reason) {
    override fun initView() {
        (requireActivity() as MainActivity).setBottomNaviVisibility(false)
    }

    override fun initListener() {
        super.initListener()

        binding.apply {
            tbUnlink.btnBack.setOnClickListener { requireActivity().supportFragmentManager.popBackStack() }
            tbUnlink.btnNext.setOnClickListener { moveFragment(UnlinkCheckFragment()) }

            tvReason1.setOnClickListener { moveFragment(UnlinkCheckFragment()) }
            tvReason2.setOnClickListener { moveFragment(UnlinkCheckFragment()) }
            tvReason3.setOnClickListener { moveFragment(UnlinkCheckFragment()) }
            tvReason4.setOnClickListener { moveFragment(UnlinkCheckFragment()) }
            tvReason5.setOnClickListener { moveFragment(UnlinkCheckFragment()) }
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