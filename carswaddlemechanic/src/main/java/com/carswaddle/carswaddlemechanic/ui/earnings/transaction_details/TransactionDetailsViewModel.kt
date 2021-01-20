package com.carswaddle.carswaddlemechanic.ui.earnings.transaction_details

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

class TransactionDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val transactionRepository: TransactionRepository

    init {
        val db = AppDatabase.getDatabase(application)
        transactionRepository = TransactionRepository(db.transactionDao())
    }
    
    var transactionId: String = "" 
    set(newValue) {
        field = newValue
        viewModelScope.launch {
            _transaction.postValue(transactionRepository.getTransaction(transactionId))
        }
    }
    
    private val _transactionMetadata = MutableLiveData<TransactionMetadata?>().apply {
        value = null
    }
    val transactionMetadata: LiveData<TransactionMetadata?> = _transactionMetadata

    private val _transaction = MutableLiveData<Transaction?>().apply {
        value = null
    }
    val transaction: LiveData<Transaction?> = _transaction
    
    fun getTransactionMetadata(transactionId: String, context: Context, completion: (t: Throwable?, transactionMetadataId: String?) -> Unit) {
        transactionRepository.getTransactionDetails(transactionId, context) { error ->
            viewModelScope.launch { 
                val m = transactionRepository.getTransactionMetadata(transactionId)
                _transactionMetadata.postValue(m)
                completion(error, m?.id)
            }
        }
    }
    
}
