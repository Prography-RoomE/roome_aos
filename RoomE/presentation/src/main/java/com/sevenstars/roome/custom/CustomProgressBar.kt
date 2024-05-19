package com.sevenstars.roome.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.sevenstars.roome.R
import com.sevenstars.roome.utils.Converter

class CustomProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var stepCount = 11
    private var currentStep = 0

    init {
        orientation = HORIZONTAL
        weightSum = stepCount.toFloat()
        setupSteps()
    }

    private fun setupSteps() {
        for (i in 0 until stepCount) {
            val stepView = View(context)
            val params = LayoutParams(0, Converter.ConvertDPtoPX(context, 2f), 1.0f)
            params.setMargins(
                Converter.ConvertDPtoPX(context, 4.8f),
                Converter.ConvertDPtoPX(context, 12f),
                Converter.ConvertDPtoPX(context, 4.8f),
                Converter.ConvertDPtoPX(context, 12f))
            stepView.layoutParams = params
            stepView.background = ContextCompat.getDrawable(context, R.drawable.shape_stepper)
            addView(stepView)
        }
    }

    fun setStep(step: Int) {
        if (step in 0..stepCount) {
            currentStep = step
            updateSteps()
        }
    }

    private fun updateSteps() {
        for (i in 0 until stepCount) {
            val stepView = getChildAt(i)
            if (i < currentStep) {
                stepView.background = ContextCompat.getDrawable(context, R.drawable.shape_stepper_activate)
            } else {
                stepView.background = ContextCompat.getDrawable(context, R.drawable.shape_stepper)
            }
        }
    }
}
