package com.vehiclemaintenance.app.data.database

import androidx.room.*
import com.vehiclemaintenance.app.data.models.ServiceLog
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceLogDao {
    @Query("SELECT * FROM service_logs WHERE vehicleId = :vehicleId ORDER BY dateLogged DESC")
    fun getLogsForVehicle(vehicleId: Int): Flow<List<ServiceLog>>

    @Query("SELECT * FROM service_logs ORDER BY dateLogged DESC")
    fun getAllLogs(): Flow<List<ServiceLog>>

    @Query("SELECT * FROM service_logs WHERE id = :id")
    suspend fun getLogById(id: Int): ServiceLog?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: ServiceLog): Long

    @Update
    suspend fun update(log: ServiceLog)

    @Delete
    suspend fun delete(log: ServiceLog)
}
