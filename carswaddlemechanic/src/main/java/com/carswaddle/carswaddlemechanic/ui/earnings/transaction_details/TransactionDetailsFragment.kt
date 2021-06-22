package com.carswaddle.carswaddlemechanic.ui.earnings.transaction_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.carswaddlemechanic.ui.earnings.transactions.TransactionViewHolder
import com.carswaddle.carswaddlemechanic.ui.earnings.transactions.localizedString
import com.carswaddle.foundation.Extensions.centsToDollars
import com.carswaddle.foundation.Extensions.metersToMiles
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*

class TransactionDetailsFragment : Fragment() {

    private lateinit var viewModel: TransactionDetailsViewModel

    private lateinit var transactionId: String

    private lateinit var transactionDetailsDateTextView: TextView
    private lateinit var transactionTypeTextView: TextView
    private lateinit var paymentAmountValueTextView: TextView
    private lateinit var writeOffValueTextView: TextView
    private lateinit var milesDrivenValueTextView: TextView
    private lateinit var autoServiceTextView: TextView
    private lateinit var autoServiceSelectionLinearLayout: LinearLayout


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(requireActivity()).get(TransactionDetailsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_transaction_details, container, false)

        this.transactionId = arguments?.getString("transactionId") ?: ""

        viewModel.transactionId = transactionId

        transactionDetailsDateTextView = root.findViewById(R.id.transactionDetailsDateTextView)
        transactionTypeTextView = root.findViewById(R.id.transactionTypeTextView)
        paymentAmountValueTextView = root.findViewById(R.id.paymentAmountValueTextView)
        writeOffValueTextView = root.findViewById(R.id.writeOffValueTextView)
        milesDrivenValueTextView = root.findViewById(R.id.milesDrivenValueTextView)
        autoServiceTextView = root.findViewById(R.id.autoServiceTextView)
        autoServiceSelectionLinearLayout = root.findViewById(R.id.autoServiceSelectionLinearLayout)

        viewModel.transactionMetadata.observe(viewLifecycleOwner) {
            requireActivity().runOnUiThread {
                it?.let { metadata ->
                    val cost = metadata.mechanicCost.centsToDollars()
                    writeOffValueTextView.text = currencyFormatter.format(cost)
                    val milesDriven = milesFormatter.format(metadata.drivingDistance.metersToMiles())
                    milesDrivenValueTextView.text = "$milesDriven miles driven"
                }
            }
        }

        viewModel.transaction.observe(viewLifecycleOwner) {
            requireActivity().runOnUiThread {
                it?.let { transaction ->
                    val localDateTime = LocalDateTime.ofInstant(
                        transaction.created.toInstant(),
                        transaction.created.timeZone.toZoneId()
                    )
                    transactionDetailsDateTextView.text = dateFormatter.format(localDateTime)
                    transactionTypeTextView.text = transaction.type.localizedString()
                    val dollars = transaction.amount.centsToDollars()
                    paymentAmountValueTextView.text = currencyFormatter.format(dollars)
                }
            }
        }
        
        autoServiceSelectionLinearLayout.setOnClickListener { 
            print("tapped autoservice")
            val autoServiceId = viewModel.transactionMetadata.value?.autoServiceID
            autoServiceId?.let {
                val bundle = bundleOf("autoServiceId" to it)
                findNavController().navigate(
                    R.id.action_navigation_transaction_details_to_autoServiceDetailsFragment,
                    bundle
                )
            }
        }
        
        viewModel.getTransactionMetadata(
            transactionId,
            requireContext()
        ) { error, transactionMetadataId -> }

        return root
    }

    companion object {
        val dateFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
            .appendPattern("MMM d, yyyy")
            .toFormatter(Locale.US)
        val currencyFormatter: NumberFormat by lazy {
            val f = NumberFormat.getCurrencyInstance()
            f.minimumFractionDigits = 2
            f.maximumFractionDigits = 2
            return@lazy f
        }
        val milesFormatter: NumberFormat by lazy {
            val f = NumberFormat.getNumberInstance()
            f.minimumFractionDigits = 0
            f.maximumFractionDigits = 0
            return@lazy f
        }
    }

}
