package com.vehiclemaintenance.app

import android.content.Context
import android.content.Intent
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
import com.vehiclemaintenance.app.data.models.ServiceLog
import com.vehiclemaintenance.app.data.models.Vehicle
import com.vehiclemaintenance.app.data.repository.VehicleRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class VehicleDetailActivity : AppCompatActivity() {

    private lateinit var vehicleNameText: TextView
    private lateinit var odometerText: TextView
    private lateinit var emptyLogsText: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var addServiceButton: Button
    private lateinit var adapter: ServiceLogAdapter
    private lateinit var repository: VehicleRepository
    private var vehicleId: Int = 0

    companion object {
        private const val EXTRA_VEHICLE_ID = "vehicle_id"

        fun start(context: Context, vehicleId: Int) {
            val intent = Intent(context, VehicleDetailActivity::class.java)
            intent.putExtra(EXTRA_VEHICLE_ID, vehicleId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_detail)

        vehicleId = intent.getIntExtra(EXTRA_VEHICLE_ID, 0)
        if (vehicleId == 0) {
            finish()
            return
        }

        vehicleNameText = findViewById(R.id.text_vehicle_name)
        odometerText = findViewById(R.id.text_odometer)
        emptyLogsText = findViewById(R.id.text_empty_logs)
        recyclerView = findViewById(R.id.recycler_service_logs)
        addServiceButton = findViewById(R.id.btn_add_service)

        val db = AppDatabase.getDatabase(this)
        repository = VehicleRepository(db)

        adapter = ServiceLogAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        loadVehicle()
        loadServiceLogs()

        addServiceButton.setOnClickListener {
            showAddServiceDialog()
        }

        // Back navigation
        findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
            .setNavigationOnClickListener { finish() }
    }

    private fun loadVehicle() {
        lifecycleScope.launch {
            val vehicle = repository.getVehicleById(vehicleId)
            vehicle?.let { v ->
                vehicleNameText.text = "${v.year} ${v.make} ${v.model}".trim()
                odometerText.text = v.currentOdometer?.let { "Odometer: $it mi" }
                    ?: "No odometer recorded"
            }
        }
    }

    private fun loadServiceLogs() {
        lifecycleScope.launch {
            repository.getLogsForVehicle(vehicleId).collect { logs ->
                adapter.updateData(logs)
                if (logs.isEmpty()) {
                    recyclerView.visibility = RecyclerView.GONE
                    emptyLogsText.visibility = TextView.VISIBLE
                } else {
                    recyclerView.visibility = RecyclerView.VISIBLE
                    emptyLogsText.visibility = TextView.GONE
                }
            }
        }
    }

    private fun showAddServiceDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_service, null)
        val inputType = dialogView.findViewById<TextInputEditText>(R.id.input_service_type)
        val inputDate = dialogView.findViewById<TextInputEditText>(R.id.input_date)
        val inputNotes = dialogView.findViewById<TextInputEditText>(R.id.input_notes)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        inputDate.setText(dateFormat.format(Date()))

        AlertDialog.Builder(this)
            .setTitle("Add Service Record")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val serviceType = inputType.text.toString()
                val dateLogged = inputDate.text.toString()
                val notes = inputNotes.text.toString()

                if (serviceType.isNotBlank()) {
                    lifecycleScope.launch {
                        repository.insertLog(
                            ServiceLog(
                                vehicleId = vehicleId,
                                serviceType = serviceType,
                                dateLogged = dateLogged,
                                notes = notes.ifBlank { null }
                            )
                        )
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
