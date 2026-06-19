package com.vehiclemaintenance.app.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class Vehicle(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val make: String,
    val model: String,
    val year: Int,
    val currentOdometer: Int? = null
)
