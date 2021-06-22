package com.carswaddle.carswaddlemechanic.ui.calendar.calendarfragment

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.core.content.ContextCompat
import com.carswaddle.carswaddleandroid.Extensions.isWithinDaysOfToday
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.carswaddlemechanic.application.CarSwaddleMechanicApp.Companion.applicationContext
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.WeekView

class SimpleWeekView(context: Context?) : WeekView(context) {

    var disabledTextPaint = Paint()

    val TEXT_SIZE = 14

    private var mRadius = 0
    override fun onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2
        mSchemePaint.style = Paint.Style.STROKE

        disabledTextPaint.isAntiAlias = true
        disabledTextPaint.style = Paint.Style.FILL
        disabledTextPaint.textAlign = Paint.Align.CENTER
        disabledTextPaint.color = Color.LTGRAY
        disabledTextPaint.isFakeBoldText = true
        disabledTextPaint.textSize = dipToPx(context, TEXT_SIZE.toFloat()).toFloat()
        
        mCurDayTextPaint.color = ContextCompat.getColor(applicationContext, R.color.brand2)
    }

    // From CalendarUtil.dipToPx
    fun dipToPx(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    override fun onDrawSelected(
        canvas: Canvas,
        calendar: Calendar,
        x: Int,
        hasScheme: Boolean
    ): Boolean {
        val cx = x + mItemWidth / 2
        val cy = mItemHeight / 2
        canvas.drawCircle(cx.toFloat(), cy.toFloat(), mRadius.toFloat(), mSelectedPaint)
        return false
    }

    override fun onDrawScheme(
        canvas: Canvas,
        calendar: Calendar,
        x: Int
    ) {
        val cx = x + mItemWidth / 2
        val cy = mItemHeight / 2
        canvas.drawCircle(cx.toFloat(), cy.toFloat(), mRadius.toFloat(), mSchemePaint)
    }

    override fun onDrawText(
        canvas: Canvas,
        calendar: Calendar,
        x: Int,
        hasScheme: Boolean,
        isSelected: Boolean
    ) {
        val baselineY = mTextBaseLine
        val cx = x + mItemWidth / 2
        if (isSelected) {
            canvas.drawText(
                calendar.day.toString(),
                cx.toFloat(),
                baselineY,
                mSelectTextPaint
            )
        } else if (hasScheme) {
            canvas.drawText(
                calendar.day.toString(),
                cx.toFloat(),
                baselineY,
                if (calendar.isCurrentDay) mCurDayTextPaint else if (calendar.isCurrentMonth) mSchemeTextPaint else mSchemeTextPaint
            )
        } else {
            canvas.drawText(
                calendar.day.toString(), cx.toFloat(), baselineY,
                if (calendar.isCurrentDay) mCurDayTextPaint else if (calendar.isCurrentMonth) mCurMonthTextPaint else mCurMonthTextPaint
            )
        }
    }

}

