package com.carswaddle.carswaddlemechanic.ui.earnings.transactions

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.store.transaction.Transaction
import java.security.AccessController.getContext
import java.util.*


class TransactionsListAdapter(
    private val transactions: LiveData<List<TransactionItem>>,
    val listener: (Transaction) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
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
        if (holder is TransactionSectionViewHolder) {
            holder.depositDate = transactionItem(position)?.sectionDate ?: Calendar.getInstance()
        } else if (holder is TransactionViewHolder) {
            val transaction = transactionItem(position)?.transaction
            if (transaction != null) {
                holder.configure(transaction, listener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val t = transactionItem(position)?.type ?: TransactionItem.ItemType.TRANSACTION
        return viewType(t)
    }

    override fun getItemCount(): Int {
        return transactions.value?.count() ?: 0
    }
    
    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_TRANSACTION = 1
    }
    
    private fun transactionItem(position: Int): TransactionItem? {
        val items = transactions.value ?: return null
        return items[position]
    }
    
    private fun viewType(transactionItemType: TransactionItem.ItemType): Int {
        return when(transactionItemType) {
            TransactionItem.ItemType.SECTION -> VIEW_TYPE_HEADER
            TransactionItem.ItemType.TRANSACTION -> VIEW_TYPE_TRANSACTION
        }
    }

}
