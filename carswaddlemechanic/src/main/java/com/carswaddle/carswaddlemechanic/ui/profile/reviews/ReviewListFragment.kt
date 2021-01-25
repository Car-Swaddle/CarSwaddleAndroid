package com.carswaddle.carswaddlemechanic.ui.profile.reviews

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

class ReviewListFragment : Fragment() {
    
    private lateinit var viewModel: ReviewListViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ReviewsListAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ReviewListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_review_list, container, false)
        
        recyclerView = root.findViewById(R.id.reviews_recycler_view)
        viewModel.reviews.observe(viewLifecycleOwner) {
            requireActivity().runOnUiThread {
                viewAdapter.notifyDataSetChanged()
            }
        }

        viewAdapter = ReviewsListAdapter(viewModel.reviews, requireContext()) { review ->
//            if (review.type == TransactionType.payment) {
//                val bundle = bundleOf("transactionId" to transaction.id)
//                findNavController().navigate(
//                    R.id.action_navigation_transactions_to_navigation_transaction_details,
//                    bundle
//                )
//            }
            print("tapped")
        }

        viewManager = LinearLayoutManager(requireContext())

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

//        viewModel.getTransactions(null, payoutId, 100, requireContext()) { t, ids ->
//            print("got back transactions")
//        }
        
        
        
        return root
    }
    
}