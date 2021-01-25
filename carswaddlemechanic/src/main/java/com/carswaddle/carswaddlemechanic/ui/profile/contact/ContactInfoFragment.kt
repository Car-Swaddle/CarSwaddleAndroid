package com.carswaddle.carswaddlemechanic.ui.profile.contact

import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.carswaddle.carswaddleandroid.ui.view.ProgressButton
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.carswaddlemechanic.ui.common.ActionIndicatorView
import com.carswaddle.carswaddlemechanic.ui.profile.MechanicProfileViewModel

class ContactInfoFragment : Fragment() {
    
    private lateinit var viewModel: ContactInfoViewModel
    
    private lateinit var emailTextView: TextView
    private lateinit var emailExplanationTextView: TextView
    private lateinit var resendEmailButton: ProgressButton
    private lateinit var phoneNumberTextView: TextView
    private lateinit var contactEmailActionIndicatorView: ActionIndicatorView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ContactInfoViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_mechanic_contact_info, container, false)
        
        emailTextView = root.findViewById(R.id.contactEmailTextView)
        resendEmailButton = root.findViewById(R.id.resendEmailButton)
        phoneNumberTextView = root.findViewById(R.id.contactPhoneNumberTextView)
        emailExplanationTextView = root.findViewById(R.id.emailExplanationTextView)
        contactEmailActionIndicatorView = root.findViewById(R.id.contactEmailActionIndicatorView)
        
        viewModel.mechanicUser.observe(viewLifecycleOwner) {
            it?.let {  
                requireActivity().runOnUiThread {
                    emailTextView.text = it.email
                    phoneNumberTextView.text = PhoneNumberUtils.formatNumber(it.phoneNumber, "US")
                    
                    if (it.isEmailVerified == true) {
                        emailExplanationTextView.text = resources.getString(R.string.email_verified)
                    } else {
                        emailExplanationTextView.text = resources.getString(R.string.email_unverified)
                    }
                    contactEmailActionIndicatorView.visibility = if (it.isEmailVerified == true) View.GONE else View.VISIBLE
                }
            }
        }
        
        resendEmailButton.button.setOnClickListener {
            resendEmailButton.isLoading = true
            viewModel.resendVerificationEmail(requireContext()) {
                resendEmailButton.isLoading = false
                resendEmailButton.isButtonEnabled = false
                if (it == null) {
                    resendEmailButton.displayText = getString(R.string.successfully_sent_email)
                } else {
                    resendEmailButton.displayText = getString(R.string.unable_to_resend)
                }
            }
        }
        
        return root
    }
    
}