package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.GeneratedQrEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GeneratedQrDao {
    @Query("SELECT * FROM generated_qrs ORDER BY timestamp DESC")
    fun getAllGeneratedQrs(): Flow<List<GeneratedQrEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeneratedQr(qr: GeneratedQrEntity): Long

    @Query("DELETE FROM generated_qrs WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM generated_qrs")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM generated_qrs")
    fun getCountGenerated(): Flow<Int>
}
