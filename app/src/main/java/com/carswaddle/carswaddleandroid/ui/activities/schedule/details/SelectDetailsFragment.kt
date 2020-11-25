package com.carswaddle.carswaddleandroid.ui.activities.schedule.details

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.Extensions.safeFirst
import com.carswaddle.carswaddleandroid.Extensions.toCalendar
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.vehicle.Vehicle
import com.carswaddle.carswaddleandroid.services.CouponErrorType
import com.carswaddle.carswaddleandroid.services.serviceModels.*
import com.carswaddle.carswaddleandroid.ui.view.ProgressButton
import com.google.android.material.button.MaterialButton
import com.stripe.android.PaymentSession
import com.stripe.android.PaymentSessionConfig
import com.stripe.android.PaymentSessionData
import com.stripe.android.model.PaymentMethod
import java.util.*

class SelectDetailsFragment(val point: Point, val mechanicId: String, val scheduledDate: Date) : Fragment() {

    var listener: OnPriceUpdatedListener? = null

    private lateinit var selectDetailsViewModel: SelectDetailsViewModel
    private lateinit var oilTypeRecyclerView: RecyclerView

    private val vehicleAdapter: VehicleRecyclerViewAdapter = VehicleRecyclerViewAdapter()

    private var newVehicleId: String? = null
    private var hasScrolledToFirstVehicleIndex: Boolean = false

    private var coupon: String? = null
    
    private var selectedVehicleId: String? = null

    private lateinit var couponEditText: EditText

    private lateinit var redeemButton: Button

    private lateinit var couponStatusTextView: TextView

    private lateinit var paymentMethodImageView: ImageView
    private lateinit var paymentMethodTextView: TextView
    private lateinit var paymentLayout: LinearLayout
    private lateinit var paymentSession: PaymentSession
    private lateinit var payButton: ProgressButton

