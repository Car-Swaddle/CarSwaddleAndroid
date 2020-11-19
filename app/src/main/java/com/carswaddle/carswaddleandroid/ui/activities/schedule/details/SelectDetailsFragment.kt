package com.carswaddle.carswaddleandroid.ui.activities.schedule.details

import android.app.ActionBar
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.Extensions.onTextChanged
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.vehicle.Vehicle
import com.carswaddle.carswaddleandroid.services.ContentType
import com.carswaddle.carswaddleandroid.services.CouponErrorType
import com.carswaddle.carswaddleandroid.services.serviceModels.OilType
import com.carswaddle.carswaddleandroid.services.serviceModels.Point
import com.carswaddle.carswaddleandroid.services.serviceModels.Price

class SelectDetailsFragment(val point: Point, val mechanicId: String) : Fragment() {

    private var vehicleItemWidth: Int = 0

    private lateinit var selectDetailsViewModel: SelectDetailsViewModel
    private lateinit var oilTypeRecyclerView: RecyclerView

    private val vehicleAdapter: VehicleRecyclerViewAdapter = VehicleRecyclerViewAdapter()
    
    private var newVehicleId: String? = null
    private var hasScrolledToFirstVehicleIndex: Boolean = false
    
    private var coupon: String? = null
    
    private var price: Price? = null
    
    private lateinit var couponEditText: EditText

    private lateinit var redeemButton: Button

    private lateinit var couponStatusTextView: TextView
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_select_details, container, false)

        selectDetailsViewModel = ViewModelProviders.of(this).get(SelectDetailsViewModel::class.java)

        couponEditText = view.findViewById(R.id.couponEditText)
        couponStatusTextView = view.findViewById(R.id.couponStatusTextView)
        
        selectDetailsViewModel.couponError.observe(
            viewLifecycleOwner,
            Observer<CouponErrorType?> { errorType ->
                if (errorType != null) {
                    couponStatusTextView.visibility = View.VISIBLE
                    couponStatusTextView.text = context?.let { errorType.localizedString(it) }
                    val c = context
                    if (c != null) {
                        couponStatusTextView.setTextColor(ContextCompat.getColor(c, R.color.error))
                    }
                    couponStatusTextView.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT))
                } else if (couponEditText.text.isEmpty() == false && errorType == null) {
                    couponStatusTextView.visibility = View.VISIBLE
                    couponStatusTextView.text = getString(R.string.coupon_code_success)
                    val c = context
                    if (c != null) {
                        couponStatusTextView.setTextColor(ContextCompat.getColor(c, R.color.success))
                    }
                    couponStatusTextView.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT))
                } else {
                    couponStatusTextView.visibility = View.INVISIBLE
                    couponStatusTextView.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0))
                }
            }
        )
        
        couponEditText.addTextChangedListener {
            coupon = it.toString()
        }
        
        redeemButton = view.findViewById(R.id.redeemButton)
        redeemButton.setOnClickListener { 
            updatePrice()
        }
        
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

            oilTypeRecyclerView
                .addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
    
    private fun updatePrice() {
        selectDetailsViewModel.loadPrice(
            point.latitude(),
            point.longitude(),
            mechanicId,
            OilType.synthetic,
            coupon
        )
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