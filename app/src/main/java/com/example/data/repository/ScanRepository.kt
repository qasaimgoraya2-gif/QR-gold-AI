package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.entity.GeneratedQrEntity
import com.example.data.local.entity.ScanRecordEntity
import com.example.data.local.entity.SecurityDomainRuleEntity
import kotlinx.coroutines.flow.Flow

class ScanRepository(private val database: AppDatabase) {
    private val scanDao = database.scanRecordDao()
    private val qrDao = database.generatedQrDao()
    private val ruleDao = database.securityDomainRuleDao()

    val allScans: Flow<List<ScanRecordEntity>> = scanDao.getAllScans()
    val favoriteScans: Flow<List<ScanRecordEntity>> = scanDao.getFavoriteScans()
    val allGeneratedQrs: Flow<List<GeneratedQrEntity>> = qrDao.getAllGeneratedQrs()
    val domainRules: Flow<List<SecurityDomainRuleEntity>> = ruleDao.getAllRules()

    val totalScansCount: Flow<Int> = scanDao.getCountTotal()
    val safeScansCount: Flow<Int> = scanDao.getCountSafe()
    val suspiciousScansCount: Flow<Int> = scanDao.getCountSuspicious()
    val highRiskScansCount: Flow<Int> = scanDao.getCountHighRisk()
    val generatedCount: Flow<Int> = qrDao.getCountGenerated()

    fun getRecentScans(limit: Int): Flow<List<ScanRecordEntity>> = scanDao.getRecentScans(limit)

    suspend fun getScanById(id: Long): ScanRecordEntity? = scanDao.getScanById(id)

    suspend fun saveScan(scan: ScanRecordEntity): Long = scanDao.insertScan(scan)

    suspend fun toggleFavorite(id: Long, isFavorite: Boolean) = scanDao.setFavorite(id, isFavorite)

    suspend fun updateNotesAndTags(id: Long, notes: String, tags: String) =
        scanDao.updateNotesAndTags(id, notes, tags)

    suspend fun deleteScan(id: Long) = scanDao.deleteScanById(id)

    suspend fun clearHistory() {
        scanDao.clearAllScans()
    }

    suspend fun saveGeneratedQr(qr: GeneratedQrEntity): Long = qrDao.insertGeneratedQr(qr)

    suspend fun deleteGeneratedQr(id: Long) = qrDao.deleteById(id)

    suspend fun getRuleForDomain(domain: String): SecurityDomainRuleEntity? =
        ruleDao.getRuleForDomain(domain)

    suspend fun addDomainRule(domain: String, status: String, notes: String = ""): Long =
        ruleDao.insertRule(SecurityDomainRuleEntity(domain = domain.lowercase().trim(), status = status, notes = notes))

    suspend fun removeDomainRule(domain: String) = ruleDao.deleteByDomain(domain.lowercase().trim())
}
