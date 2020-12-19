package com.carswaddle.carswaddleandroid.Extensions

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter

fun <VH : RecyclerView.ViewHolder> RecyclerView.updateTransformForScrollOffset(
    viewAdapter: Adapter<VH>,
    minSize: Float = 0.7f
) {
    var rL = IntArray(2)
    getLocationOnScreen(rL)

    val recyclerViewX = rL[0]

    for (i in 0..viewAdapter.itemCount) {
        val view = getChildAt(i)
        if (view == null) {
            continue
        }
        var vL = IntArray(2)
        view.getLocationOnScreen(vL)
        val vX = vL[0]
        val vY = vL[1]

        val vXRelativeToRecycler = vX - recyclerViewX

        // Relative to recycler
        val locationOfViewCenterX = (view.width / 2) + vXRelativeToRecycler
        val locationOfRecyclerCenterX = (width / 2)

        val maxDifference = locationOfRecyclerCenterX

        val difference = Math.abs(locationOfViewCenterX) - Math.abs(locationOfRecyclerCenterX)
        val percentageSmaller = Math.abs(difference.toFloat() / maxDifference.toFloat())

        val maxShrinkage = 1.0 - minSize

        val newScale = (minSize + (maxShrinkage * (1.0 - percentageSmaller))).toFloat()
        
        view.scaleX = newScale
        view.scaleY = newScale
    }
}