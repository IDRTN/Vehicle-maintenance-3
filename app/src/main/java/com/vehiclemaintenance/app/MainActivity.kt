package com.vehiclemaintenance.app

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.vehiclemaintenance.app.data.database.AppDatabase
import com.vehiclemaintenance.app.data.models.Vehicle
import com.vehiclemaintenance.app.data.repository.VehicleRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var emptyStateTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var addVehicleButton: Button
    private lateinit var adapter: VehicleAdapter
    private lateinit var repository: VehicleRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emptyStateTextView = findViewById(R.id.text_empty_state)
        recyclerView = findViewById(R.id.recycler_services)
        addVehicleButton = findViewById(R.id.btn_add_vehicle)

        val db = AppDatabase.getDatabase(this)
        repository = VehicleRepository(db)

        adapter = VehicleAdapter(emptyList()) { vehicle ->
            VehicleDetailActivity.start(this, vehicle.id)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        loadVehicles()

        addVehicleButton.setOnClickListener {
            showAddVehicleDialog()
        }
    }

    private fun loadVehicles() {
        lifecycleScope.launch {
            repository.getAllVehicles().collect { vehicles ->
                adapter.updateData(vehicles)
                updateVisibility(vehicles.isEmpty())
            }
        }
    }

    private fun updateVisibility(isEmpty: Boolean) {
        if (isEmpty) {
            recyclerView.visibility = RecyclerView.GONE
            emptyStateTextView.visibility = TextView.VISIBLE
        } else {
            recyclerView.visibility = RecyclerView.VISIBLE
            emptyStateTextView.visibility = TextView.GONE
        }
    }

    private fun showAddVehicleDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_vehicle, null)
        val inputMake = dialogView.findViewById<TextInputEditText>(R.id.input_make)
        val inputModel = dialogView.findViewById<TextInputEditText>(R.id.input_model)
        val inputYear = dialogView.findViewById<TextInputEditText>(R.id.input_year)
        val inputOdometer = dialogView.findViewById<TextInputEditText>(R.id.input_odometer)

        AlertDialog.Builder(this)
            .setTitle("Add Vehicle")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val make = inputMake.text.toString()
                val model = inputModel.text.toString()
                val year = inputYear.text.toString().toIntOrNull() ?: 2026
                val odoInput = inputOdometer.text.toString()
                val finalOdometer: Int? = if (odoInput.isNotEmpty()) odoInput.toInt() else null

                if (make.isNotBlank() && model.isNotBlank()) {
                    lifecycleScope.launch {
                        repository.insertVehicle(
                            Vehicle(
                                make = make,
                                model = model,
                                year = year,
                                currentOdometer = finalOdometer
                            )
                        )
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
