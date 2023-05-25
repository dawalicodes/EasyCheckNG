package com.dawalisoftwares.easycheck.adapters

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dawalisoftwares.easycheck.adapters.USSDAdapter.UssdViewHolder

class USSDItemDecorator : RecyclerView.ItemDecoration() {
    private val spacing: Int = 16
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.findContainingViewHolder(view) is UssdViewHolder) {
            outRect.left = spacing
            outRect.right = spacing
            outRect.top = spacing
            outRect.bottom = 0
        }else if (parent.findContainingViewHolder(view) is ActionViewHolder) {
            outRect.top = 0
            outRect.left = spacing
            outRect.right = spacing
            outRect.bottom = 0
        }

    }
}