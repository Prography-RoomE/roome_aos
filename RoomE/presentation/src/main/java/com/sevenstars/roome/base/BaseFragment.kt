package com.sevenstars.roome.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.sevenstars.roome.custom.CustomToast

abstract class BaseFragment<T : ViewDataBinding>(@LayoutRes val layoutId : Int): Fragment() {
    private var _binding : T? = null
    protected val binding get() = _binding!!

    private var currentToast: Toast? = null

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
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    protected open fun afterViewCreated() {}

    protected abstract fun initView()

    protected open fun initListener() {}

    protected open fun observer() {}

    protected fun showToast(msg: String) {
        currentToast?.cancel()
        currentToast = CustomToast.makeToast(context, msg)
        currentToast?.show()
    }
}
