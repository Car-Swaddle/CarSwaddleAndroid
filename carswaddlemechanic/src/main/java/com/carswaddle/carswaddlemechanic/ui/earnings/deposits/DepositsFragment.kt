package com.carswaddle.carswaddlemechanic.ui.earnings.deposits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.carswaddlemechanic.ui.earnings.EarningsViewModel

class DepositsFragment: Fragment() {

    private lateinit var depositsViewModel: DepositsViewModel

//    private lateinit var depositsLayout: RelativeLayout
//    private lateinit var transactionsLayout: RelativeLayout
//    private lateinit var accountBalanceTextView: TextView
//    private lateinit var processingBalanceTextView: TextView
//    private lateinit var transferringBalanceTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        depositsViewModel = ViewModelProvider(requireActivity()).get(DepositsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_deposits, container, false)

//        depositsLayout = root.findViewById(R.id.depositsLayout)
//        transactionsLayout = root.findViewById(R.id.transactionsLayout)
//        accountBalanceTextView = root.findViewById(R.id.accountBalanceTextView)
//        processingBalanceTextView = root.findViewById(R.id.processingValueTextView)
//        transferringBalanceTextView = root.findViewById(R.id.transferringValueTextView)
//
//        depositsLayout.setOnClickListener {
//
//        }
//
//        transactionsLayout.setOnClickListener {
//
//        }

        return root
    }

}