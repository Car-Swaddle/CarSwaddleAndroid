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
import com.carswaddle.store.AppDatabase
import com.carswaddle.store.balance.BalanceRepository
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class EarningsViewModel(application: Application) : AndroidViewModel(application) {
    
    private val balanceRepo: BalanceRepository
    private val userRepo: UserRepository

    init {
        val db = AppDatabase.getDatabase(application)
        balanceRepo = BalanceRepository(db.balanceDao())
        userRepo = UserRepository(db.userDao())
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