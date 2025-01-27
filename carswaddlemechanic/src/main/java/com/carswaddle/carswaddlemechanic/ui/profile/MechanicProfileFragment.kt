package com.carswaddle.carswaddlemechanic.ui.profile

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.carswaddle.carswaddleandroid.data.mechanic.Mechanic
import com.carswaddle.carswaddleandroid.services.IdDocumentImageSide
import com.carswaddle.carswaddleandroid.services.serviceModels.UpdateMechanic
import com.carswaddle.carswaddleandroid.services.serviceModels.VerifyField
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.carswaddlemechanic.ui.common.ActionIndicatorView
import com.carswaddle.carswaddlemechanic.ui.common.MechanicImageView
import com.carswaddle.carswaddlemechanic.ui.login.AuthActivity
import com.carswaddle.foundation.generic.PathUtil
import com.carswaddle.services.Authentication
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode

class MechanicProfileFragment : Fragment() {

    private lateinit var viewModel: MechanicProfileViewModel

    private lateinit var mechanicImageView: MechanicImageView
    private lateinit var ratingBar: AppCompatRatingBar
    private lateinit var ratingsTextView: TextView
    private lateinit var servicesCompletedTextView: TextView
    private lateinit var mechanicNameTextView: TextView
    private lateinit var allReviewsTextView: TextView
    private lateinit var allowNewAppointmentsSwitch: SwitchCompat
    private lateinit var chargeForTravelSwitch: SwitchCompat
    private lateinit var setServiceRegionContainer: LinearLayout
    private lateinit var setHoursContainer: LinearLayout
    private lateinit var setPricingContainer: LinearLayout
    private lateinit var personalInformationContainer: LinearLayout
    private lateinit var contactInformationContainer: LinearLayout
    private lateinit var profileContactInfoActionIndicator: ActionIndicatorView
    private lateinit var personalInformationActionIndicator: ActionIndicatorView
    private lateinit var taxDeductionsContainer: LinearLayout
    private lateinit var logoutButton: Button
    
    private lateinit var editTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(MechanicProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_mechanic_profile, container, false)

        ratingBar = root.findViewById(R.id.ratingBar)
        ratingsTextView = root.findViewById(R.id.ratingsTextView)
        servicesCompletedTextView = root.findViewById(R.id.servicesCompletedTextView)
        mechanicNameTextView = root.findViewById(R.id.mechanicProfileNameTextView)
        allReviewsTextView = root.findViewById(R.id.reviewsTextView)
        allowNewAppointmentsSwitch = root.findViewById(R.id.allowNewSwitch)
        chargeForTravelSwitch = root.findViewById(R.id.chargeForTravelSwitch)
        setServiceRegionContainer = root.findViewById(R.id.setServiceRegionContainer)
        setHoursContainer = root.findViewById(R.id.setHoursContainer)
        setPricingContainer = root.findViewById(R.id.setPricingContainer)
        personalInformationContainer = root.findViewById(R.id.personalInformationContainer)
        contactInformationContainer = root.findViewById(R.id.contactInformationContainer)
        profileContactInfoActionIndicator = root.findViewById(R.id.profileContactInfoActionIndicator)
        taxDeductionsContainer = root.findViewById(R.id.taxDeductionsContainer)
        logoutButton = root.findViewById(R.id.profile_logout_button)
        mechanicImageView = root.findViewById(R.id.mechanicImageView)
        personalInformationActionIndicator = root.findViewById(R.id.personalInformationActionIndicator)

        editTextView = root.findViewById(R.id.editTextView)

        personalInformationActionIndicator.visibility = View.INVISIBLE

        viewModel.mechanic.observe(viewLifecycleOwner) {
            requireActivity().runOnUiThread {
                if (it == null) { return@runOnUiThread }
                val newIsActive = it.isActive
                if (allowNewAppointmentsSwitch.isChecked != newIsActive) {
                    allowNewAppointmentsSwitch.isChecked = newIsActive
                }
                
                val newCharge = it.chargeForTravel
                if (chargeForTravelSwitch.isChecked != newCharge) {
                    chargeForTravelSwitch.isChecked = newCharge
                }
                
                ratingsTextView.text = ratingsText(it)
                servicesCompletedTextView.text = servicesCompletedText(it)
                ratingBar.rating = it.averageRating?.toFloat() ?: 0.0F
                mechanicImageView.mechanicId = it.id
            }
        }

