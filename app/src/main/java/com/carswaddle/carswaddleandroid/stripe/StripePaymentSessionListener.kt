package com.carswaddle.carswaddleandroid.stripe

import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.stripe.android.PaymentSession
import com.stripe.android.PaymentSessionData
import com.stripe.android.model.CardBrand

class StripePaymentSessionListener : PaymentSession.PaymentSessionListener {
    
    val isLoading: LiveData<Boolean>
        get() = _isLoading
    
    private var _isLoading = MutableLiveData<Boolean>()
    
    // Called whenever the PaymentSession's data changes,
    // e.g. when the user selects a new `PaymentMethod` or enters shipping info.
    override fun onPaymentSessionDataChanged(data: PaymentSessionData) {
        if (data.useGooglePay) {
            // customer intends to pay with Google Pay
        } else {
            data.paymentMethod?.let { paymentMethod ->
                // Display information about the selected payment method
            }
        }

        // Update your UI here with other data
        if (data.isPaymentReadyToCharge) {
            // Use the data to complete your charge - see below.
        }
    }

    override fun onCommunicatingStateChanged(isCommunicating: Boolean) {
        _isLoading.postValue(isCommunicating) 
    }
    
    override fun onError(errorCode: Int, errorMessage: String) {
        
    }
    
}