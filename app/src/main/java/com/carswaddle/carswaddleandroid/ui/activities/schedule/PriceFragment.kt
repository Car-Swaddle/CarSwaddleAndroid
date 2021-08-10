package com.carswaddle.carswaddleandroid.ui.activities.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.services.services.serviceModels.Price
import com.carswaddle.carswaddleandroid.ui.view.PriceRow

class PriceFragment : Fragment() {

    private lateinit var oilChangePriceRow: PriceRow
    private lateinit var discountPriceRow: PriceRow
    private lateinit var totalPriceRow: PriceRow
    private lateinit var bookingFeePriceRow: PriceRow

    var price: Price? = null
        set(value) {
            field = value
            if (value == null) {
                return
            }
            updatePrice(value)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_price, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        oilChangePriceRow = view.findViewById(R.id.oilChangePriceRow)
        oilChangePriceRow.label = getString(R.string.oil_change)
        bookingFeePriceRow = view.findViewById(R.id.bookingFeePriceRow)
        bookingFeePriceRow.label = getString(R.string.booking_fee)
        discountPriceRow = view.findViewById(R.id.discountPriceRow)
        discountPriceRow.label = getString(R.string.discount)
        discountPriceRow.visibility = GONE // hide discount by default
        totalPriceRow = view.findViewById(R.id.totalPriceRow)
        totalPriceRow.label = getString(R.string.total)
    }

    private fun updatePrice(price: Price) {
        oilChangePriceRow.value = String.format("%.2f", price.oilChangeTotal / 100.0)
        bookingFeePriceRow.value = String.format("%.2f", (price.bookingFee + price.processingFee + price.salesTaxTotal) / 100.0)
//        row2.value = String.format("%.2f", price.salesTaxTotal / 100.0)
        val discount = price.discountTotal
        if (discount == null || discount == 0) {
            discountPriceRow.visibility = View.GONE
            discountPriceRow.value = ""
        } else {
            discountPriceRow.visibility = View.VISIBLE
            discountPriceRow.value = String.format("%.2f", discount / 100.0)
        }

        totalPriceRow.value = String.format("$%.2f", price.total / 100.0)
    }
    
}