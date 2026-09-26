package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_records")
data class ScanRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val rawContent: String,
    val qrType: String, // URL, TEXT, WIFI, CONTACT, EMAIL, PHONE, SMS, LOCATION, CALENDAR, PAYMENT, OTHER
    val title: String,
    val riskLevel: String, // SAFE, SUSPICIOUS, HIGH_RISK, UNKNOWN
    val riskScore: Int, // 0 - 100 (where 0 is completely safe, 100 is critical danger)
    val detectedSignals: String, // JSON or formatted list of signals
    val explanation: String,
    val recommendedAction: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val notes: String = "",
    val tags: String = "",
    val source: String = "CAMERA" // CAMERA, GALLERY, SCREENSHOT, WEBSITE_CHECK, MEDIA_ANALYZER
)
