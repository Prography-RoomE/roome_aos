package com.sevenstars.roome.view.profile

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalSpaceItemDecoration(
    context: Context,
    spaceDp: Int,
    private val isButtonVisible: Boolean
) : RecyclerView.ItemDecoration() {

    private val bottomMarginPx: Int
    private val lastItemBottomMarginPx: Int

    init {
        val density = context.resources.displayMetrics.density
        bottomMarginPx = (spaceDp * density).toInt()
        lastItemBottomMarginPx = (84 * density).toInt()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        if (position == itemCount - 1 && isButtonVisible) {
            outRect.bottom = lastItemBottomMarginPx
        } else {
            outRect.bottom = bottomMarginPx
        }
    }
}
