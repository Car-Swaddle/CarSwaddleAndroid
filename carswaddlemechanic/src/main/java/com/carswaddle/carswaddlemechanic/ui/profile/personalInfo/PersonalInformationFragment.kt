package com.carswaddle.carswaddlemechanic.ui.profile.personalInfo

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.carswaddle.carswaddleandroid.services.serviceModels.VerificationStatus
import com.carswaddle.carswaddleandroid.services.serviceModels.VerifyField
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.carswaddlemechanic.ui.common.ActionIndicatorView
import com.carswaddle.carswaddlemechanic.ui.profile.personalInfo.details.IdentificationNumberFragment


class PersonalInformationFragment : Fragment() {

    private lateinit var viewModel: PersonalInformationViewModel

    private lateinit var fullSocialActionIndicator: ActionIndicatorView
    private lateinit var last4SocialActionIndicator: ActionIndicatorView
    private lateinit var streetAddressActionIndicator: ActionIndicatorView
    private lateinit var bankAccountActionIndicator: ActionIndicatorView
    private lateinit var identificationDocumentActionIndicator: ActionIndicatorView
    private lateinit var dateOfBirthActionIndicator: ActionIndicatorView

    private lateinit var fullSocialLinearLayout: LinearLayout
    private lateinit var last4SocialLinearLayout: LinearLayout
    private lateinit var streetAddressLinearLayout: LinearLayout
    private lateinit var bankAccountLinearLayout: LinearLayout
    private lateinit var identificationDocumentLinearLayout: LinearLayout
    private lateinit var dateOfBirthLinearLayout: LinearLayout
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(PersonalInformationViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_personal_information, container, false)

        fullSocialLinearLayout = root.findViewById(R.id.fullSocialLinearLayout)
        last4SocialLinearLayout = root.findViewById(R.id.last4SocialLinearLayout)
        streetAddressLinearLayout = root.findViewById(R.id.streetAddressLinearLayout)
        bankAccountLinearLayout = root.findViewById(R.id.bankAccountLinearLayout)
        identificationDocumentLinearLayout = root.findViewById(R.id.identificationDocumentLinearLayout)
        dateOfBirthLinearLayout = root.findViewById(R.id.dateOfBirthLinearLayout)
        
        fullSocialActionIndicator = root.findViewById(R.id.fullSocialActionIndicatorView)
        last4SocialActionIndicator = root.findViewById(R.id.last4SocialActionIndicatorView)
        streetAddressActionIndicator = root.findViewById(R.id.streetAddressActionIndicatorView)
        bankAccountActionIndicator = root.findViewById(R.id.bankAccountActionIndicatorView)
        identificationDocumentActionIndicator = root.findViewById(R.id.identificationDocumentActionIndicatorView)
        dateOfBirthActionIndicator = root.findViewById(R.id.dateOfBirthActionIndicatorView)
        
        fullSocialActionIndicator.visibility = View.INVISIBLE
        last4SocialActionIndicator.visibility = View.INVISIBLE
        streetAddressActionIndicator.visibility = View.INVISIBLE
        bankAccountActionIndicator.visibility = View.INVISIBLE
        identificationDocumentActionIndicator.visibility = View.INVISIBLE
        dateOfBirthActionIndicator.visibility = View.INVISIBLE
        
        
        fullSocialLinearLayout.setOnClickListener {
            val bundle = bundleOf("mode" to IdentificationNumberFragment.IdentificationMode.FULL_SOCIAL.value)
            findNavController().navigate(
                R.id.action_navigation_personalInformation_to_navigation_identification,
                bundle
            )    
        }
        
        last4SocialLinearLayout.setOnClickListener {
            val bundle = bundleOf("mode" to IdentificationNumberFragment.IdentificationMode.LAST_4_SOCIAL.value)
            findNavController().navigate(
                R.id.action_navigation_personalInformation_to_navigation_identification,
                bundle
            )
        }
        
        streetAddressLinearLayout.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_personalInformation_to_navigation_street_address)
        }
        
        bankAccountLinearLayout.setOnClickListener { 
            findNavController().navigate(R.id.action_navigation_personalInformation_to_navigation_bank_account)
        }

        identificationDocumentLinearLayout.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_personalInformation_to_navigation_identification_documents)
        }
        
        dateOfBirthLinearLayout.setOnClickListener { 
            findNavController().navigate(R.id.action_navigation_personalInformation_to_navigation_date_of_birth)
        }
        
        viewModel.verification.observe(viewLifecycleOwner) {
            if (it != null) {
                fullSocialActionIndicator.visibility = visibilityFromVerificationStatus(it.status(VerifyField.SOCIAL_SECURITY_NUMBER_LAST_4))
                last4SocialActionIndicator.visibility = visibilityFromVerificationStatus(it.status(VerifyField.PERSONAL_ID_NUMBER))
                streetAddressActionIndicator.visibility = visibilityFromVerificationStatus(it.highestPriorityStatusForAddress())
                bankAccountActionIndicator.visibility = visibilityFromVerificationStatus(it.status(VerifyField.EXTERNAL_ACCOUNT))
                identificationDocumentActionIndicator.visibility = visibilityFromVerificationStatus(it.status(VerifyField.VERIFICATION_DOCUMENT))
                dateOfBirthActionIndicator.visibility = visibilityFromVerificationStatus(it.highestPriorityStatusForBirthday())
            }
        }

        return root
    }
    
    
    private fun visibilityFromVerificationStatus(verificationStatus: VerificationStatus): Int {
        return when(verificationStatus) {
            VerificationStatus.currentlyDue, VerificationStatus.pastDue -> View.VISIBLE
            VerificationStatus.eventuallyDue, VerificationStatus.notDue -> View.INVISIBLE
        }
    }
    
}