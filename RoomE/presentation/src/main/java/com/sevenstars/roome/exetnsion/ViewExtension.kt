package com.sevenstars.roome.exetnsion

import android.content.res.Resources
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.util.TypedValue
import android.view.View

/*
    프로필 생성 중 색 선택에서 서버에서 받은 값을 바탕으로
    뷰의 백그라운드 색을 설정
 */

fun setColorBackground(
    view: View,
    mode: String,
    shape: String,
    orientation: String,
    startColor: String,
    endColor: String,
    isRoundCorner: Boolean,
    radius: Float = 12f,
    hasStroke: Boolean = false
) {
    val cornerRadius = if (isRoundCorner) {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            radius,
            Resources.getSystem().displayMetrics
        )
    } else {
        0f
    }

    val strokeWidth = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        2f,
        Resources.getSystem().displayMetrics
    )
    val strokeColor = Color.BLACK

    when (mode) {
        "solid" -> {
            val gradientDrawable = GradientDrawable()
            gradientDrawable.setColor(Color.parseColor(startColor))
            if (isRoundCorner) gradientDrawable.cornerRadius = cornerRadius
            if (hasStroke) gradientDrawable.setStroke(strokeWidth.toInt(), strokeColor)
            view.background = gradientDrawable
        }
        "gradient" -> {
            val gradientDrawable = GradientDrawable()

            gradientDrawable.gradientType = when (shape) {
                "linear" -> GradientDrawable.LINEAR_GRADIENT
                "radial" -> GradientDrawable.RADIAL_GRADIENT
                "sweep" -> GradientDrawable.SWEEP_GRADIENT
                "diamond" -> GradientDrawable.RADIAL_GRADIENT // Diamond 형태 지원 x
                else -> GradientDrawable.LINEAR_GRADIENT
            }

            if (shape == "diamond") {
                gradientDrawable.gradientRadius = 500f
                gradientDrawable.shape = GradientDrawable.OVAL
            } else {
                val setOrientation = when (orientation) {
                    "topLeftToBottomRight" -> GradientDrawable.Orientation.TL_BR
                    "topRightToBottomLeft" -> GradientDrawable.Orientation.TR_BL
                    "bottomLeftToTopRight" -> GradientDrawable.Orientation.BL_TR
                    "bottomRightToTopLeft" -> GradientDrawable.Orientation.BR_TL
                    "topToBottom" -> GradientDrawable.Orientation.TOP_BOTTOM
                    "bottomToTop" -> GradientDrawable.Orientation.BOTTOM_TOP
                    "none" -> GradientDrawable.Orientation.TL_BR
                    else -> GradientDrawable.Orientation.TL_BR
                }

                gradientDrawable.orientation = setOrientation
            }

            gradientDrawable.colors = intArrayOf(Color.parseColor(startColor), Color.parseColor(endColor))
            if (isRoundCorner) gradientDrawable.cornerRadius = cornerRadius
            if (hasStroke) gradientDrawable.setStroke(strokeWidth.toInt(), strokeColor)

            view.background = gradientDrawable
        }
        else -> throw IllegalArgumentException("Unsupported mode: $mode")
    }
}

class RectShapeWithCorners(private val radius: Float) : RectShape() {
    override fun draw(canvas: android.graphics.Canvas, paint: android.graphics.Paint) {
        val rect = rect()
        val r = radius
        canvas.drawRoundRect(rect, r, r, paint)
    }
}

