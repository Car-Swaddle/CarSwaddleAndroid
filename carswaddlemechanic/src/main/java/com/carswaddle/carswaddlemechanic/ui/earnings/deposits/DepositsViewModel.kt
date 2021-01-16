package com.carswaddle.carswaddlemechanic.ui.earnings.deposits

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DepositsViewModel(application: Application) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

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