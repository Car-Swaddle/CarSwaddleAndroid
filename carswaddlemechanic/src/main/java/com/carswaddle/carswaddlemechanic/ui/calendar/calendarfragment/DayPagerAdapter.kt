package com.carswaddle.carswaddlemechanic.ui.calendar.calendarfragment

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.carswaddle.carswaddlemechanic.ui.calendar.singleday.DayAutoServiceListFragment
import java.util.*


class DayPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        return DayAutoServiceListFragment(date(position))
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    private fun date(position: Int): Calendar {
        val difference = position - todayItemIndex
        val newDate = Calendar.getInstance()
        newDate.add(Calendar.DATE, difference)
        return newDate
    }
    
    companion object {
        fun positionToCalendar(position: Int): Calendar {
            val newDay = Calendar.getInstance()
            val difference = position - todayItemIndex 
            newDay.add(Calendar.DATE, difference)
            return newDay
        }

        fun calendarToPosition(calendar: Calendar): Int {
            var different: Long = calendar.time.time - positionToCalendar(todayItemIndex).time.time
            
            val secondsInMilli: Long = 1000
            val minutesInMilli = secondsInMilli * 60
            val hoursInMilli = minutesInMilli * 60
            val daysInMilli = hoursInMilli * 24 
            
            val days = different / daysInMilli
            var new = days.toInt() + todayItemIndex

            if (different > 0) {
                new += 1
            }
            return new
        }

        private val todayItemIndex: Int = Int.MAX_VALUE / 2
    }

}