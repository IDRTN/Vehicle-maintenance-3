package com.vehiclemaintenance.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vehiclemaintenance.app.data.models.ServiceLog
import com.vehiclemaintenance.app.data.models.Vehicle

@Database(
    entities = [Vehicle::class, ServiceLog::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vehicleDao(): VehicleDao
    abstract fun serviceLogDao(): ServiceLogDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vehicle_maintenance_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
