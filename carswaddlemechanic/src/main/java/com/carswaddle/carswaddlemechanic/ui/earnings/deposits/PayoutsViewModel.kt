package com.carswaddle.carswaddlemechanic.ui.earnings.deposits

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.carswaddle.carswaddleandroid.services.serviceModels.PayoutStatus
import com.carswaddle.carswaddleandroid.services.serviceModels.Payout as PayoutServiceModel
import com.carswaddle.carswaddlemechanic.ui.earnings.transactions.TransactionItem
import com.carswaddle.store.AppDatabase
import com.carswaddle.store.payout.Payout as PayoutStoreModel
import com.carswaddle.store.payout.PayoutRepository
import com.carswaddle.store.transaction.Transaction
import com.carswaddle.store.transaction.TransactionRepository
import kotlinx.coroutines.launch

class PayoutsViewModel(application: Application) : AndroidViewModel(application) {

    private val payoutRepository: PayoutRepository

    init {
        val db = AppDatabase.getDatabase(application)
        payoutRepository = PayoutRepository(db.payoutDao())
    }
    
    private val _payouts = MutableLiveData<List<PayoutStoreModel>>().apply {
        value = listOf()
    }
    val payouts: LiveData<List<PayoutStoreModel>> = _payouts

    fun getPayouts(startingAfterId: String? = null, status: PayoutStatus? = null, limit: Int = 10, context: Context, completion: (t: Throwable?, payoutIds: List<String>?) -> Unit) {
        payoutRepository.getPayouts(startingAfterId, status, limit, context) { t, payoutIds ->
//        transactionRepository.getTransactions(startingAfterId, payoutId, limit, context) { t, transactionIds ->
            print("transactions")
            val ids = payoutIds
            if (ids != null && t == null) {
                viewModelScope.launch {
                    val newPayouts = payoutRepository.getPayouts(ids)
                    _payouts.postValue(newPayouts)
                    completion(t, payoutIds)
                }
            } else {
                completion(t, payoutIds)
            }
        }
    }

}