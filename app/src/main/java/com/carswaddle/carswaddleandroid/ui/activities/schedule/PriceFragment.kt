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

    private lateinit var row1: PriceRow
    private lateinit var row2: PriceRow
    private lateinit var row3: PriceRow
    private lateinit var row4: PriceRow

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

        row1 = view.findViewById(R.id.row1)
        row1.label = "Oil change"
        row2 = view.findViewById(R.id.row2)
        row2.label = "Sales tax"
        row3 = view.findViewById(R.id.row3)
        row3.label = "Discount"
        row3.visibility = GONE // hide discount by default
        row4 = view.findViewById(R.id.row4)
        row4.label = "Total"
    }

    private fun updatePrice(price: Price) {
        row1.value = String.format("%.2f", price.oilChangeTotal / 100.0)
        row2.value = String.format("%.2f", price.salesTaxTotal / 100.0)
        val discount = price.discountTotal
        if (discount == null || discount == 0) {
            row3.visibility = View.GONE
            row3.value = ""
        } else {
            row3.visibility = View.VISIBLE
            row3.value = String.format("%.2f", discount / 100.0)
        }
        
        row4.value = String.format("$%.2f", price.total / 100.0)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProgressFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProgressFragment()
    }
}