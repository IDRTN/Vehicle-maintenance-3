package com.vehiclemaintenance.app.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "service_logs")
data class ServiceLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val vehicleId: Int,
    val serviceType: String, // e.g., "Oil Change", "Brake Pad Replacement"
    val dateLogged: String,
    val notes: String? = null
)
