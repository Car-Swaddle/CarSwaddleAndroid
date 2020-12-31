package com.carswaddle.carswaddlemechanic.ui.calendar.calendarfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager2.widget.ViewPager2
import com.carswaddle.carswaddleandroid.Extensions.toJavaCalendar
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.carswaddlemechanic.ui.calendar.singleday.DayAutoServiceListViewModel
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarLayout
import com.haibin.calendarview.CalendarView
import java.text.DateFormatSymbols
import java.util.*
import java.util.Calendar.*


class CalendarFragment : Fragment() {

    private lateinit var viewModel: DayAutoServiceListViewModel
    
    private lateinit var viewPager: ViewPager2
    private lateinit var calendarLayout: CalendarLayout
    private lateinit var calendarView: CalendarView
    private lateinit var monthTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(DayAutoServiceListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_calendar, container, false)
        
        viewPager = root.findViewById(R.id.calendarViewPager)
        calendarLayout = root.findViewById(R.id.calendar_layout)
        calendarView = root.findViewById(R.id.calendar_view)
        
        monthTextView = root.findViewById(R.id.month_text_view)

        calendarView.setOnCalendarSelectListener(object : CalendarView.OnCalendarSelectListener {
            override fun onCalendarOutOfRange(calendar: Calendar?) {}
            override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
                if (calendar == null) {
                    return
                }
                val c = calendar.toJavaCalendar()
                updateMonthView(c)
            }
        })

        return root
    }

    private fun updateMonthView(date: java.util.Calendar) {
        val month = date.get(MONTH)
        val year = date.get(YEAR)
        val monthStr = DateFormatSymbols(Locale.getDefault()).months[month]
        monthTextView.text = "$monthStr $year"
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewPager.adapter = DayPagerAdapter(this)
        viewPager.setCurrentItem(Int.MAX_VALUE / 2, false)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                print("page changed")

                val c = DayPagerAdapter.positionToCalendar(position)
                val year = c.get(java.util.Calendar.YEAR)
                val month = c.get(java.util.Calendar.MONTH)+1
                val day = c.get(java.util.Calendar.DAY_OF_MONTH)
                calendarView.scrollToCalendar(year, month, day, true, false)

                updateMonthView(c)
                
                super.onPageSelected(position)
            }
        })

        updateMonthView(getInstance())
    }

}