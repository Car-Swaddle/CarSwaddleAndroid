package com.carswaddle.carswaddlemechanic.ui.profile.personalInfo.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.carswaddle.carswaddleandroid.services.serviceModels.UpdateMechanicAddress
import com.carswaddle.carswaddleandroid.ui.view.ProgressButton
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.ui.LabeledEditText

class StreetAddressFragment : Fragment() {

    private lateinit var viewModel: StreetAddressViewModel

    private lateinit var line1LabeledEditText: LabeledEditText
    private lateinit var line2LabeledEditText: LabeledEditText
    private lateinit var postalCodeLabeledEditText: LabeledEditText
    private lateinit var cityLabeledEditText: LabeledEditText
    private lateinit var stateLabeledEditText: LabeledEditText

    private lateinit var saveButton: ProgressButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(StreetAddressViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_street_address, container, false)

        line1LabeledEditText = root.findViewById(R.id.line1LabeledEditText)
        line2LabeledEditText = root.findViewById(R.id.line2LabeledEditText)
        postalCodeLabeledEditText = root.findViewById(R.id.postalCodeLabeledEditText)
        cityLabeledEditText = root.findViewById(R.id.cityLabeledEditText)
        stateLabeledEditText = root.findViewById(R.id.stateLabeledEditText)
        
        saveButton = root.findViewById(R.id.saveStreetAddressButton)

        saveButton.button.setOnClickListener {
            save()
        }

        return root
    }

    private fun save() {

        val line1 = line1LabeledEditText.editText.text.toString()
        val line2 = line2LabeledEditText.editText.text.toString()
        val postalCode = postalCodeLabeledEditText.editText.text.toString()
        val city = cityLabeledEditText.editText.text.toString()
        val state = stateLabeledEditText.editText.text.toString()
        
        val address = UpdateMechanicAddress(line1, line2, postalCode, city, state, null)
        
        if (line1.isNullOrEmpty() || postalCode.isNullOrEmpty() || city.isNullOrEmpty() || state.isNullOrEmpty()) {
            return
        }
        saveButton.isLoading = true

        viewModel.updateStreetAddress(address, requireContext()) { 
            requireActivity().runOnUiThread {
                if (it == null) {
                    finishSuccessfully()
                }
            }
        }
    }

    private fun finishSuccessfully() {
        saveButton.isLoading = false
        findNavController().popBackStack()
        Toast.makeText(
            requireContext(),
            "Car Swaddle successfully saved your street address.",
            Toast.LENGTH_SHORT
        ).show()
    }

}