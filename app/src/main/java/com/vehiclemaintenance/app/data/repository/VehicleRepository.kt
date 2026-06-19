package com.vehiclemaintenance.app.data.repository

import com.vehiclemaintenance.app.data.database.AppDatabase
import com.vehiclemaintenance.app.data.models.ServiceLog
import com.vehiclemaintenance.app.data.models.Vehicle
import kotlinx.coroutines.flow.Flow

class VehicleRepository(private val db: AppDatabase) {
    private val vehicleDao = db.vehicleDao()
    private val serviceLogDao = db.serviceLogDao()

    fun getAllVehicles(): Flow<List<Vehicle>> = vehicleDao.getAllVehicles()
    suspend fun getVehicleById(id: Int): Vehicle? = vehicleDao.getVehicleById(id)
    suspend fun insertVehicle(vehicle: Vehicle): Int = vehicleDao.insert(vehicle)
    suspend fun updateVehicle(vehicle: Vehicle) = vehicleDao.update(vehicle)
    suspend fun deleteVehicle(vehicle: Vehicle) = vehicleDao.delete(vehicle)
    suspend fun deleteVehicleById(id: Int) = vehicleDao.deleteById(id)

    fun getLogsForVehicle(vehicleId: Int): Flow<List<ServiceLog>> =
        serviceLogDao.getLogsForVehicle(vehicleId)
    suspend fun insertLog(log: ServiceLog): Int = serviceLogDao.insert(log)
    suspend fun deleteLog(log: ServiceLog) = serviceLogDao.delete(log)
}
