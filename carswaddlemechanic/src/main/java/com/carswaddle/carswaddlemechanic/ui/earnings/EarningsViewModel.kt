package com.carswaddle.carswaddlemechanic.ui.earnings

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.carswaddle.carswaddleandroid.data.Review.ReviewRepository
import com.carswaddle.carswaddleandroid.data.autoservice.AutoServiceRepository
import com.carswaddle.carswaddleandroid.data.location.AutoServiceLocationRepository
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicRepository
import com.carswaddle.carswaddleandroid.data.oilChange.OilChangeRepository
import com.carswaddle.carswaddleandroid.data.serviceEntity.ServiceEntityRepository
import com.carswaddle.carswaddleandroid.data.user.MechanicIdIsUnavailable
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddleandroid.data.vehicle.VehicleRepository
import com.carswaddle.carswaddleandroid.services.serviceModels.PayoutStatus
import com.carswaddle.store.AppDatabase
import com.carswaddle.store.balance.BalanceRepository
import com.carswaddle.store.payout.PayoutRepository
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class EarningsViewModel(application: Application) : AndroidViewModel(application) {
    
    private val balanceRepo: BalanceRepository
    private val userRepo: UserRepository
    private val payoutRepo: PayoutRepository

    init {
        val db = AppDatabase.getDatabase(application)
        balanceRepo = BalanceRepository(db.balanceDao())
        userRepo = UserRepository(db.userDao())
        payoutRepo = PayoutRepository(db.payoutDao())
    }
    
    fun updateBalance(context: Context, completion: (error: Throwable?) -> Unit) {
        val currentMechanicId = userRepo.getCurrentMechanicId(context)
        if (currentMechanicId != null) {
            balanceRepo.getBalance(currentMechanicId, context) {
                viewModelScope.launch {
                    val newBalance = balanceRepo.getBalance(currentMechanicId)
                    if (newBalance != null) {
                        _totalBalance.postValue(newBalance.availableValue)
                        _processingBalance.postValue(newBalance.pendingValue)
                    }
                    completion(it)
                }
            }
        } else {
            completion(MechanicIdIsUnavailable())
        }
    }
    
    fun updateTransferringAmount(context: Context, completion: (error: Throwable?) -> Unit) {
        payoutRepo.getPayouts(null, PayoutStatus.inTransit, 100, context) { error, payoutIds ->
            print("payouts")
            viewModelScope.launch {
                val inTransit = payoutRepo.payoutSumInTransit()
                if (inTransit != null) {
                    _transferringBalance.postValue(inTransit)
                }
                completion(null)
            }
        }
    }
    
    /// Number of cents
    private val _totalBalance = MutableLiveData<Int?>().apply {
        value = null
    }
    val totalBalance: LiveData<Int?> = _totalBalance

    /// Number of cents being processed
    private val _processingBalance = MutableLiveData<Int?>().apply {
        value = null
    }
    val processingBalance: LiveData<Int?> = _processingBalance

    /// Number of cents being transferred
    private val _transferringBalance = MutableLiveData<Int?>().apply {
        value = null
    }
    val transferringBalance: LiveData<Int?> = _transferringBalance

}