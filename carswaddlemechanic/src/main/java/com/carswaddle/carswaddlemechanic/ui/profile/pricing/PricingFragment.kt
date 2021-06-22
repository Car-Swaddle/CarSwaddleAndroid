package com.carswaddle.carswaddlemechanic.ui.profile.pricing

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.carswaddle.carswaddleandroid.services.serviceModels.UpdateOilChangePricing
import com.carswaddle.carswaddleandroid.ui.view.ProgressButton
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.ui.LabeledEditText
import java.text.NumberFormat

class PricingFragment : Fragment() {

    private lateinit var viewModel: PricingViewModel
    private lateinit var savebutton: ProgressButton
    
    private lateinit var conventionalLabeledEditText: LabeledEditText
    private lateinit var blendLabeledEditText: LabeledEditText
    private lateinit var syntheticLabeledEditText: LabeledEditText
    private lateinit var highMileageLabeledEditText: LabeledEditText
    
    private var updateOilChangePricing: UpdateOilChangePricing? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(PricingViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_mechanic_pricing, container, false)

        savebutton = root.findViewById(R.id.savePricingButton)
        conventionalLabeledEditText = root.findViewById(R.id.conventionalLabeledEditText)
        blendLabeledEditText = root.findViewById(R.id.blendLabeledEditText)
        syntheticLabeledEditText = root.findViewById(R.id.syntheticLabeledEditText)
        highMileageLabeledEditText = root.findViewById(R.id.highMileageLabeledEditText)

        val inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
        
        conventionalLabeledEditText.editText.inputType = inputType
        blendLabeledEditText.editText.inputType = inputType
        syntheticLabeledEditText.editText.inputType = inputType
        highMileageLabeledEditText.editText.inputType = inputType
        
        viewModel.oilChangePricing.observe(viewLifecycleOwner) {
            if (it != null) {
                updateOilChangePricing = it.updatePricing()
                requireActivity().runOnUiThread {
                    updateNumbers()
                }
            }
        }
        
        savebutton.button.setOnClickListener {
            savebutton.isLoading = true
            
            val u = UpdateOilChangePricing(
                editTextStringToCents(conventionalLabeledEditText.editText),
                editTextStringToCents(blendLabeledEditText.editText),
                editTextStringToCents(syntheticLabeledEditText.editText),
                editTextStringToCents(highMileageLabeledEditText.editText)
            )
            if (u == null) {
                return@setOnClickListener
            }
            
            viewModel.updateOilChangePricing(u, requireContext()) { error, oilChangePricing ->
                requireActivity().runOnUiThread {
                    savebutton.isLoading = false
                    if (error == null) {
                        findNavController().popBackStack()
                        Toast.makeText(
                            requireContext(),
                            "Car Swaddle successfully saved your pricing.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        return root
    }
    
    private fun updateNumbers() {
        val c = updateOilChangePricing?.conventional?.toLong()
        if (c != null) {
            conventionalLabeledEditText.editText.setText(numberFormatter.format(c / 100.0) ?: "")
        }

        val b = updateOilChangePricing?.blend?.toLong()
        if (b != null) {
            blendLabeledEditText.editText.setText(numberFormatter.format(b / 100.0) ?: "")
        }

        val s = updateOilChangePricing?.synthetic?.toLong()
        if (s != null) {
            syntheticLabeledEditText.editText.setText(numberFormatter.format(s / 100.0) ?: "")
        }

        val h = updateOilChangePricing?.conventional?.toLong()
        if (h != null) {
            highMileageLabeledEditText.editText.setText(numberFormatter.format(h / 100.0) ?: "")
        }
    }
    
    private fun editTextStringToCents(editText: EditText): Int {
        val v = editText.text?.toString()?.toDouble() ?: 0.0
        return (v * 100.0).toInt()
    }
    

    companion object {

        private val numberFormatter: NumberFormat by lazy {
            val f = NumberFormat.getInstance()
            f.minimumFractionDigits = 2
            f.maximumFractionDigits = 2
            return@lazy f
        }
        
    }
    
}