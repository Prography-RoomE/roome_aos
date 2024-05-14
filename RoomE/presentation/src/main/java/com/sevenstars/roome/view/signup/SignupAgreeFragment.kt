package com.sevenstars.roome.view.signup

import androidx.fragment.app.activityViewModels
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentSignupAgreeBinding

class SignupAgreeFragment: BaseFragment<FragmentSignupAgreeBinding>(R.layout.fragment_signup_agree) {
    private val viewModel: SignUpViewModel by activityViewModels()

    override fun initView() {

    }

    override fun initListener() {
        super.initListener()

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun observer() {
        super.observer()

    }
}