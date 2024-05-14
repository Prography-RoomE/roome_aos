package com.sevenstars.roome.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.sevenstars.data.utils.LoggerUtils

abstract class BaseActivity<T: ViewDataBinding>(@LayoutRes private val layoutId: Int): AppCompatActivity() {
    lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        beforeSetContentView()
        super.onCreate(savedInstanceState)
        LoggerUtils.debug("onCreate: $localClassName")

        binding = DataBindingUtil.setContentView(this, layoutId)
        initView()
        initListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        LoggerUtils.debug("onDestroy: $localClassName")
    }

    protected open fun beforeSetContentView() {}
    protected abstract fun initView()
    protected open fun initListener() {}

}