package com.carswaddle.carswaddleandroid.ui.activities.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.R
import java.util.*

class MechanicFragment : Fragment() {

    private var itemWidth: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mechanic, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.mechanic_list_container)
        with (recyclerView) {
            this.adapter = MyMechanicRecyclerViewAdapter(
                listOf(MyMechanicRecyclerViewAdapter.Mechanic(), MyMechanicRecyclerViewAdapter.Mechanic(), MyMechanicRecyclerViewAdapter.Mechanic())
            )
            this.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            PagerSnapHelper().attachToRecyclerView(this)
        }
        runJustBeforeBeingDrawn(recyclerView, Runnable {
            if (itemWidth > 0 || recyclerView.layoutManager!!.itemCount < 1) {
                return@Runnable
            }
            itemWidth = recyclerView.layoutManager!!.findViewByPosition(0)!!.width
            recyclerView.layoutManager = MechanicLinearLayoutManager(context, activity!!.window.decorView.width, itemWidth)
        })

        return view
    }

    private fun runJustBeforeBeingDrawn(view: View, runnable: Runnable) {
        val preDrawListener: ViewTreeObserver.OnPreDrawListener =
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    view.viewTreeObserver.removeOnPreDrawListener(this)
                    runnable.run()
                    return true
                }
            }
        view.viewTreeObserver.addOnPreDrawListener(preDrawListener)
    }

}