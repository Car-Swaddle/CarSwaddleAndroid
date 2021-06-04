package com.carswaddle.carswaddlemechanic.ui.profile.personalInfo.details

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.carswaddle.carswaddleandroid.Extensions.toCalendar
import com.carswaddle.carswaddleandroid.ui.view.ProgressButton
import com.carswaddle.carswaddlemechanic.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*


class DateOfBirthFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var viewModel: DateOfBirthViewModel

    private lateinit var dateOfBirthTextView: TextView
    private lateinit var selectDateOfBirthView: RelativeLayout

    private lateinit var saveButton: ProgressButton

    private var selectedDate: Calendar? = null
        set(newValue) {
            field = newValue
            updateDisplayedDate()
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(DateOfBirthViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_date_of_birth, container, false)

        dateOfBirthTextView = root.findViewById(R.id.dateOfBirthTextView)
        selectDateOfBirthView = root.findViewById(R.id.selectDateOfBirthView)

        selectDateOfBirthView.setOnClickListener {
            val c = selectedDate ?: Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(requireContext(), this, year, month, day)
            dpd.show()
        }
        
        saveButton = root.findViewById(R.id.saveDateOfBirthButton)
        
        saveButton.button.setOnClickListener {
            saveDateOfBirth()
        }
        
        viewModel.dateOfBirth.observe(viewLifecycleOwner) {
            selectedDate = it?.toCalendar()
        }
        
        updateDisplayedDate()
        
        return root
    }

    private fun updateDisplayedDate() {
        val d = selectedDate
        if (d == null) {
            dateOfBirthTextView.text = resources.getString(R.string.blank)
        } else {
            val localDateTime = LocalDateTime.ofInstant(
                d.toInstant(),
                d.timeZone.toZoneId()
            )
            this.dateOfBirthTextView.text = dateTimeFormatter.format(localDateTime)
        }
    }

    private fun saveDateOfBirth() {
        val newDOB = selectedDate
        if (newDOB == null) {
            return
        }
        saveButton.isLoading = true
        viewModel.updateDateOfBirth(newDOB.time, requireContext()) {
            if (it == null) {
                requireActivity().runOnUiThread {
                    finishSuccessfully()
                }
            }
        }
    }

    private fun finishSuccessfully() {
        saveButton.isLoading = false
        findNavController().popBackStack()
        Toast.makeText(
            requireContext(),
            "Car Swaddle successfully saved your date of birth.",
            Toast.LENGTH_SHORT
        ).show()
    }
    
    override fun onDateSet(picker: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val p = picker
        if (p != null) {
            selectedDate = this.getDateFromDatePicker(p)?.toCalendar()
            updateDisplayedDate()
        }
    }

    private fun getDateFromDatePicker(datePicker: DatePicker): Date? {
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year
        val calendar = Calendar.getInstance()
        calendar[year, month] = day
        return calendar.time
    }

    companion object {
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("MMMM d, yyyy")
            .toFormatter(Locale.US)
    }

}