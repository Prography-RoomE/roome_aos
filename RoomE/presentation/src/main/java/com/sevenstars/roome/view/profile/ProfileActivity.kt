package com.sevenstars.roome.view.profile

import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseActivity
import com.sevenstars.roome.databinding.ActivityProfileBinding
import com.sevenstars.roome.utils.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity: BaseActivity<ActivityProfileBinding>(R.layout.activity_profile) {
    private val viewModel: ProfileViewModel by viewModels()

    override fun initView() {
        observer()
        viewModel.fetchData()
    }

    override fun initListener() {
        super.initListener()

        binding.tbProfile.btnBack.setOnClickListener {
            supportFragmentManager.popBackStack()
        }
    }

    private fun observer(){
        viewModel.profileState.observe(this){
            when(it){
                is UiState.Failure -> {}
                is UiState.Loading -> {}
                is UiState.Success -> {
                    moveStep(it.data.name)
                }
            }
        }
    }

    private fun moveStep(step: String){
        LoggerUtils.debug(step)
        val destination = when(step){
            "1" -> ProfileWelcomeFragment()
            "2" -> ProfileCountFragment()
            "3" -> ProfileWelcomeFragment()
            else -> ProfileWelcomeFragment()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_profile, destination)
            .commit()
    }

    fun setToolbarVisibility(p: Boolean){
        binding.tbProfile.clProfileTb.isVisible = p
    }

    fun setStep(p: Int){
        binding.tbProfile.customStepper.setStep(p)
    }
}