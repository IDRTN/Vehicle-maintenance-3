package com.vehiclemaintenance.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vehiclemaintenance.app.data.models.Vehicle

class VehicleAdapter(
    private var vehicles: List<Vehicle>,
    private val onVehicleClick: (Vehicle) -> Unit
) : RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>() {

    fun updateData(newVehicles: List<Vehicle>) {
        vehicles = newVehicles
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vehicle, parent, false)
        return VehicleViewHolder(view)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val vehicle = vehicles[position]
        holder.bind(vehicle, onVehicleClick)
    }

    override fun getItemCount() = vehicles.size

    class VehicleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameText: TextView = itemView.findViewById(R.id.text_vehicle_name)
        private val odoText: TextView = itemView.findViewById(R.id.text_vehicle_odometer)

        fun bind(vehicle: Vehicle, onClick: (Vehicle) -> Unit) {
            nameText.text = "${vehicle.year} ${vehicle.make} ${vehicle.model}".trim()
            odoText.text = vehicle.currentOdometer?.let { "Odometer: $it mi" } ?: "No odometer recorded"
            itemView.setOnClickListener { onClick(vehicle) }
        }
    }
}
