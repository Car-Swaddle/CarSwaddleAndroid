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
import com.carswaddle.carswaddleandroid.Extensions.addDays
import com.carswaddle.carswaddleandroid.Extensions.safeFirst
import com.carswaddle.carswaddleandroid.Extensions.toJavaCalendar
import com.carswaddle.carswaddleandroid.Extensions.today
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.stripe.StripeKeyProvider
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicListElements
import com.carswaddle.carswaddleandroid.data.mechanic.TemplateTimeSpan
import com.carswaddle.carswaddleandroid.services.serviceModels.Point
import com.google.android.material.button.MaterialButton
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.stripe.android.CustomerSession
import java.text.DateFormatSymbols
import java.util.*
import java.util.Calendar.*
import kotlin.jvm.internal.Intrinsics
import java.util.Calendar as KotlinCalendar


class MechanicFragment(val point: Point) : Fragment() {

    private var spanCount = 4

    private lateinit var monthYearTextView: TextView
    private lateinit var callback: OnConfirmListener

    fun setOnConfirmCallbackListener(callback: OnConfirmListener) {
        this.callback = callback
    }

    private lateinit var mechanicViewModel: SelectMechanicViewModel
    private lateinit var mechanicViewAdapter: MyMechanicRecyclerViewAdapter
    private lateinit var timeSlotViewAdapter: TimePickerRecyclerViewAdapter
    private lateinit var calendarView: CalendarView
    private var selectedMechanicId: String? = null
    private var selectedTimeSlot: TemplateTimeSpan? = null
    
    private var selectedDate: Date? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mechanic, container, false)

        mechanicViewModel = ViewModelProviders.of(this).get(SelectMechanicViewModel::class.java)

        mechanicViewModel.point = point

        mechanicViewAdapter = MyMechanicRecyclerViewAdapter()

        val recyclerView = view.findViewById<RecyclerView>(R.id.mechanic_list_container)
        with(recyclerView) {
            this.adapter = mechanicViewAdapter
            this.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            val snapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(recyclerView)
            this.onFlingListener = snapHelper
        }

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

        val confirmButton = view.findViewById<MaterialButton>(R.id.confirm_mechanic)
        confirmButton.setOnClickListener { v ->
            val m = selectedMechanicId
            val t = selectedTimeSlot
            val date = selectedDate
            if (m != null && date != null) {
                callback.onConfirm(m, date)
            }
        }

        calendarView.setOnCalendarSelectListener(object : CalendarView.OnCalendarSelectListener {
            override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
                if (calendar == null) {
                    return
                }
                updateTimeSlots(calendar.toJavaCalendar())
            }

            override fun onCalendarOutOfRange(calendar: Calendar?) {
                return
            }
        })

        timeSlotViewAdapter = TimePickerRecyclerViewAdapter()
        val timeRecycler = view.findViewById<RecyclerView>(R.id.time_slot)
        with(timeRecycler) {
            this.adapter = timeSlotViewAdapter
            val gridLayoutManager = GridLayoutManager(context, spanCount)
            this.layoutManager = gridLayoutManager
        }

        mechanicViewModel.mechanics.observe(
            viewLifecycleOwner,
            Observer<List<MechanicListElements>> { mechanicElements ->
                this.mechanicViewAdapter.mechanicElements =
                    mechanicElements
                val firstMechanicId = mechanicElements.safeFirst()?.mechanic?.id
                activity?.runOnUiThread {
                    recyclerView.adapter?.notifyDataSetChanged()
                    // 0 is padding, 1 will be position of first item (always at least two with padding items)
                    recyclerView.smoothScrollToPosition(1)
                }
                if (firstMechanicId != null) {
                    mechanicViewModel.loadTimeSlots(firstMechanicId) {
                        activity?.runOnUiThread {
                            var tomorrow = KotlinCalendar.getInstance()
                            tomorrow.add(KotlinCalendar.DAY_OF_YEAR, 1)
                            updateTimeSlots(tomorrow)
                        }
                    }
                }

                if (selectedMechanicId == null) {
                    selectedMechanicId = firstMechanicId
                }
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

    private fun updateTimeSlots(calendar: java.util.Calendar) {
        val mechanicId = mechanicViewModel.mechanics.value?.safeFirst()?.mechanic?.id
        updateMonthYear(calendar.get(MONTH), calendar.get(YEAR))
        if (mechanicId == null) {
            return
        }
        val slots = mechanicViewModel.timeSlots(mechanicId, calendar)
        timeSlotViewAdapter.timeSlots = slots
        timeSlotViewAdapter.didChangeSelectedTimeSlot = { newTimeSlot ->
            if (newTimeSlot != null) {
                val selectedCal = getInstance()
                val timeCal = newTimeSlot.calendar
                selectedCal.set(calendar.get(YEAR), calendar.get(MONTH), calendar.get(DATE), timeCal.get(HOUR_OF_DAY), timeCal.get(MINUTE), timeCal.get(SECOND))
                
                this.selectedDate = selectedCal.time
            } else {
                // clear
            }
        }
        
        Log.w("slots", "slots: $slots")
    }

    private fun updateMonthYear(month: Int, year: Int) {
        val monthStr = DateFormatSymbols(Locale.getDefault()).months[month];
        monthYearTextView.text = monthStr + " " + year
    }

    interface OnConfirmListener {
        fun onConfirm(mechanicId: String, date: Date)
    }

}