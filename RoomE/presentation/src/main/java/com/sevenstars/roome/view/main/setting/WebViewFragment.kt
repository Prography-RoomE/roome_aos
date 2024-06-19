package com.sevenstars.roome.view.main.setting

import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentWebviewBinding
import com.sevenstars.roome.view.main.MainActivity
import com.sevenstars.roome.view.signup.SignUpViewModel

class WebViewFragment(
    private val title: String,
    private val link: String,
    private val agreeBtnVisibility: Boolean
): BaseFragment<FragmentWebviewBinding>(R.layout.fragment_webview) {
    private val viewModel: SignUpViewModel by activityViewModels()

    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(binding.viewWeb.canGoBack()) binding.viewWeb.goBack()
            else requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun initView() {
        requireActivity().onBackPressedDispatcher.addCallback(this. onBackPressedCallback)
        visibilityNavi()

        LoggerUtils.info("WebView!")

        binding.tvWebViewTitle.text = title
        binding.btnAgree.isVisible = agreeBtnVisibility

        settingWebViewClient()
    }

    override fun initListener() {
        super.initListener()

        binding.btnAgree.setOnClickListener {
            viewModel.updateAgreementTitle = title
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.ibCancel.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun visibilityNavi(){
        if(requireActivity().localClassName == "view.main.MainActivity"){
            (requireActivity() as MainActivity).setBottomNaviVisibility(false)
        }
    }

    private fun settingWebViewClient(){
        binding.viewWeb.apply {
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            settings.javaScriptEnabled = true
            settings.useWideViewPort = true
            settings.domStorageEnabled = true
            settings.userAgentString = "app"
        }

        binding.viewWeb.loadUrl(link)
    }


    override fun onDestroyView() {
        super.onDestroyView()

        onBackPressedCallback.remove()
    }
}