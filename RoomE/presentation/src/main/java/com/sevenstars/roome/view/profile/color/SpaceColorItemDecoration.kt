package com.sevenstars.roome.view.profile.color

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceColorItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val spacePx: Int
    private val bottomMarginPx: Int

    init {
        val density = context.resources.displayMetrics.density
        spacePx = (12 * density).toInt()
        bottomMarginPx = (16 * density).toInt()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position: Int = parent.getChildAdapterPosition(view)

        if (position >= 0) {
            val column = position % 3 // item column
            outRect.apply {
                left = spacePx - column * spacePx / 3
                right = (column + 1) * spacePx / 3
                if (position < 3) top = spacePx
                bottom = bottomMarginPx
            }
        } else {
            outRect.apply {
                left = 0
                right = 0
                top = 0
                bottom = 0
            }
        }
    }
}
