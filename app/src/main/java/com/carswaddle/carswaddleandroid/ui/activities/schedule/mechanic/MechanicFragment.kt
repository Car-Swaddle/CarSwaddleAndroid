package com.carswaddle.carswaddleandroid.ui.activities.schedule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.*
import com.carswaddle.carswaddleandroid.Extensions.*
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicListElements
import com.carswaddle.carswaddleandroid.services.serviceModels.Point
import com.carswaddle.carswaddleandroid.stripe.StripeKeyProvider
import com.carswaddle.carswaddleandroid.ui.activities.schedule.mechanic.MechanicEmptyStateView
import com.carswaddle.carswaddleandroid.ui.activities.schedule.mechanic.TimeSlotsEmptyStateView
import com.carswaddle.carswaddleandroid.ui.view.ProgressButton
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.stripe.android.CustomerSession
import java.text.DateFormatSymbols
import java.util.*
import java.util.Calendar.*
import java.util.Calendar as KotlinCalendar


class MechanicFragment() : Fragment() {

    private var spanCount = 4

    private lateinit var monthYearTextView: TextView
    private lateinit var callback: OnConfirmListener
    
    private val mechanicLinearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

    lateinit var point: Point

    fun setOnConfirmCallbackListener(callback: OnConfirmListener) {
        this.callback = callback
    }

    private lateinit var mechanicViewModel: SelectMechanicViewModel
    private lateinit var mechanicViewAdapter: MyMechanicRecyclerViewAdapter
    private lateinit var timeSlotViewAdapter: TimePickerRecyclerViewAdapter
    private lateinit var calendarView: CalendarView
    private lateinit var mechanicEmptyView: MechanicEmptyStateView
//    private var selectedTimeSlot: TemplateTimeSpan? = null
    
   private var currentTimeSlotCount: Int? = null 
    set(newValue) {
        field = newValue
        updateTimeSlotDisplayState()
    }
    
    private lateinit var mechanicRecycler: RecyclerView
    
    private lateinit var emptySlotsView: TimeSlotsEmptyStateView
    private var hasFetchedTimeSlots: Boolean = false
        set(newValue) {
            field = newValue
            updateTimeSlotDisplayState()
        }
    
    private lateinit var timeRecycler: RecyclerView
    
    private lateinit var confirmButton: ProgressButton
    
    private var selectedDate: Date? = null
    set(newValue) {
        field = newValue
        confirmButton.isButtonEnabled = selectedDate != null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mechanic, container, false)

        mechanicViewModel = ViewModelProviders.of(this).get(SelectMechanicViewModel::class.java)

        mechanicViewModel.point = point

        mechanicViewAdapter = MyMechanicRecyclerViewAdapter()

        mechanicRecycler = view.findViewById<RecyclerView>(R.id.mechanic_list_container)
        with(mechanicRecycler) {
            this.adapter = mechanicViewAdapter
            this.layoutManager = mechanicLinearLayoutManager
            val snapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(mechanicRecycler)
            this.onFlingListener = snapHelper
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    mechanicRecycler.updateTransformForScrollOffset(mechanicViewAdapter)
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                        return
                    }
                    val snapView = snapHelper.findSnapView(layoutManager)
                    val lm = layoutManager
                    if (snapView == null || lm == null) {
                        return
                    }
                    val snapPosition = lm.getPosition(snapView)
                    mechanicViewAdapter.selectedPosition = snapPosition

                    val mechanicId = mechanicViewAdapter.selectedMechanicId
                    if (mechanicId != null) {
                        mechanicViewModel.loadTimeSlots(mechanicId) { e ->
                            activity?.runOnUiThread {
                                // If error or changed during fetch, don't update
                                if (e == null && mechanicId == mechanicViewAdapter.selectedMechanicId) {
                                    updateTimeSlotsToTomorrow()
                                }
                            }
                        }
                    }

                    // Loop through all and mark as not selected. oldPosition wasn't always accurate
                    for (i in 0..mechanicViewAdapter.itemCount) {
                        val childView = getChildAt(i) ?: continue
                        val vh = findContainingViewHolder(childView) as? MyMechanicRecyclerViewAdapter.ViewHolder
                        vh?.isSelected = false
                    }

