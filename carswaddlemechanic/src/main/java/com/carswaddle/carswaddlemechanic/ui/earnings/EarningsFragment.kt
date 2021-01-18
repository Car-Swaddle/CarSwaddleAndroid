package com.carswaddle.carswaddlemechanic.ui.earnings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.carswaddle.carswaddleandroid.services.serviceModels.PayoutStatus
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.carswaddlemechanic.ui.autoservice_details.AutoServiceDetailsViewModel
import java.text.NumberFormat

class EarningsFragment() : Fragment() {

    private lateinit var earningsViewModel: EarningsViewModel
    
    private lateinit var depositsLayout: RelativeLayout
    private lateinit var transactionsLayout: RelativeLayout
    private lateinit var accountBalanceTextView: TextView
    private lateinit var processingBalanceTextView: TextView
    private lateinit var transferringBalanceTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        earningsViewModel = ViewModelProvider(requireActivity()).get(EarningsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_earnings, container, false)
        
        depositsLayout = root.findViewById(R.id.depositsLayout)
        transactionsLayout = root.findViewById(R.id.transactionsLayout)
        accountBalanceTextView = root.findViewById(R.id.accountBalanceTextView)
        processingBalanceTextView = root.findViewById(R.id.processingValueTextView)
        transferringBalanceTextView = root.findViewById(R.id.transferringValueTextView)
        
        depositsLayout.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_earnings_to_depositsFragment, null)
        }
        
        transactionsLayout.setOnClickListener {
            findNavController().navigate(
                R.id.action_navigation_earnings_to_transactionsFragment,
                null
            )
        }
        
        updateAccountBalanceForCurrentValue()
        earningsViewModel.totalBalance.observe(viewLifecycleOwner) { totalBalance ->
            updateAccountBalanceForCurrentValue()
        }
        
        updateProcessingBalance()
        earningsViewModel.processingBalance.observe(viewLifecycleOwner) { processingBalance ->
            updateProcessingBalance()
        }

        updateTransferringBalance()
        earningsViewModel.transferringBalance.observe(viewLifecycleOwner) { transferringBalance ->
            updateTransferringBalance()
        }

        // Updates total balance and processing in Balance Stripe object
        earningsViewModel.updateBalance(requireContext()) {}
        // Updates transferring amount by fetching payouts with `inTransit` status and summing all amounts of those payouts.
        earningsViewModel.updateTransferringAmount(requireContext()) {}
        
        return root
    }
    
    private fun updateAccountBalanceForCurrentValue() {
        val t = earningsViewModel.totalBalance.value
        accountBalanceTextView.text = currencyFormatted(t)
    }

    private fun updateProcessingBalance() {
        val p = earningsViewModel.processingBalance.value
        processingBalanceTextView.text = currencyFormatted(p)
    }

    private fun updateTransferringBalance() {
        val t = earningsViewModel.transferringBalance.value
        transferringBalanceTextView.text = currencyFormatted(t)
    }
    
    private fun currencyFormatted(value: Int?): String {
        return if (value == null) { 
            getString(R.string.empty_value) 
        } else {
            format.format(value.toFloat()/100.0)
        }
    }
    
    companion object {
        val format: NumberFormat by lazy {
            val f = NumberFormat.getCurrencyInstance()
            f.minimumFractionDigits = 2
            f.maximumFractionDigits = 2
            return@lazy f
        }
    }
    
}