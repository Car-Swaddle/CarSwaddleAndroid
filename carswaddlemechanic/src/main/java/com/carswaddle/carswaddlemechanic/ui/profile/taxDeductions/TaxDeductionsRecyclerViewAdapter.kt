package com.carswaddle.carswaddlemechanic.ui.profile.taxDeductions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.foundation.Extensions.metersToMiles
import com.carswaddle.store.taxInfo.TaxInfo as TaxInfoStoreModel
import java.math.RoundingMode
import java.text.NumberFormat


/// Displays a list of the week with time slots on each day
class TaxDeductionsRecyclerViewAdapter() : RecyclerView.Adapter<TaxDeductionsRecyclerViewAdapter.TaxInfoViewHolder>() {
    
    var taxInfos: List<TaxInfoStoreModel> = listOf()
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaxInfoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_tax_deductions, parent, false)
        return TaxInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaxInfoViewHolder, position: Int) {
        holder.configure(taxInfos[position])
    }

    override fun getItemCount(): Int = taxInfos.count()

    class TaxInfoViewHolder(view: View) : RecyclerView.ViewHolder(view)  {
        
        private val taxYearTextView: TextView = view.findViewById(R.id.taxYearTextView)
        private val dollarTextView: TextView = view.findViewById(R.id.dollarDeductTextView)
        private val milesTextView: TextView = view.findViewById(R.id.milesDeductTextView)
        
        fun configure(taxInfo: TaxInfoStoreModel) {
            taxYearTextView.text = taxInfo.year
            dollarTextView.text = currencyFormatter.format(taxInfo.mechanicCostInCents/100.0)
            milesTextView.text = numberFormatter.format(taxInfo.metersDriven.metersToMiles())
        }
        
        companion object {
            val currencyFormatter: NumberFormat by lazy {
                val f = NumberFormat.getCurrencyInstance()
                f.minimumFractionDigits = 2
                f.maximumFractionDigits = 2
                return@lazy f
            }

            val numberFormatter: NumberFormat by lazy {
                val f = NumberFormat.getNumberInstance()
                f.minimumFractionDigits = 0
                f.maximumFractionDigits = 0
                f.roundingMode = RoundingMode.FLOOR
                return@lazy f
            }
        }
    }

}