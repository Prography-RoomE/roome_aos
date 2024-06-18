package com.sevenstars.roome.view.main.setting

import android.content.Intent
import androidx.fragment.app.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.sevenstars.data.service.auth.KakaoAuthService
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.domain.enums.Provider
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.base.RoomeApplication.Companion.app
import com.sevenstars.roome.custom.CustomDialog
import com.sevenstars.roome.databinding.FragmentMainSettingBinding
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.main.MainActivity
import com.sevenstars.roome.view.splash.StartActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainSettingFragment: BaseFragment<FragmentMainSettingBinding>(R.layout.fragment_main_setting) {
    private val viewModel: MainSettingViewModel by viewModels()

    @Inject
    lateinit var kakaoAuthService: KakaoAuthService

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    override fun initView() {
        (requireActivity() as MainActivity).setBottomNaviVisibility(true)
    }

    override fun initListener() {
        super.initListener()

        binding.tvTos.setOnClickListener { moveWebView("서비스 이용약관", "https://chief-collarbone-3ba.notion.site/faf7c25d73ed45a094177f8904868286?pvs=4") }
        binding.ibTos.setOnClickListener { moveWebView("서비스 이용약관", "https://chief-collarbone-3ba.notion.site/faf7c25d73ed45a094177f8904868286?pvs=4") }

        binding.tvPrivacy.setOnClickListener { moveWebView("개인정보처리방침", "https://chief-collarbone-3ba.notion.site/faf7c25d73ed45a094177f8904868286?pvs=4") }
        binding.ibPrivacy.setOnClickListener { moveWebView("개인정보처리방침", "https://chief-collarbone-3ba.notion.site/faf7c25d73ed45a094177f8904868286?pvs=4") }

        binding.tvUpdate.setOnClickListener {  }
        binding.ibUpdate.setOnClickListener {  }

        binding.tvLogout.setOnClickListener { signOut() }
        binding.ibLogout.setOnClickListener { signOut() }

        binding.tvUnlink.setOnClickListener { unlink() }
        binding.ibUnlink.setOnClickListener { unlink() }
    }

    private fun moveWebView(title: String, url: String){
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_main, WebViewFragment(title, url, false))
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun signOut(){
        CustomDialog.getInstance(CustomDialog.DialogType.SIGN_OUT, null).apply {
            setButtonClickListener(object: CustomDialog.OnButtonClickListener{
                override fun onButton1Clicked() { }
                override fun onButton2Clicked() {
                    CoroutineScope(Dispatchers.IO).launch {
                        val provider = app.userPreferences.getLoginProvider().getOrNull().orEmpty()
                        launch(Dispatchers.Main){
                            if(provider == Provider.KAKAO.provider) signOutKakao()
                            else signOutGoogle()
                        }
                    }
                }
            })
        }.show(requireActivity().supportFragmentManager, "SignOut")
    }

    private fun unlink(){
        CustomDialog.getInstance(CustomDialog.DialogType.UNLINK, null).apply {
            setButtonClickListener(object: CustomDialog.OnButtonClickListener{
                override fun onButton1Clicked() { }
                override fun onButton2Clicked() {
                    CoroutineScope(Dispatchers.IO).launch {
                        val provider = app.userPreferences.getLoginProvider().getOrNull().orEmpty()
                        launch(Dispatchers.Main){
                            if(provider == Provider.KAKAO.provider) unlinkKakao()
                            else unlinkGoogle()
                        }
                    }
                }
            })
        }.show(requireActivity().supportFragmentManager, "Unlink")
    }

    private fun signOutKakao(){
        kakaoAuthService.signOut { error ->
            if(error != null){
                showToast("카카오 로그아웃 실패: ${error.message}")
            } else {
                LoggerUtils.info("카카오 로그아웃 성공")
                viewModel.signOut()
            }
        }
    }

    private fun signOutGoogle(){
        googleSignInClient.signOut()
            .addOnCompleteListener(requireActivity()){
                LoggerUtils.info("구글 로그아웃 성공")
                viewModel.signOut()
            }
            .addOnFailureListener{
                showToast("구글 로그아웃 실패: ${it.message}")
            }
    }

    private fun unlinkKakao(){
        kakaoAuthService.withdraw { error ->
            if (error != null) {
                showToast("카카오 회원탈퇴 실패: ${error.message}")
            } else {
                LoggerUtils.info("카카오 회원탈퇴 성공")
                viewModel.unlink()
            }
        }
    }

    private fun unlinkGoogle(){
        googleSignInClient.revokeAccess()
            .addOnCompleteListener(requireActivity()){
                LoggerUtils.info("구글 회원탈퇴 성공")
                viewModel.unlink()
            }
            .addOnFailureListener{
                showToast("구글 회원탈퇴 실패: ${it.message}")
            }

    }

    override fun observer() {
        super.observer()

        viewModel.signOutState.observe(viewLifecycleOwner){
            when(it){
                is UiState.Failure -> {
                    showToast("로그아웃 실패: ${it.message}")
                }
                is UiState.Loading -> {}
                is UiState.Success -> {
                    removeData()
                }
            }
        }

        viewModel.unlinkState.observe(viewLifecycleOwner){
            when(it){
                is UiState.Failure -> {
                    showToast("회원탈퇴 실패: ${it.message}")
                }
                is UiState.Loading -> {}
                is UiState.Success -> {
                    removeData()
                }
            }
        }
    }

    private fun removeData(){
        CoroutineScope(Dispatchers.IO).launch {
            app.userPreferences.clearData()
                .onSuccess {
                    launch(Dispatchers.Main){
                        restartApplication()
                    }
                }.onFailure { _ ->
                    launch(Dispatchers.Main){
                        showToast("데이터 삭제 실패")
                    }
                }
        }
    }

    private fun restartApplication() {
        val intent = Intent(requireContext(), StartActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        requireActivity().finish()
    }
}