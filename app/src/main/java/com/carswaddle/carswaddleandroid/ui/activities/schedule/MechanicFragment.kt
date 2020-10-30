package com.carswaddle.carswaddleandroid.ui.activities.schedule

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.activities.ui.home.AutoServicesListViewModel
import com.carswaddle.carswaddleandroid.data.mechanic.Mechanic
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicListElements
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoServiceLocation
import com.carswaddle.carswaddleandroid.services.serviceModels.Point
import com.carswaddle.carswaddleandroid.ui.activities.autoservicelist.AutoServiceListElements
import androidx.lifecycle.Observer
import com.carswaddle.carswaddleandroid.Extensions.addDays
import com.carswaddle.carswaddleandroid.Extensions.isWithinDaysOfToday
import com.carswaddle.carswaddleandroid.Extensions.today
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import kotlinx.coroutines.coroutineScope
import java.text.DateFormatSymbols
import java.util.*


class MechanicFragment(val point: Point) : Fragment() {

    private var itemWidth: Int = 0
    private var times: List<String> = listOf("10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM")
    private var spanCount = 4
    private var rawSpanCount = 12
    private lateinit var monthYearTextView: TextView

    private lateinit var mechanicViewModel: SelectMechanicViewModel
    
    private lateinit var mechanicViewAdapter: MyMechanicRecyclerViewAdapter

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
        with (recyclerView) {
            this.adapter = mechanicViewAdapter
            this.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            PagerSnapHelper().attachToRecyclerView(this)
        }
        runJustBeforeBeingDrawn(recyclerView, Runnable {
            if (itemWidth > 0 || recyclerView.layoutManager!!.itemCount < 1) {
                return@Runnable
            }
            itemWidth = recyclerView.layoutManager!!.findViewByPosition(0)!!.width
            recyclerView.layoutManager = MechanicLinearLayoutManager(context, requireActivity().window.decorView.width, itemWidth)
        })

        monthYearTextView = view.findViewById<TextView>(R.id.month_year_text_view)
        updateMonthYear(java.util.Calendar.getInstance().get(java.util.Calendar.MONTH), java.util.Calendar.getInstance().get(java.util.Calendar.YEAR))
        val calendarView = view.findViewById<CalendarView>(R.id.calendar_view)

        // Never select today, always start tomorrow
        calendarView.clearSingleSelect()
        calendarView.putMultiSelect(Calendar().today().addDays(1))

        calendarView.setOnWeekChangeListener {  }
        calendarView.setOnCalendarInterceptListener(object: CalendarView.OnCalendarInterceptListener {
            override fun onCalendarIntercept(calendar: Calendar?): Boolean {
                Log.i("", "")
                return true
            }

            override fun onCalendarInterceptClick(calendar: Calendar?, isClick: Boolean) {
                Log.i("", "")
            }

        })
        calendarView.setOnCalendarSelectListener(object: CalendarView.OnCalendarSelectListener {
            override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
                if (calendar == null || !calendar.isWithinDaysOfToday(1, 7)) {
                    // Reset if not a valid selection
                    calendarView.clearSingleSelect()
                    return
                }
                updateMonthYear(calendar.month - 1, calendar.year)
            }

            override fun onCalendarOutOfRange(calendar: Calendar?) {
                TODO("Not yet implemented")
            }

        })

        val timeRecycler = view.findViewById<RecyclerView>(R.id.time_slot)
        with (timeRecycler) {
            this.adapter = TimePickerRecyclerViewAdapter(
                times
            )
            val gridLayoutManager = GridLayoutManager(context, spanCount)
//            gridLayoutManager.spanSizeLookup = MySizeLookup(rawSpanCount, spanCount, times.size)
            this.layoutManager = gridLayoutManager
//            this.addItemDecoration(EqualSpacingItemDecoration(16))
        }


        mechanicViewModel.mechanics.observe(viewLifecycleOwner, Observer<List<MechanicListElements>> { mechanicElements ->
            this.mechanicViewAdapter.mechanicElements = mechanicElements
            val firstMechanicId = mechanicElements.first()?.mechanic.id
            if (firstMechanicId != null) {
                mechanicViewModel.loadTimeSlots(firstMechanicId) {
                    activity?.runOnUiThread {
                        updateTimeSlots()
                    }
                    
                }
            }
        })
        
        return view
    }

    private fun updateTimeSlots() {
        val mechanicId = mechanicViewModel.mechanics.value?.first()?.mechanic?.id 
        if (mechanicId == null) {
            return
        }
        val slots = mechanicViewModel.timeSlots(mechanicId, 0)
        Log.w("slots", "slots: $slots")
    }

    private fun updateMonthYear(month: Int, year: Int) {
        val monthStr = DateFormatSymbols(Locale.getDefault()).months[month];
        monthYearTextView.text = monthStr + " " + year
    }

    class MySizeLookup(val rawSpanCount: Int, val spanCount: Int, val itemCount: Int) : SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            if (itemCount == 0 || spanCount == 0 || rawSpanCount == 0) {
                // Invalid, default span
                return rawSpanCount / spanCount
            }
            val excessItems = itemCount % spanCount
            val lastRowIndexStart = if (excessItems == 0) itemCount - spanCount else itemCount - excessItems
            val itemsInLastRow = itemCount - lastRowIndexStart
            if (position < lastRowIndexStart) {
                // Take up full row (3 span sizes each so 4 across)
                return rawSpanCount / spanCount
            } else {
                return rawSpanCount / spanCount
//                return rawSpanCount / itemsInLastRow
            }
        }
    }

    class EqualSpacingItemDecoration constructor(
        private val spacing: Int,
        private var displayMode: Int = -1
    ) :
        ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildViewHolder(view).adapterPosition
            val itemCount = state.itemCount
            val layoutManager = parent.layoutManager
            setSpacingForDirection(outRect, layoutManager, position, itemCount)
        }

        private fun setSpacingForDirection(
            outRect: Rect,
            layoutManager: RecyclerView.LayoutManager?,
            position: Int,
            itemCount: Int
        ) {

            // Resolve display mode automatically
            if (displayMode == -1) {
                displayMode = resolveDisplayMode(layoutManager)
            }
            when (displayMode) {
                HORIZONTAL -> {
                    outRect.left = spacing
                    outRect.right = if (position == itemCount - 1) spacing else 0
                    outRect.top = spacing
                    outRect.bottom = spacing
                }
                VERTICAL -> {
                    outRect.left = spacing
                    outRect.right = spacing
                    outRect.top = spacing
                    outRect.bottom = if (position == itemCount - 1) spacing else 0
                }
                GRID -> if (layoutManager is GridLayoutManager) {
                    val cols = layoutManager.spanCount
                    val rows = itemCount / cols
                    outRect.left = spacing
                    outRect.right = if (position % cols == cols - 1) spacing else 0
                    outRect.top = spacing
                    outRect.bottom = if (position / cols == rows - 1) spacing else 0
                }
            }
        }

        private fun resolveDisplayMode(layoutManager: RecyclerView.LayoutManager?): Int {
            if (layoutManager is GridLayoutManager) return GRID
            return if (layoutManager!!.canScrollHorizontally()) HORIZONTAL else VERTICAL
        }

        companion object {
            const val HORIZONTAL = 0
            const val VERTICAL = 1
            const val GRID = 2
        }

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