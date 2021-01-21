package com.carswaddle.carswaddlemechanic.ui.earnings.deposits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.services.serviceModels.TransactionType
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.carswaddlemechanic.ui.earnings.EarningsViewModel
import com.carswaddle.carswaddlemechanic.ui.earnings.transactions.TransactionsListAdapter

class PayoutsFragment: Fragment() {

    private lateinit var payoutsViewModel: PayoutsViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: PayoutsListAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        payoutsViewModel = ViewModelProvider(requireActivity()).get(PayoutsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_payouts, container, false)

        recyclerView = root.findViewById(R.id.payouts_recycler_view)
        payoutsViewModel.payouts.observe(viewLifecycleOwner) {
            requireActivity().runOnUiThread {
                viewAdapter.notifyDataSetChanged()
            }
        }

        viewAdapter = PayoutsListAdapter(payoutsViewModel.payouts) { payout ->
            val bundle = bundleOf("payoutId" to payout.id)
            findNavController().navigate(
                R.id.action_navigation_payouts_to_navigation_transactions,
                bundle
            )
        }

        viewManager = LinearLayoutManager(requireContext())

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        payoutsViewModel.getPayouts(null, null, 100, requireContext()) { t, ids ->
            print("got back transactions")
        }
        
        return root
    }

}