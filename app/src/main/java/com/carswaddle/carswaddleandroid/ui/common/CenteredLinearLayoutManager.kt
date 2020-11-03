package com.carswaddle.carswaddleandroid.ui.common

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class CenteredLinearLayoutManager(
    context: Context?,
    private val parentWidth: Int,
    private val itemWidth: Int
) : LinearLayoutManager(context, RecyclerView.HORIZONTAL, false) {

    override fun getPaddingLeft(): Int {
        return (parentWidth / 2f - itemWidth / 2f).roundToInt()
    }

    override fun getPaddingRight(): Int {
        return paddingLeft;
    }

}