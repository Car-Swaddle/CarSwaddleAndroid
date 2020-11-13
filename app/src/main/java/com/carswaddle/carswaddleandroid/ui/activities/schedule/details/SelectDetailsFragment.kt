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
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.vehicle.Vehicle
import com.carswaddle.carswaddleandroid.services.serviceModels.OilType
import com.carswaddle.carswaddleandroid.services.serviceModels.Point

class SelectDetailsFragment(val point: Point, val mechanicId: String) : Fragment() {

    private var vehicleItemWidth: Int = 0

    private lateinit var selectDetailsViewModel: SelectDetailsViewModel
    private lateinit var oilTypeRecyclerView: RecyclerView

    private val vehicleAdapter: VehicleRecyclerViewAdapter = VehicleRecyclerViewAdapter()
    
    private var newVehicleId: String? = null
    private var hasScrolledToFirstVehicleIndex: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_select_details, container, false)

        selectDetailsViewModel = ViewModelProviders.of(this).get(SelectDetailsViewModel::class.java)

        selectDetailsViewModel.loadPrice(
            point.latitude(),
            point.longitude(),
            mechanicId,
            OilType.synthetic,
            null
        )

        val vehicleRecyclerView = view.findViewById<RecyclerView>(R.id.vehicle_container)
        with(vehicleRecyclerView) {
            this.adapter = vehicleAdapter
            this.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            val snapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(this)
            this.onFlingListener = snapHelper
        }

        vehicleAdapter.addVehicleClick = {
            val manager = activity?.supportFragmentManager
            if (manager != null) {
                val details = AddVehicleFragment()
                
                details.didAddVehicle = {
                    newVehicleId = it
                    selectDetailsViewModel.loadVehicles()
                }

                manager.beginTransaction()
                    .add(R.id.fragment_container, details)
                    .addToBackStack("Add vehicle")
                    .commit()
            }
        }

        selectDetailsViewModel.vehicles.observe(
            viewLifecycleOwner,
            Observer<List<Vehicle>> { vehicles ->
                Log.w("vehicles", "vehicles listed")
                activity?.runOnUiThread {
                    this.vehicleAdapter.vehicles = vehicles
                    val id = newVehicleId
                    if (id != null) {
                        val newVehicleIndex = vehicles.indexOfFirst { 
                            it.id == id
                        }
                        vehicleRecyclerView.smoothScrollToPosition(newVehicleIndex+1)
                    } else if(hasScrolledToFirstVehicleIndex == false) {
                        vehicleRecyclerView.smoothScrollToPosition(1) // 0 is padding, 1 is first item
                        hasScrolledToFirstVehicleIndex = true
                    }
                }
            })

        oilTypeRecyclerView = view.findViewById<RecyclerView>(R.id.oil_type_container)
        val oilTypeSnapHelper = LinearSnapHelper()
        with(oilTypeRecyclerView) {
            val oilTypeAdapter =
                // TODO - make these enums
                OilTypeRecyclerViewAdapter(
                    listOf(
                        "Conventional", "Blend", "Synthetic", "High Mileage"
                    ), requireActivity()
                )

            this.adapter = oilTypeAdapter
            this.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            oilTypeSnapHelper.attachToRecyclerView(this)
            this.onFlingListener = oilTypeSnapHelper
            this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            val snapView = oilTypeSnapHelper.findSnapView(layoutManager)
                            if (snapView == null) {
                                return
                            }
                            val snapPosition = layoutManager!!.getPosition(snapView)
                            val oldPosition = oilTypeAdapter.selectedPosition
                            oilTypeAdapter.selectedPosition = snapPosition
                            oilTypeAdapter.notifyItemChanged(snapPosition)
                            oilTypeAdapter.notifyItemChanged(oldPosition)
                        }
                    }
                })
        }
        oilTypeRecyclerView.smoothScrollToPosition(2)
//        runAfterDraw(oilTypeRecyclerView, Runnable {
//        })

        return view
    }

    private fun runAfterDraw(view: View, runnable: Runnable) {
        val drawListener: ViewTreeObserver.OnGlobalLayoutListener =
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    runnable.run()
                }
            }
        view.viewTreeObserver.addOnGlobalLayoutListener(drawListener)
    }
}