                    // Hack because the index and/or transform on the view are jacked if using `notifyDatasetChanged`
                    val o = findViewHolderForAdapterPosition(snapPosition) as? MyMechanicRecyclerViewAdapter.ViewHolder
                    o?.isSelected = true
                }
            })
        }
        
        emptySlotsView = view.findViewById(R.id.time_slot_empty_state)
        mechanicEmptyView = view.findViewById(R.id.mechanicEmptyView)
        mechanicEmptyView.visibility = View.GONE

        monthYearTextView = view.findViewById(R.id.month_year_text_view)
        updateMonthYear(getInstance().get(MONTH), getInstance().get(YEAR))
        calendarView = view.findViewById(R.id.calendar_view)

        val minCalendar = Calendar().today().addDays(1)
        val maxCalendar = Calendar().today().addDays(7)
        calendarView.setRange(
            minCalendar.year,
            minCalendar.month,
            minCalendar.day,
            maxCalendar.year,
            maxCalendar.month,
            maxCalendar.day
        )
        calendarView.scrollToCalendar(minCalendar.year, minCalendar.month, minCalendar.day)

        confirmButton = view.findViewById(R.id.confirm_mechanic)
        confirmButton.button.setOnClickListener { v ->
            val m = mechanicViewAdapter.selectedMechanicId
            val date = selectedDate
            if (m != null && date != null) {
                callback.onConfirm(m, date)
            }
        }

        confirmButton.isButtonEnabled = false
        
        calendarView.setOnCalendarSelectListener(object : CalendarView.OnCalendarSelectListener {
            override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
                if (calendar == null) {
                    return
                }
                updateTimeSlots(calendar.toJavaCalendar())
                selectedDate = null
            }

            override fun onCalendarOutOfRange(calendar: Calendar?) {
                return
            }
        }) 

        timeSlotViewAdapter = TimePickerRecyclerViewAdapter()
        timeRecycler = view.findViewById<RecyclerView>(R.id.time_slot)
        timeRecycler.adapter = timeSlotViewAdapter
        timeRecycler.setLayoutManager(object: GridLayoutManager(context, spanCount) {
            // override methods here
        })
        
        mechanicViewModel.mechanics.observe(
            viewLifecycleOwner,
            Observer<List<MechanicListElements>> { mechanicElements ->
                this.mechanicViewAdapter.mechanicElements = mechanicElements
                val firstMechanicId = mechanicElements.safeFirst()?.mechanic?.id
                activity?.runOnUiThread {
                    mechanicRecycler.adapter?.notifyDataSetChanged()
                    // 0 is padding, 1 will be position of first item (always at least two with padding items)
                    mechanicRecycler.smoothScrollToPosition(1)
                    selectedDate = null
                }
                if (firstMechanicId != null) {
                    currentTimeSlotCount = null
                    mechanicViewModel.loadTimeSlots(firstMechanicId) { e ->
                        activity?.runOnUiThread {
                            if (e == null) {
                                updateTimeSlotsToTomorrow()
                            }
                        }
                    }
                }

                updateMechanciViewDisplayState(mechanicElements.count())
            })

        setEphemeralKey()

        return view
    }

    private fun setEphemeralKey() {
        val c = context
        if (c == null) {
            return
        }
        CustomerSession.initCustomerSession(c, StripeKeyProvider(c))
    }

    private fun updateTimeSlotsToTomorrow() {
        val calendar = getInstance()
        calendar.add(DAY_OF_YEAR, 1)
        updateTimeSlots(calendar)
    }

    private fun updateTimeSlots(calendar: java.util.Calendar) {
        val mechanicId = mechanicViewAdapter.selectedMechanicId
        updateMonthYear(calendar.get(MONTH), calendar.get(YEAR))
        if (mechanicId == null) {
            return
        }
        selectedDate = null
        val slots = mechanicViewModel.timeSlots(mechanicId, calendar)
        currentTimeSlotCount = slots.count()
        timeSlotViewAdapter.timeSlots = slots
        updateTimeSlotDisplayState()
        timeSlotViewAdapter.didChangeSelectedTimeSlot = { newTimeSlot ->
            activity?.runOnUiThread {
                if (newTimeSlot != null) {
                    val selectedCal = getInstance()
                    val localTime = newTimeSlot.localTime
                    selectedCal.set(
                        calendar.get(YEAR),
                        calendar.get(MONTH),
                        calendar.get(DATE),
                        localTime.hour,
                        localTime.minute,
                        localTime.second
                    )

                    this.selectedDate = selectedCal.time
                } else {
                    // clear
                }
            }
        }

        Log.w("slots", "slots: $slots")
    }
    
    private fun updateTimeSlotDisplayState() {
        if (currentTimeSlotCount == 0) {
            emptySlotsView.visibility = View.VISIBLE
            timeRecycler.visibility = View.GONE
        } else {
            emptySlotsView.visibility = View.GONE
            timeRecycler.visibility = View.VISIBLE
        }
    }

    private fun updateMechanciViewDisplayState(mechanicCount: Int) {
        if (mechanicCount == 0) {
            mechanicEmptyView.visibility = View.VISIBLE
            mechanicRecycler.visibility = View.GONE
        } else {
            mechanicEmptyView.visibility = View.GONE
            mechanicRecycler.visibility = View.VISIBLE
        }
    }

    private fun updateMonthYear(month: Int, year: Int) {
        val monthStr = DateFormatSymbols(Locale.getDefault()).months[month];
        monthYearTextView.text = monthStr + " " + year
    }

    interface OnConfirmListener {
        fun onConfirm(mechanicId: String, date: Date)
    }

}