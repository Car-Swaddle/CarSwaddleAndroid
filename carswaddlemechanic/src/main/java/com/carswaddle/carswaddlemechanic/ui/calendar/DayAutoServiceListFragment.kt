package com.carswaddle.carswaddlemechanic.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddlemechanic.R
import java.util.*

class DayAutoServiceListFragment : Fragment() {
    
    var date: Calendar? = null
    set(newValue) {
        field = newValue
        if (newValue != null) {
            viewAdapter.date = newValue
        } 
    }

    private lateinit var viewModel: DayAutoServiceListViewModel
    
    private lateinit var emptyStateLayout: LinearLayout
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: DayAutoServiceListAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(DayAutoServiceListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_day_autoservice_list, container, false)

        recyclerView = root.findViewById(R.id.day_autoservice_list_recycler_view)
        emptyStateLayout = root.findViewById(R.id.autoServiceListEmptyState)

        viewModel.showEmptyState.observe(viewLifecycleOwner) {
            emptyStateLayout.visibility = if (it) View.VISIBLE else View.GONE
        }
        
        viewModel.dayAutoServices.observe(viewLifecycleOwner) {
            viewAdapter.notifyDataSetChanged()
        }

        viewAdapter = DayAutoServiceListAdapter(viewModel.dayAutoServices) {
            val manager = childFragmentManager
            if (manager != null) {
                val bundle = bundleOf("autoServiceId" to it.autoService.id)
//                findNavController().navigate(R.id.action_navigation_autoservices_list_to_navigation_autoservice_details, bundle)
            }
        }

        viewManager = LinearLayoutManager(requireContext())
        
        recyclerView.apply {
            setHasFixedSize(true)    
            layoutManager = viewManager
            adapter = viewAdapter
        }
        
        viewModel.date = Calendar.getInstance()
        
        return root
    }
}