        viewModel.verification.observe(viewLifecycleOwner) {
            requireActivity().runOnUiThread {
                print("can show red dots correctly")
                personalInformationActionIndicator.visibility = if (it?.isAnyPersonalInformationDue() == true) View.VISIBLE else View.INVISIBLE 
            }
        }

        viewModel.mechanicUser.observe(viewLifecycleOwner) {
            requireActivity().runOnUiThread {
                print("can show red dots correctly")
                it?.let {
                    mechanicNameTextView.text = it.displayName() ?: "--"
                    profileContactInfoActionIndicator.visibility = if (it.isEmailVerified == true) View.INVISIBLE else View.VISIBLE
                }
            }
        }

        editTextView.setOnClickListener { 
            showImagePicker()
        }
        
        logoutButton.setOnClickListener {
            logout()
        }

        allReviewsTextView.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_mechanic_profile_to_navigation_reviewListFragment)
        }

        allowNewAppointmentsSwitch.setOnCheckedChangeListener { compoundButton, b ->
            val updateMechanic = UpdateMechanic(isActive = b)
            viewModel.updateMechanic(updateMechanic, requireContext()) {
                print("updated mechanic")
            }
            print("checked changed")
        }
        chargeForTravelSwitch.setOnCheckedChangeListener { compoundButton, b ->
            print("checked changed")
            val updateMechanic = UpdateMechanic(chargeForTravel = b)
            viewModel.updateMechanic(updateMechanic, requireContext()) {
                print("updated mechanic")
            }
            print("checked changed")
        }
        setServiceRegionContainer.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_mechanic_profile_to_navigation_region)
        }
        setHoursContainer.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_mechanic_profile_to_navigation_availability)
        }
        setPricingContainer.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_mechanic_profile_to_navigation_setPricing)
        }
        personalInformationContainer.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_mechanic_profile_to_navigation_personalInformation)
        }
        contactInformationContainer.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_mechanic_profile_to_navigation_contact)
        }
        taxDeductionsContainer.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_mechanic_profile_to_navigation_tax_deductions)
        }
        
        return root
    }
    
    private fun showImagePicker() {
        ImagePicker.create(this)
            .single()
            .returnMode(ReturnMode.ALL)
            .showCamera(true)
            .theme(R.style.ImagePickerTheme)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            val image = ImagePicker.getFirstImageOrNull(data)
            val pathUtilPath = PathUtil.getPath(requireContext(), image.uri)
            Glide.with(mechanicImageView.imageView)
                .load(image.uri)
                .into(mechanicImageView.imageView)

            viewModel.uploadProfilePicture(pathUtilPath ?: "", requireContext()) {
                if (it == null) {
                    requireActivity().runOnUiThread {
                        showSuccessfulProfilePictureToast()
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun showSuccessfulProfilePictureToast() {
        Toast.makeText(
            requireContext(),
            "Car Swaddle successfully saved your profile picture.",
            Toast.LENGTH_SHORT
        ).show()
    }
    
    private fun ratingsText(mechanic: Mechanic): SpannableStringBuilder {
        var roundedValue = "-"
        if (mechanic.averageRating != null) {
            roundedValue = (Math.round(
                (mechanic.averageRating?.toFloat() ?: 0.0F) * 10
            ) / 10.0).toString()
        }
        val ssb = SpannableStringBuilder().append(
            roundedValue,
            StyleSpan(Typeface.BOLD),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
            .append(" avg from ")
            .append(
                mechanic.numberOfRatings?.toString() ?: "-",
                StyleSpan(Typeface.BOLD),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            .append(" ratings")
        return ssb
    }
    
    private fun servicesCompletedText(mechanic: Mechanic): SpannableStringBuilder {
        val ssb = SpannableStringBuilder().append(
            mechanic.autoServicesProvided?.toString() ?: "-",
            StyleSpan(Typeface.BOLD),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
            .append(" services completed")
        return ssb
    }

    private fun logout() {
        Authentication(requireContext()).logout { error, response ->
            val intent = Intent(activity, AuthActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }

}