package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.ScanRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanRecordDao {
    @Query("SELECT * FROM scan_records ORDER BY timestamp DESC")
    fun getAllScans(): Flow<List<ScanRecordEntity>>

    @Query("SELECT * FROM scan_records WHERE id = :id LIMIT 1")
    suspend fun getScanById(id: Long): ScanRecordEntity?

    @Query("SELECT * FROM scan_records WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun getFavoriteScans(): Flow<List<ScanRecordEntity>>

    @Query("SELECT * FROM scan_records WHERE riskLevel = :riskLevel ORDER BY timestamp DESC")
    fun getScansByRisk(riskLevel: String): Flow<List<ScanRecordEntity>>

    @Query("SELECT * FROM scan_records WHERE qrType = :qrType ORDER BY timestamp DESC")
    fun getScansByType(qrType: String): Flow<List<ScanRecordEntity>>

    @Query("SELECT * FROM scan_records ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentScans(limit: Int): Flow<List<ScanRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScan(scan: ScanRecordEntity): Long

    @Update
    suspend fun updateScan(scan: ScanRecordEntity)

    @Query("UPDATE scan_records SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean)

    @Query("UPDATE scan_records SET notes = :notes, tags = :tags WHERE id = :id")
    suspend fun updateNotesAndTags(id: Long, notes: String, tags: String)

    @Query("DELETE FROM scan_records WHERE id = :id")
    suspend fun deleteScanById(id: Long)

    @Query("DELETE FROM scan_records")
    suspend fun clearAllScans()

    @Query("SELECT COUNT(*) FROM scan_records")
    fun getCountTotal(): Flow<Int>

    @Query("SELECT COUNT(*) FROM scan_records WHERE riskLevel = 'SAFE'")
    fun getCountSafe(): Flow<Int>

    @Query("SELECT COUNT(*) FROM scan_records WHERE riskLevel = 'SUSPICIOUS'")
    fun getCountSuspicious(): Flow<Int>

    @Query("SELECT COUNT(*) FROM scan_records WHERE riskLevel = 'HIGH_RISK'")
    fun getCountHighRisk(): Flow<Int>
}
