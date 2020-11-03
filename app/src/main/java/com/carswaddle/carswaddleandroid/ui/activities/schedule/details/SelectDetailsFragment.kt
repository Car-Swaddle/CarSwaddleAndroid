package com.carswaddle.carswaddleandroid.ui.activities.schedule.details

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
import com.carswaddle.carswaddleandroid.ui.common.CenteredLinearLayoutManager

class SelectDetailsFragment : Fragment() {

    private var vehicleItemWidth: Int = 0
    private var oilTypeItemWidth: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_select_details, container, false)

        val vehicleRecyclerView = view.findViewById<RecyclerView>(R.id.vehicle_container)
        with (vehicleRecyclerView) {
            this.adapter =
                    // TODO - make these actual values
                VehicleRecyclerViewAdapter(
                    listOf(
                        "Conventional", "Blend", "Synthetic", "High Mileage"
                    )
                )
            this.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            PagerSnapHelper().attachToRecyclerView(this)
        }
        runJustBeforeBeingDrawn(vehicleRecyclerView, Runnable {
            if (vehicleItemWidth > 0 || vehicleRecyclerView.layoutManager!!.itemCount < 1) {
                return@Runnable
            }
            vehicleItemWidth = vehicleRecyclerView.layoutManager!!.findViewByPosition(0)!!.width
            vehicleRecyclerView.layoutManager =
                CenteredLinearLayoutManager(
                    context,
                    requireActivity().window.decorView.width,
                    vehicleItemWidth
                )
        })

        val recyclerView = view.findViewById<RecyclerView>(R.id.oil_type_container)
        with (recyclerView) {
            this.adapter =
                    // TODO - make these enums
                OilTypeRecyclerViewAdapter(
                    listOf(
                        "Conventional", "Blend", "Synthetic", "High Mileage"
                    )
                )
            this.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            PagerSnapHelper().attachToRecyclerView(this)
        }
        runJustBeforeBeingDrawn(recyclerView, Runnable {
            if (oilTypeItemWidth > 0 || recyclerView.layoutManager!!.itemCount < 1) {
                return@Runnable
            }
            oilTypeItemWidth = recyclerView.layoutManager!!.findViewByPosition(0)!!.width
            recyclerView.layoutManager =
                CenteredLinearLayoutManager(
                    context,
                    requireActivity().window.decorView.width,
                    oilTypeItemWidth
                )
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