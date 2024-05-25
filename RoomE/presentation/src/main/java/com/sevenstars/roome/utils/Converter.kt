package com.sevenstars.roome.utils

import android.content.Context
import kotlin.math.roundToInt


object Converter {
    fun ConvertDPtoPX(context: Context, dp: Float): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).roundToInt()
    }
}