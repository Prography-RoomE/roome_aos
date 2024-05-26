package com.sevenstars.roome.view.profile

import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseActivity
import com.sevenstars.roome.base.RoomeApplication.Companion.app
import com.sevenstars.roome.base.RoomeApplication.Companion.userName
import com.sevenstars.roome.custom.CustomToast
import com.sevenstars.roome.databinding.ActivityProfileBinding
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.profile.welcome.ProfileWelcomeFragment
import com.sevenstars.roome.view.splash.StartActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.userAgent

@AndroidEntryPoint
class ProfileActivity: BaseActivity<ActivityProfileBinding>(R.layout.activity_profile) {
    private val viewModel: ProfileViewModel by viewModels()

    override fun initView() {
        observer()
        viewModel.fetchSaveData()
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
                is UiState.Failure -> {
                    LoggerUtils.error("프로필 데이터 조회 실패\n${it.message}")
                    CustomToast.makeToast(this, "프로필 데이터 조회 실패\n${it.message}").show()

                    moveStart()
                }
                is UiState.Loading -> {}
                is UiState.Success -> {
                    viewModel.fetchDefaultData(it.data)
                }
            }
        }

        viewModel.profileDataState.observe(this){
            when(it){
                is UiState.Failure -> {
                    LoggerUtils.error("프로필 생성에 필요한 데이터 조회 실패\n${it.message}")
                    CustomToast.makeToast(this, "프로필 생성에 필요한 데이터 조회 실패\n${it.message}").show()

                    moveStart()
                }
                is UiState.Loading -> {}
                is UiState.Success -> {
                    // MVP - 메인페이지 없기 때문에 프로필 생성으로 이동
//                    if(it.data.step == ProfileState.COMPLETE.step) {
//                        val intent = Intent(this, MainActivity::class.java)
//                        startActivity(intent)
//                        finish()
//                    } else {
//                        replaceFragmentWithStack(ProfileWelcomeFragment(it.data.step))
//                    }
                    replaceFragmentWithStack(ProfileWelcomeFragment(it.data.step))
                }
            }
        }
    }

    fun setToolbarVisibility(p: Boolean){
        binding.tbProfile.clProfileTb.isVisible = p
    }

    fun setStep(p: Int){
        binding.tbProfile.customStepper.setStep(p)
    }

    fun replaceFragmentWithStack(p: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_profile, p)
            .addToBackStack(null)
            .commit()
    }


    private fun moveStart(){
        CoroutineScope(Dispatchers.IO).launch {
            app.userPreferences.clearData()
        }

        val intent = Intent(this, StartActivity::class.java)
        startActivity(intent)
        finish()
    }
}