package com.carswaddle.carswaddlemechanic.ui.profile.availability

import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.services.serviceModels.TransactionType
import com.carswaddle.carswaddleandroid.ui.view.ProgressButton
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.carswaddlemechanic.ui.common.ActionIndicatorView
import com.carswaddle.carswaddlemechanic.ui.earnings.transactions.TransactionsListAdapter
import com.carswaddle.carswaddlemechanic.ui.profile.MechanicProfileViewModel

class AvailabilityListFragment : Fragment() {
    
    private lateinit var viewModel: AvailabilityListViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: AvailabilityRecyclerViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(AvailabilityListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_availability_list, container, false)
        
        recyclerView = root.findViewById(R.id.availability_recycler_view)
        viewModel.reviews.observe(viewLifecycleOwner) {
            requireActivity().runOnUiThread {
                viewAdapter.notifyDataSetChanged()
            }
        }

        viewAdapter = AvailabilityRecyclerViewAdapter()
        viewAdapter.selectedTimeSlots = listOf()
        viewManager = LinearLayoutManager(requireContext())

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        
        return root
    }
    
}