package com.sevenstars.roome.custom

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.sevenstars.roome.databinding.ToastCustomBinding


object CustomToast {
    fun makeToast(context: Context?, msg: String): Toast {
        val inflater = LayoutInflater.from(context)
        val binding = ToastCustomBinding.inflate(LayoutInflater.from(context))

        binding.tvMsg.text = msg

        return Toast(context).apply {
            duration = Toast.LENGTH_SHORT
            view = binding.root
        }
    }
}