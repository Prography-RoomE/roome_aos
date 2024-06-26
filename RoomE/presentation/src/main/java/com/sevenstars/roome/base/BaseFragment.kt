package com.sevenstars.roome.base

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_NETWORK_OPERATOR_SETTINGS
import android.provider.Settings.ACTION_SETTINGS
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.custom.CustomDialog
import com.sevenstars.roome.custom.CustomToast

abstract class BaseFragment<T : ViewDataBinding>(@LayoutRes val layoutId : Int): Fragment() {
    private var _binding : T? = null
    protected val binding get() = _binding!!

    private var currentToast: Toast? = null
    private lateinit var noConnectionDialog: DialogFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        initView()
        initListener()
        observer()
        afterViewCreated()
        setupKeyboardListener()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    protected open fun afterViewCreated() {}

    protected abstract fun initView()

    protected open fun initListener() {}

    protected open fun observer() {}

    protected open fun onKeyboardVisibilityChanged(isVisible: Boolean) {}

    protected fun showToast(msg: String) {
        currentToast?.cancel()
        currentToast = CustomToast.makeToast(context, msg)
        currentToast?.show()
    }

    protected fun hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    protected fun showKeyboardAndFocus(view: View) {
        view.requestFocus()
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun Context.hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun setupKeyboardListener() {
        val rootView = view?.rootView
        rootView?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            private val rect = Rect()

            override fun onGlobalLayout() {
                rootView.getWindowVisibleDisplayFrame(rect)
                val screenHeight = rootView.height
                val keypadHeight = screenHeight - rect.bottom

                if (keypadHeight > screenHeight * 0.15) {
                    onKeyboardVisibilityChanged(true)
                } else {
                    onKeyboardVisibilityChanged(false)
                }
            }
        })
    }

    fun showNoConnectionDialog(containerId: Int?, fragment: Fragment? = null, isReplace: Boolean = true){
        CustomDialog.getInstance(CustomDialog.DialogType.NO_CONNECTION, null).apply {
            LoggerUtils.info(this@BaseFragment.requireActivity().localClassName)
            setButtonClickListener(object : CustomDialog.OnButtonClickListener{
                override fun onButton1Clicked() { // 다시 시도

                    if(isReplace){
                        requireActivity().supportFragmentManager.beginTransaction().replace(containerId!!, fragment ?: this@BaseFragment).commit()
                    } else {
                        requireActivity().supportFragmentManager.beginTransaction().detach(fragment ?: this@BaseFragment).commit()
                        requireActivity().supportFragmentManager.beginTransaction().attach(fragment ?: this@BaseFragment).commit()
                    }
                    dismiss()
                }

                override fun onButton2Clicked() { // 설정 페이지
                    showToast("test")
                    val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(intent)
                }
            })
        }.show(requireActivity().supportFragmentManager, "no_connection_dialog")
    }
}
