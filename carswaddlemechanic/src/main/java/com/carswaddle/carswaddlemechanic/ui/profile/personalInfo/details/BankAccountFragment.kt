package com.carswaddle.carswaddlemechanic.ui.profile.personalInfo.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.carswaddlemechanic.stripe.stripePublishableKey
import com.carswaddle.carswaddlemechanic.ui.profile.personalInfo.PersonalInformationViewModel
import com.stripe.android.ApiResultCallback
import com.stripe.android.Stripe
import com.stripe.android.model.BankAccountTokenParams
import com.stripe.android.model.PersonTokenParams
import com.stripe.android.model.Token

class BankAccountFragment : Fragment(), ApiResultCallback<Token> {

    private lateinit var viewModel: PersonalInformationViewModel

    private var accountNumber: String? = null
    private var accountHolderName: String? = null
    private var routingNumber: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(PersonalInformationViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_personal_information, container, false)
        
        return root
    }
    
    private fun saveBankAccount() {
        val a = accountNumber
        val n = accountHolderName
        val r = routingNumber
        
        if (a == null || n == null || r == null) {
            return
        }
        val stripe = Stripe(
            requireContext(),
            stripePublishableKey()
        )
        
        
        
    }

    override fun onSuccess(result: Token) {
        print("success $result.id")
        result.id
    }

    override fun onError(e: Exception) {
        print("error")
    }
    
}