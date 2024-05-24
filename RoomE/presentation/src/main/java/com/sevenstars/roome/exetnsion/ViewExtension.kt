package com.sevenstars.roome.exetnsion

import android.content.res.Resources
import android.graphics.Color
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
    endColor: String
) {
    val cornerRadius = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        12f,
        Resources.getSystem().displayMetrics
    )

    when (mode) {
        "solid" -> {
            val shapeDrawable = ShapeDrawable(RectShape())
            shapeDrawable.paint.color = Color.parseColor(startColor)
            shapeDrawable.shape = RectShapeWithCorners(cornerRadius)
            view.background = shapeDrawable
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
                val setOrientation = when(orientation){
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
            gradientDrawable.cornerRadius = cornerRadius

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
