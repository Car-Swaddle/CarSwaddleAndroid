package com.carswaddle.carswaddlemechanic.ui.profile.personalInfo.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.carswaddle.carswaddleandroid.ui.view.ProgressButton
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.carswaddlemechanic.stripe.stripePublishableKey
import com.carswaddle.carswaddlemechanic.ui.profile.personalInfo.PersonalInformationViewModel
import com.mukesh.OtpView
import com.stripe.android.ApiResultCallback
import com.stripe.android.Stripe
import com.stripe.android.model.Token


class IdentificationNumberFragment : Fragment(), ApiResultCallback<Token> {

    private lateinit var viewModel: IdentificationNumberViewModel

    private var identificationMode: IdentificationMode = IdentificationMode.FULL_SOCIAL
        set(newValue) {
            field = newValue
            updateUI()
        }

    enum class IdentificationMode(val value: String) {
        FULL_SOCIAL("fullSocial"),
        LAST_4_SOCIAL("last4Social");

        companion object {
            fun fromString(value: String) = values().first { it.value == value }
        }
    }

    private lateinit var otpView: OtpView
    private lateinit var titleTextView: TextView
    private lateinit var explainTextView: TextView
    private lateinit var saveButton: ProgressButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(IdentificationNumberViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_social_security, container, false)

        otpView = root.findViewById(R.id.socialSecurityOtpView)
        titleTextView = root.findViewById(R.id.socialSecurityTitleTextView)
        explainTextView = root.findViewById(R.id.socialSecurityExplainTextView)
        saveButton = root.findViewById(R.id.socialSecurityProgressButton)

        this.identificationMode = IdentificationMode.fromString(arguments?.getString("mode") ?: "")

        saveButton.button.setOnClickListener {
            save()
        }

        updateUI()

        return root
    }

    private fun updateUI() {
        otpView.itemCount = expectedStringCount()
        when (identificationMode) {
            IdentificationMode.FULL_SOCIAL -> {
                titleTextView.text = getText(R.string.full_social_title)
                explainTextView.text = getText(R.string.full_social_explain)
            }
            IdentificationMode.LAST_4_SOCIAL -> {
                titleTextView.text = getText(R.string.last_4_social_title)
                explainTextView.text = getText(R.string.last_4_social_explain)
            }
        }
    }

    private fun expectedStringCount(): Int {
        return when (identificationMode) {
            IdentificationMode.FULL_SOCIAL -> 9
            IdentificationMode.LAST_4_SOCIAL -> 4
        }
    }

    private fun save() {
        val ss = otpView.text?.toString()
        if (ss == null || ss.count() != expectedStringCount()) {
            return
        }
        saveButton.isLoading = true

        when (identificationMode) {
            IdentificationMode.FULL_SOCIAL -> {
                val stripe = Stripe(requireContext(), stripePublishableKey())
                stripe.createPiiToken(ss, callback = this)
            }
            IdentificationMode.LAST_4_SOCIAL -> {
                viewModel.updateSocialSecurityLast4(ss, requireContext()) {
                    requireActivity().runOnUiThread {
                        if (it == null) {
                            finishSuccessfully()
                        }
                    }
                }
            }
        }
    }

    override fun onSuccess(result: Token) {
        viewModel.updateFullSocial(result.id, requireContext()) {
            requireActivity().runOnUiThread {
                if (it == null) {
                    finishSuccessfully()
                }
            }
        }
    }
    
    private fun finishSuccessfully() {
        
        val text = when(identificationMode) {
            IdentificationMode.LAST_4_SOCIAL -> getString(R.string.toast_last_4)
            IdentificationMode.FULL_SOCIAL -> getString(R.string.toast_full_social)
        }
        saveButton.isLoading = false
        findNavController().popBackStack()
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    override fun onError(e: Exception) {
        print("error")
    }

}