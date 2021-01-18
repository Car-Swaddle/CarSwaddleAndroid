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
import com.carswaddle.store.transaction.TransactionRepository
import kotlinx.coroutines.launch

class TransactionsViewModel(application: Application) : AndroidViewModel(application) {

    private val transactionRepository: TransactionRepository

    init {
        val db = AppDatabase.getDatabase(application)
        transactionRepository = TransactionRepository(db.transactionDao())
    }
    
    private val _transactions = MutableLiveData<List<Transaction>>().apply {
        value = listOf()
    }
    val transactions: LiveData<List<Transaction>> = _transactions
    
    fun getTransactions(startingAfterId: String? = null, payoutId: String? = null, limit: Int = 10, context: Context, completion: (t: Throwable?, transactionIds: List<String>?) -> Unit) {
        transactionRepository.getTransactions(startingAfterId, payoutId, limit, context) { t, transactionIds ->
            print("transactions")
            val ids = transactionIds
            if (ids != null && t == null) {
                viewModelScope.launch {
                    val transactions = transactionRepository.getTransactions(ids = ids)
                    _transactions.postValue(transactions)
                    completion(t, transactionIds)
                }
            } else {
                completion(t, transactionIds)
            }
        }
    }

}