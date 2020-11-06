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
import com.carswaddle.carswaddleandroid.ui.activities.autoserviceDetails.AutoServiceDetailsViewModel
import com.carswaddle.carswaddleandroid.ui.activities.autoservicelist.AutoServiceListElements
import com.carswaddle.carswaddleandroid.ui.common.CenteredLinearLayoutManager
import kotlinx.android.synthetic.main.fragment_autoservices_list.*
import java.util.*

class SelectDetailsFragment : Fragment() {

    private var vehicleItemWidth: Int = 0
    private var oilTypeItemWidth: Int = 0

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
        
//        runJustBeforeBeingDrawn(vehicleRecyclerView, Runnable {
//            if (vehicleItemWidth > 0 || vehicleRecyclerView.layoutManager!!.itemCount < 1) {
//                return@Runnable
//            }
//            vehicleItemWidth = vehicleRecyclerView.layoutManager!!.findViewByPosition(0)!!.width
//            vehicleRecyclerView.layoutManager =
//                CenteredLinearLayoutManager(
//                    context,
//                    requireActivity().window.decorView.width,
//                    vehicleItemWidth
//                )
//        })

        val recyclerView = view.findViewById<RecyclerView>(R.id.oil_type_container)
        with (recyclerView) {
            this.adapter =
                    // TODO - make these enums
                OilTypeRecyclerViewAdapter(
                    listOf(
                        "Conventional", "Blend", "Synthetic", "High Mileage"
                    )
                )
            activity?.runOnUiThread {
//                this.adapter.vehicles = vehicles
                vehicleRecyclerView.scrollToPosition(1) // 0 is padding, 1 is first item
            }
            this.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(this)
            this.onFlingListener = snapHelper
        }
//        runJustBeforeBeingDrawn(recyclerView, Runnable {
//            if (oilTypeItemWidth > 0 || recyclerView.layoutManager!!.itemCount < 1) {
//                return@Runnable
//            }
//            oilTypeItemWidth = recyclerView.layoutManager!!.findViewByPosition(0)!!.width
//            recyclerView.layoutManager =
//                CenteredLinearLayoutManager(
//                    context,
//                    requireActivity().window.decorView.width,
//                    oilTypeItemWidth
//                )
//        })

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