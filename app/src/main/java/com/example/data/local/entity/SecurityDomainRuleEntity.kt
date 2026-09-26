package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "domain_rules")
data class SecurityDomainRuleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val domain: String,
    val status: String, // TRUSTED or BLOCKED
    val addedAt: Long = System.currentTimeMillis(),
    val notes: String = ""
)
