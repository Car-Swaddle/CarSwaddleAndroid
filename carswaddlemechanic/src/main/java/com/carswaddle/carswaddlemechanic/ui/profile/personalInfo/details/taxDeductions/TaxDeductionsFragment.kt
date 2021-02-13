package com.carswaddle.carswaddlemechanic.ui.profile.personalInfo.details.taxDeductions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.carswaddlemechanic.ui.profile.availability.toTimeAvailabilityItems


class TaxDeductionsFragment : Fragment() {

    private lateinit var viewModel: TaxDeductionsViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: TaxDeductionsRecyclerViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(TaxDeductionsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_tax_deductions, container, false)
        
        recyclerView = root.findViewById(R.id.taxesRecyclerView)
        viewModel.taxInfos.observe(viewLifecycleOwner) {
            requireActivity().runOnUiThread {
                viewAdapter.taxInfos = it ?: listOf()
                viewAdapter.notifyDataSetChanged()
            }
        }
        
        viewAdapter = TaxDeductionsRecyclerViewAdapter()
        viewManager = LinearLayoutManager(requireContext())
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        return root
    }

}