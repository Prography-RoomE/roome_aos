package com.sevenstars.roome.view.profile.genres

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(context: Context, spaceDp: Int) : RecyclerView.ItemDecoration() {

    private val spacePx: Int
    private val bottomMarginPx: Int

    init {
        val density = context.resources.displayMetrics.density
        spacePx = (spaceDp * density).toInt()
        bottomMarginPx = (98 * density).toInt()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % 2

        if (column == 0) {
            outRect.right = spacePx
        } else {
            outRect.left = spacePx
        }

        if (position == state.itemCount - 1) {
            outRect.bottom = bottomMarginPx
        }
    }
}
