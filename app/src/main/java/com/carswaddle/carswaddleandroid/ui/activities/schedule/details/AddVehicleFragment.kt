package com.carswaddle.carswaddleandroid.ui.activities.schedule.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.ui.activities.profile.LabeledEditText
import com.google.android.gms.maps.OnMapReadyCallback

class AddVehicleFragment() : Fragment() {
    
    private lateinit var saveButton: Button

    private lateinit var addVehicleViewModel: AddVehicleViewModel
    
    var didAddVehicle: (vehicleId: String) -> Unit = { v -> }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_add_vehicle, container, false)

        addVehicleViewModel = ViewModelProviders.of(this).get(AddVehicleViewModel::class.java)
        
        val vehicleName: LabeledEditText  = root.findViewById(R.id.vehicleNameLabeledTextView)
        val licensePlate: LabeledEditText  = root.findViewById(R.id.licensePlateLabeledTextView)
        
        vehicleName.editTextValue = ""
        licensePlate.editTextValue = ""
        
        vehicleName.labelText = getString(R.string.vehicle_name)
        licensePlate.labelText = getString(R.string.license_plate)
        
        val spinner: Spinner = root.findViewById(R.id.spinner) 
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.us_states,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            
            // Apply the adapter to the spinner
            spinner.adapter = adapter

            val selection = adapter.getPosition("Utah")
            if (selection != 0) {
                spinner.setSelection(selection)
            }
        }
        
        saveButton = root.findViewById(R.id.saveButton)
        
        saveButton.setOnClickListener {
            val name: String? = vehicleName.editTextValue
            val licensePlate: String? = licensePlate.editTextValue
            val state: String? = spinner.selectedItem.toString()
            if (name == null || licensePlate == null || state == null) {
                return@setOnClickListener
            }
            addVehicleViewModel.createVehicle(name, licensePlate, state) {
                activity?.runOnUiThread {
                    if (it != null) {
                        activity?.supportFragmentManager?.popBackStack()
                        didAddVehicle(it)
                    } else {
                        // TODO: Show error 
                    }
                }
                
            }
        }
        
        return root
    }
    
}