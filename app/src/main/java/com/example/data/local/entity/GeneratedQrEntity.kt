package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "generated_qrs")
data class GeneratedQrEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val qrType: String,
    val foregroundColor: Long = 0xFF00E5FF, // Cyan default
    val backgroundColor: Long = 0xFF0A0F1D, // Dark navy default
    val hasGradient: Boolean = true,
    val gradientColor: Long = 0xFF7C4DFF, // Neon purple
    val frameText: String = "SCAN ME",
    val logoType: String = "SHIELD", // NONE, SHIELD, LINK, WIFI, PHONE, SECURE
    val roundedStyle: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)
