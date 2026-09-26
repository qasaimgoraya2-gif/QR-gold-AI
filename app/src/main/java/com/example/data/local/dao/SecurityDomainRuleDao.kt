package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.SecurityDomainRuleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SecurityDomainRuleDao {
    @Query("SELECT * FROM domain_rules ORDER BY addedAt DESC")
    fun getAllRules(): Flow<List<SecurityDomainRuleEntity>>

    @Query("SELECT * FROM domain_rules WHERE domain = :domain LIMIT 1")
    suspend fun getRuleForDomain(domain: String): SecurityDomainRuleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRule(rule: SecurityDomainRuleEntity): Long

    @Query("DELETE FROM domain_rules WHERE id = :id")
    suspend fun deleteRule(id: Long)

    @Query("DELETE FROM domain_rules WHERE domain = :domain")
    suspend fun deleteByDomain(domain: String)
}
