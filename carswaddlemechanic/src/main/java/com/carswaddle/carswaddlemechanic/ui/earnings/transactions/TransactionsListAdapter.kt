package com.carswaddle.carswaddlemechanic.ui.earnings.transactions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.ui.activities.autoservicelist.AutoServiceListElements
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.store.transaction.Transaction
import java.util.*

class TransactionsListAdapter(
    private val transactions: LiveData<List<Transaction>>,
    val listener: (Transaction) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // create a new view
        if (viewType == VIEW_TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_transactions_header, parent, false)
            return TransactionSectionViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_transactions_item, parent, false)

            return TransactionViewHolder(view)
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        if (holder is TransactionSectionViewHolder) {
//            holder.depositDate = date
        } else if (holder is TransactionViewHolder) {
            val s = transactions.value
            if (s != null) {
                holder.configure(s[position], listener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_TRANSACTION
    }

    override fun getItemCount(): Int {
        return transactions.value?.count() ?: 0
    }

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_TRANSACTION = 1
    }

}