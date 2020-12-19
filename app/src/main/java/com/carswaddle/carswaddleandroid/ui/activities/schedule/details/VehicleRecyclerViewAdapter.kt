package com.carswaddle.carswaddleandroid.ui.activities.schedule.details

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.vehicle.Vehicle
import com.carswaddle.carswaddleandroid.ui.activities.schedule.MyMechanicRecyclerViewAdapter

class VehicleRecyclerViewAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class ViewType(val value: Int) {
        VEHICLE(0),
        ADD_VEHICLE(1);
        companion object {
            fun fromInt(value: Int) = ViewType.values().first { it.value == value }
        }
    }

    var selectedPosition: Int = 1

    val selectedVehicle: Vehicle? get() {
        val index = selectedPosition - 1
        if (index < 0 || index >= vehicles.size) {
            return null
        }
        return vehicles[index]
    }

    var addVehicleClick: (() -> Unit) = {}
    
    var vehicles: List<Vehicle> = listOf()
    set(newValue) {
        field = newValue
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        if (itemCount <= 2) {
            return VIEW_TYPE_PADDING
        }
        return if (position == 0 || position == itemCount - 1) {
            VIEW_TYPE_PADDING
        } else if(position == itemCount - 2) {
            VIEW_TYPE_ADD_VEHICLE
        } else {
            VIEW_TYPE_VEHICLE
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_PADDING) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.empty_view, parent, false)
            return EmptyViewHolder(view)
        } else if (viewType == VIEW_TYPE_ADD_VEHICLE) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.add_vehicle_item, parent, false)
            
            view.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    addVehicleClick()
                }
            })
            return AddVehicleViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.vehicle_item, parent, false)
            return VehicleViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is VehicleViewHolder) {
            val vehicle = vehicles[position-1]
            holder.vehicleNameTextView.text = vehicle.name
            holder.licensePlateTextView.text = vehicle.licensePlate
            holder.isSelectedVehicle = position == selectedPosition
        } else if (holder is AddVehicleViewHolder) {
            // Do nothing already localized in the xml
            Log.w("vehicle", "add vehicle")
        } else if (holder is EmptyViewHolder) {
            // Do nothing
            Log.w("vehicle", "empty view holder")
        }
    }

    override fun getItemCount(): Int {
        return vehicles.size + 1 + 2
    } // 1 for Add vehicle, 2 for padding left and right.

    inner class VehicleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val vehicleNameTextView: TextView = view.findViewById(R.id.vehicleName)
        val licensePlateTextView: TextView = view.findViewById(R.id.licensePlate)
        val rootView: View = view.findViewById(R.id.rootView)
        var isSelectedVehicle: Boolean = false
            set(newValue) {
                field = newValue
                if (isSelectedVehicle) {
                    rootView.setBackgroundResource(R.drawable.large_selected_border)
                } else  {
                    rootView.setBackgroundResource(R.drawable.large_unselected_border)
                }
            }
    }

    inner class AddVehicleViewHolder(view: View) : RecyclerView.ViewHolder(view) {}

    companion object {
        private const val VIEW_TYPE_PADDING = 1
        private const val VIEW_TYPE_VEHICLE = 2
        private const val VIEW_TYPE_ADD_VEHICLE = 3
    }
    
}


class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view) {}