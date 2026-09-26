package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.GeneratedQrDao
import com.example.data.local.dao.ScanRecordDao
import com.example.data.local.dao.SecurityDomainRuleDao
import com.example.data.local.entity.GeneratedQrEntity
import com.example.data.local.entity.ScanRecordEntity
import com.example.data.local.entity.SecurityDomainRuleEntity

@Database(
    entities = [
        ScanRecordEntity::class,
        GeneratedQrEntity::class,
        SecurityDomainRuleEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scanRecordDao(): ScanRecordDao
    abstract fun generatedQrDao(): GeneratedQrDao
    abstract fun securityDomainRuleDao(): SecurityDomainRuleDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "qr_guard_ai.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