    private var paymentSourceId: String? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_select_details, container, false)

        selectDetailsViewModel = ViewModelProviders.of(this).get(SelectDetailsViewModel::class.java)

        couponEditText = view.findViewById(R.id.couponEditText)
        couponStatusTextView = view.findViewById(R.id.couponStatusTextView)
        paymentMethodImageView = view.findViewById(R.id.paymentMethodImageView)
        paymentMethodTextView = view.findViewById(R.id.paymentMethodTextView)
        paymentMethodTextView = view.findViewById(R.id.paymentMethodTextView)
        paymentLayout = view.findViewById(R.id.paymentLayout)
        payButton = view.findViewById(R.id.payButton)
        
        payButton.button.setOnClickListener { 
            payForService()
        }

        paymentSession = PaymentSession(
            this,
            paymentSessionConfig()
        )

        paymentLayout.setOnClickListener {
            paymentSession.presentPaymentMethodSelection()
        }

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
                    couponStatusTextView.setLayoutParams(
                        LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                    )
                } else if (couponEditText.text.isEmpty() == false && errorType == null) {
                    couponStatusTextView.visibility = View.VISIBLE
                    couponStatusTextView.text = getString(R.string.coupon_code_success)
                    val c = context
                    if (c != null) {
                        couponStatusTextView.setTextColor(
                            ContextCompat.getColor(
                                c,
                                R.color.success
                            )
                        )
                    }
                    couponStatusTextView.setLayoutParams(
                        LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                    )
                } else {
                    couponStatusTextView.visibility = View.INVISIBLE
                    couponStatusTextView.setLayoutParams(
                        LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0
                        )
                    )
                }
            }
        )

        selectDetailsViewModel.price.observe(
            viewLifecycleOwner,
            Observer { price ->
                val p = selectDetailsViewModel.price.value
                if (p == null) {
                    // TODO - error handling
                    return@Observer
                }
                listener?.onPriceUpdated(p)
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
                    selectedVehicleId = vehicles.safeFirst()?.id
                    if (id != null) {
                        val newVehicleIndex = vehicles.indexOfFirst {
                            it.id == id
                        }
                        vehicleRecyclerView.smoothScrollToPosition(newVehicleIndex + 1)
                    } else if (hasScrolledToFirstVehicleIndex == false) {
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
                OilTypeRecyclerViewAdapter(requireActivity())

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
                            if (snapView == null || layoutManager == null) {
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

        setupPaymentSession()
        
        updatePrice()

        return view
    }

    private fun paymentSessionConfig(): PaymentSessionConfig {
        return PaymentSessionConfig.Builder()
            .setShippingMethodsRequired(false)
            .setShippingInfoRequired(false)
            .setPaymentMethodTypes(
                listOf(PaymentMethod.Type.Card)
            )
            .setShouldShowGooglePay(false)
            .build()
    }

    private fun setupPaymentSession() {
        paymentSession.init(
            object : PaymentSession.PaymentSessionListener {
                override fun onCommunicatingStateChanged(isCommunicating: Boolean) {
                    // update UI, such as hiding or showing a progress bar
                    Log.w("pay", "isCommunicating")
                }

                override fun onError(errorCode: Int, errorMessage: String) {
                    // handle error
                    Log.w("pay", "error" + errorMessage)
                }

                override fun onPaymentSessionDataChanged(data: PaymentSessionData) {
                    paymentMethodImageView.setImageDrawable(paymentMethodImage(data))
                    paymentMethodTextView.text = paymentMethodText(data)
                    paymentSourceId = data.paymentMethod?.id
                }
            }
        )
    }
    
    private fun paymentMethodImage(data: PaymentSessionData): Drawable? {
        val paymentMethod = data.paymentMethod
        if (paymentMethod == null) {
            return null
        }
        val c = context
        val cardResource = paymentMethod.card?.brand?.icon
        if (c != null && cardResource != null) {
            val icon = ContextCompat.getDrawable(c, cardResource)
            return icon
        } else {
            return null
        }
    }
    
    private fun paymentMethodText(data: PaymentSessionData): String {
        val paymentMethod = data.paymentMethod
        if (paymentMethod == null) {
            return getString(R.string.select_payment_method)
        }
        
        if (data.useGooglePay) {
            // TODO: 11/20/20 Not currently supported. Support it! 
            return getString(R.string.google_play) 
        } else {
            val card = paymentMethod.card ?: return getString(R.string.default_card_copy)
            return card.brand.displayName + " " + card.last4
        }
    }
    
    private fun payForService() {
        val vehicleId = selectedVehicleId
        val sourceID = paymentSourceId
        if (vehicleId == null || sourceID == null) {
            return
        }
        
        val loc = ServerLocation(point.longitude(), point.latitude(), point.streetAddress)
        val serviceEntities = listOf(CreateServiceEntity.init(OilType.SYNTHETIC))
        val createAutoService = CreateAutoService(
            AutoServiceStatus.scheduled,
            null,
            vehicleId,
            mechanicId,
            scheduledDate,
            null,
            loc,
            serviceEntities,
            sourceID
        )
        
        payButton.isLoading = true
        
        selectDetailsViewModel.createAndPayForAutoService(createAutoService) { error ->
            Log.w("autoservice", "Created new auto service")
            if (error == null) {
                activity?.runOnUiThread {
                    payButton.isLoading = false
                    showAlertToAddToCalendar(scheduledDate)
                }
            }
        }
    }
    
    private fun showAlertToAddToCalendar(scheduledDate: Date) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.add_event_calendar_title))
        builder.setMessage(getString(R.string.add_event_calendar_message))
        builder.setPositiveButton(getString(R.string.add_event_calendar)) { dialog, which ->
            openIntentForCalendarEvent(scheduledDate)
            activity?.finish()
        }
        builder.setNegativeButton(getString(R.string.dismiss_dialog)) { dialog, which ->
            activity?.finish()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    
    private fun openIntentForCalendarEvent(scheduledDate: Date) {
        val cal = scheduledDate.toCalendar()
        if (cal == null) {
            return 
        }
        val intent = Intent(Intent.ACTION_EDIT)
        intent.setData(CalendarContract.Events.CONTENT_URI)
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.timeInMillis)
        intent.putExtra(CalendarContract.CalendarAlerts.ALARM_TIME, cal.timeInMillis - 30 * 60 * 1000)
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal.timeInMillis + 45 * 60 * 1000)
        intent.putExtra(CalendarContract.Events.TITLE, getString(R.string.oil_change_event_title))
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            paymentSession.handlePaymentData(requestCode, resultCode, data)
        }
    }

    private fun updatePrice() {
        selectDetailsViewModel.loadPrice(
            point.latitude(),
            point.longitude(),
            mechanicId,
            OilType.SYNTHETIC,
            coupon
        )
    }

    interface OnPriceUpdatedListener {
        fun onPriceUpdated(price: Price)
    }

}