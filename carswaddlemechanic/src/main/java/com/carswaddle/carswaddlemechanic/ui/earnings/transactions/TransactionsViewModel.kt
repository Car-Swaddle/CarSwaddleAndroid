package com.carswaddle.carswaddlemechanic.ui.earnings.transactions

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.bumptech.glide.load.resource.transcode.TranscoderRegistry
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddleandroid.retrofit.ServiceGenerator
import com.carswaddle.carswaddleandroid.retrofit.ServiceNotAvailable
import com.carswaddle.services.services.StripeService
import com.carswaddle.store.AppDatabase
import com.carswaddle.store.balance.BalanceRepository
import com.carswaddle.store.payout.PayoutRepository
import com.carswaddle.store.transaction.Transaction
import com.carswaddle.store.transaction.TransactionMetadata
import com.carswaddle.store.transaction.TransactionRepository
import kotlinx.coroutines.launch
import java.util.*

class TransactionsViewModel(application: Application) : AndroidViewModel(application) {

    private val transactionRepository: TransactionRepository

    init {
        val db = AppDatabase.getDatabase(application)
        transactionRepository = TransactionRepository(db.transactionDao())
    }
    
    private val _transactionItems = MutableLiveData<List<TransactionItem>>().apply {
        value = listOf()
    }
    val transactionItems: LiveData<List<TransactionItem>> = _transactionItems
    
    private var transactions: List<Transaction> = listOf()
    set(newValue) {
        field = newValue
        updateViewModel()
    }
    
    fun getTransactions(startingAfterId: String? = null, payoutId: String? = null, limit: Int = 10, context: Context, completion: (t: Throwable?, transactionIds: List<String>?) -> Unit) {
        transactionRepository.getTransactions(startingAfterId, payoutId, limit, context) { t, transactionIds ->
            print("transactions")
            val ids = transactionIds
            if (ids != null && t == null) {
                viewModelScope.launch {
                    transactions = transactionRepository.getTransactions(ids)
                    completion(t, transactionIds)
                }
            } else {
                completion(t, transactionIds)
            }
        }
    }

    private fun updateViewModel() {
        var newTransactionItems: MutableList<TransactionItem> = mutableListOf()
        var lastDate: Calendar? = null
        for (transaction in transactions) {
            if (lastDate == null || lastDate != transaction.adjustedAvailableOn) {
                lastDate = transaction.adjustedAvailableOn
                val transactionSection = TransactionItem(TransactionItem.ItemType.SECTION, null, lastDate)
                newTransactionItems.add(transactionSection)
            }
            val transactionItem = TransactionItem(TransactionItem.ItemType.TRANSACTION, transaction, null)
            newTransactionItems.add(transactionItem)
        }
        _transactionItems.postValue(newTransactionItems)
    }

    fun transactionMetadata(transaction: Transaction, completion: (transactionMetadata: TransactionMetadata?) -> Unit) {
        val id = transaction.id
        viewModelScope.launch {
            val m = transactionRepository.getTransactionMetadata(id)
            completion(m)
        }
    }
    
}

data class TransactionItem(
    val type: ItemType,
    val transaction: Transaction?,
    val sectionDate: Calendar?,
) {
    
    enum class ItemType {
        SECTION, TRANSACTION
    }
    
}
