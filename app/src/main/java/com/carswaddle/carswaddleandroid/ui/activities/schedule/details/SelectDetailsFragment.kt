package com.carswaddle.carswaddleandroid.ui.activities.schedule.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.vehicle.Vehicle
import com.carswaddle.carswaddleandroid.ui.common.CenteredLinearLayoutManager
import kotlinx.android.synthetic.main.fragment_autoservices_list.*
import java.util.*

class SelectDetailsFragment : Fragment() {

    private var vehicleItemWidth: Int = 0

    private lateinit var selectDetailsViewModel: SelectDetailsViewModel
    
    private val vehicleAdapter: VehicleRecyclerViewAdapter = VehicleRecyclerViewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_select_details, container, false)

        selectDetailsViewModel = ViewModelProviders.of(this).get(SelectDetailsViewModel::class.java)
        
        val vehicleRecyclerView = view.findViewById<RecyclerView>(R.id.vehicle_container)
        with (vehicleRecyclerView) {
            this.adapter = vehicleAdapter
            this.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(this)
            this.onFlingListener = snapHelper
        }

        selectDetailsViewModel.vehicles.observe(viewLifecycleOwner, Observer<List<Vehicle>> { vehicles ->
            Log.w("vehicles", "vehicles listed")
            activity?.runOnUiThread {
                this.vehicleAdapter.vehicles = vehicles
                vehicleRecyclerView.scrollToPosition(1) // 0 is padding, 1 is first item
            }
        })

        val recyclerView = view.findViewById<RecyclerView>(R.id.oil_type_container)
        val oilTypeSnapHelper = PagerSnapHelper()
        with(recyclerView) {
            val oilTypeAdapter =
                    // TODO - make these enums
                OilTypeRecyclerViewAdapter(
                    listOf(
                        "Conventional", "Blend", "Synthetic", "High Mileage"
                    ), requireActivity()
                )
            
            this.adapter = oilTypeAdapter
            this.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            oilTypeSnapHelper.attachToRecyclerView(recyclerView)
            this.onFlingListener = oilTypeSnapHelper
            smoothScrollToPosition(1)
            recyclerView
                .addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            val snapView = oilTypeSnapHelper.findSnapView(layoutManager)
                            if (snapView == null) {
                                return
                            }
                            val snapPosition = layoutManager!!.getPosition(snapView)
                            oilTypeAdapter.selectedPosition = snapPosition
                            oilTypeAdapter.notifyDataSetChanged()
                        }
                    }
                })
        }

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