package com.carswaddle.carswaddlemechanic.ui.earnings.deposits

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.store.payout.Payout
import com.carswaddle.store.transaction.Transaction
import java.security.AccessController.getContext
import java.util.*


class PayoutsListAdapter(
    private val payouts: LiveData<List<Payout>>,
    val listener: (Payout) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_payouts_item, parent, false)
        return PayoutViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PayoutViewHolder) {
            val ps = payouts.value ?: listOf()
            val payout = ps[position] 
            if (payout != null) {
                holder.configure(payout, listener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_PAYOUT
    }
    
    override fun getItemCount(): Int {
        return payouts.value?.count() ?: 0
    }
    
    companion object {
        private const val VIEW_TYPE_PAYOUT = 1
    }

}
