package com.carswaddle.carswaddleandroid.ui.activities.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.services.serviceModels.Price
import com.carswaddle.carswaddleandroid.ui.view.PriceRow
import com.carswaddle.carswaddleandroid.ui.view.ProgressBubble

class PriceFragment : Fragment() {

    private lateinit var row1: PriceRow
    private lateinit var row2: PriceRow
    private lateinit var row3: PriceRow
    private lateinit var row4: PriceRow

    var price: Price? = null
        set(value) {
            field = value
            row1.value = "" + price?.total
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