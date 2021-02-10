package com.carswaddle.carswaddlemechanic.ui.profile.personalInfo.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.carswaddle.carswaddleandroid.ui.view.ProgressButton
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.carswaddlemechanic.stripe.stripePublishableKey
import com.carswaddle.carswaddlemechanic.ui.profile.personalInfo.PersonalInformationViewModel
import com.carswaddle.ui.LabeledEditText
import com.mukesh.OtpView
import com.stripe.android.ApiResultCallback
import com.stripe.android.Stripe
import com.stripe.android.model.BankAccountTokenParams
import com.stripe.android.model.PersonTokenParams
import com.stripe.android.model.Token

class BankAccountFragment : Fragment(), ApiResultCallback<Token> {

    private lateinit var viewModel: BankAccountViewModel

    private lateinit var routingNumberOtpView: OtpView
    private lateinit var accountNumberLabeledEditText: LabeledEditText
    private lateinit var accountHolderLabeledEditText: LabeledEditText

    private lateinit var saveButton: ProgressButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(BankAccountViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_bank_account, container, false)

        routingNumberOtpView = root.findViewById(R.id.routingNumberOtpView)
        accountNumberLabeledEditText = root.findViewById(R.id.accountNumberLabeledEditText)
        accountHolderLabeledEditText = root.findViewById(R.id.accountHolderLabeledEditText)

        saveButton = root.findViewById(R.id.saveBankAccountButton)
        
        saveButton.button.setOnClickListener { 
            saveBankAccount()
        }
        
        return root
    }
    
    
    
    private fun saveBankAccount() {
        val routing = routingNumberOtpView.text?.toString()
        val number = accountNumberLabeledEditText.editTextValue ?: ""
        val name = accountHolderLabeledEditText.editTextValue ?: ""
        
        if (routing?.isEmpty() == true || name.isEmpty() == true || number.isEmpty() == true) {
            return
        }
        saveButton.isLoading = true
        val stripe = Stripe(requireContext(), stripePublishableKey())
        val bankAccountParams = BankAccountTokenParams("US", "usd", number, BankAccountTokenParams.Type.Individual, name, routing)
        BankAccountTokenParams("US", "usd", "num", BankAccountTokenParams.Type.Individual, name, routing)
        stripe.createBankAccountToken(bankAccountParams, null, null, this)
    }

    private fun finishSuccessfully() {
        saveButton.isLoading = false
        findNavController().popBackStack()
        Toast.makeText(requireContext(), "Car Swaddle successfully saved your bank account.", Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess(result: Token) {
        viewModel.updateBankAccountToken(result.id, requireContext()) {
            requireActivity().runOnUiThread {
                if (it == null) {
                    finishSuccessfully()
                }
            }
        }
    }

    override fun onError(e: Exception) {
        saveButton.isLoading = false
        Toast.makeText(requireContext(), "The routing number or bank account number is invalid", Toast.LENGTH_SHORT).show()
    }
    
}