package com.carswaddle.carswaddlemechanic.ui.profile.availability

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.services.serviceModels.UpdateTemplateTimeSpan
import com.carswaddle.carswaddleandroid.ui.view.ProgressButton
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.ui.TimeAvailabilityItem

class AvailabilityListFragment : Fragment() {
    
    private lateinit var viewModel: AvailabilityListViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: AvailabilityRecyclerViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var savebutton: ProgressButton
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(AvailabilityListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_availability_list, container, false)
        
        recyclerView = root.findViewById(R.id.availability_recycler_view)
        savebutton = root.findViewById(R.id.saveAvailabilityButton)
        viewModel.selectedTimeSlots.observe(viewLifecycleOwner) {
            requireActivity().runOnUiThread {
                val timeSpans = it
                if (timeSpans != null) {
                    viewAdapter.updateSelectedTimeSlots(timeSpans.toTimeAvailabilityItems().toSet())
                }
                viewAdapter.notifyDataSetChanged()
            }
        }

        // Load selected time slots
        // Use this to get the selected time slots when the user taps 'save' viewAdapter.adjustedWeekdaySelectedTimeSlots
        
        viewAdapter = AvailabilityRecyclerViewAdapter()
        viewManager = LinearLayoutManager(requireContext())

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        
        savebutton.button.setOnClickListener {
            savebutton.isLoading = true
            val sel = viewAdapter.getAllSelectedTimeSlots()
            val updates = convertTimeItemToUpdateTimeSpan(sel.toList())
            viewModel.updateAvailability(updates, requireContext()) { error, ids ->
                requireActivity().runOnUiThread {
                    savebutton.isLoading = false
                    if (error == null) {
                        findNavController().popBackStack()
                        Toast.makeText(requireContext(), "Car Swaddle successfully saved your availability.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        return root
    }
    
    private fun convertTimeItemToUpdateTimeSpan(timeItems: List<TimeAvailabilityItem>): List<UpdateTemplateTimeSpan> {
        val updates: MutableList<UpdateTemplateTimeSpan> = mutableListOf()
        for (i in timeItems) {
            updates.add(
                UpdateTemplateTimeSpan(
                    i.weekday.value,
                    i.secondsSinceMidnight,
                    (60 * 60).toFloat()
                )
            )
        }
        return updates.toList()
    }
    
}