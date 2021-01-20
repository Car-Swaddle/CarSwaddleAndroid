package com.carswaddle.carswaddlemechanic.ui.earnings.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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
import com.carswaddle.carswaddlemechanic.ui.calendar.singleday.DayAutoServiceListAdapter

class TransactionsFragment : Fragment() {

    private lateinit var transactionViewModel: TransactionsViewModel

    private lateinit var emptyStateLayout: LinearLayout

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: TransactionsListAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        transactionViewModel =
            ViewModelProvider(requireActivity()).get(TransactionsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_transactions, container, false)

        recyclerView = root.findViewById(R.id.transactions_recycler_view)
//        emptyStateLayout = root.findViewById(R.id.autoServiceListEmptyState)

//        viewModel.showEmptyState.observe(viewLifecycleOwner) {
//            emptyStateLayout.visibility = if (it) View.VISIBLE else View.GONE
//        }

        transactionViewModel.transactionItems.observe(viewLifecycleOwner) {
            requireActivity().runOnUiThread {
                viewAdapter.notifyDataSetChanged()
            }
        }

        viewAdapter = TransactionsListAdapter(transactionViewModel.transactionItems) { transaction ->
                if (transaction.type == TransactionType.payment) {
                    val bundle = bundleOf("transactionId" to transaction.id)
                    findNavController().navigate(
                        R.id.action_navigation_transactions_to_navigation_transaction_details,
                        bundle
                    )
                }
            }

        viewManager = LinearLayoutManager(requireContext())

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

//        viewModel.date = date
//        viewAdapter.date = date

        transactionViewModel.getTransactions(null, null, 100, requireContext()) { t, ids ->
            print("got back transactions")
        }

        return root
    }

}