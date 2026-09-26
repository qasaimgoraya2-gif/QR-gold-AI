package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.ScanRecordEntity
import com.example.security.RiskLevel
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.components.GlassCard
import com.example.ui.components.RiskBadge
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceElevated
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SecurityHighRisk
import com.example.ui.theme.SecuritySafe
import com.example.ui.theme.SecuritySuspicious
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val allScans by viewModel.allScans.collectAsState()
    val isUrdu by viewModel.isUrdu.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ALL") } // ALL, SAFE, SUSPICIOUS, HIGH_RISK, FAVORITES
    var showClearConfirm by remember { mutableStateOf(false) }

    val filteredScans = remember(allScans, searchQuery, selectedFilter) {
        allScans.filter { scan ->
            val matchesQuery = searchQuery.isBlank() ||
                    scan.title.contains(searchQuery, ignoreCase = true) ||
                    scan.rawContent.contains(searchQuery, ignoreCase = true)

            val matchesFilter = when (selectedFilter) {
                "SAFE" -> scan.riskLevel == "SAFE"
                "SUSPICIOUS" -> scan.riskLevel == "SUSPICIOUS"
                "HIGH_RISK" -> scan.riskLevel == "HIGH_RISK"
                "FAVORITES" -> scan.isFavorite
                else -> true
            }

            matchesQuery && matchesFilter
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.navigateTo(AppScreen.HOME) },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(CyberSurface)
                            .border(1.dp, CyberBorder, CircleShape)
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (isUrdu) "اسکین کی تاریخ" else "Scan History & Reports",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                if (allScans.isNotEmpty()) {
                    IconButton(onClick = { showClearConfirm = true }) {
                        Icon(imageVector = Icons.Default.DeleteSweep, contentDescription = "Clear All", tint = SecurityHighRisk)
                    }
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(if (isUrdu) "یو آر ایل یا نام تلاش کریں..." else "Search scans, URLs, or notes...", color = TextMuted, fontSize = 13.sp) },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = NeonCyan) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = CyberBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = CyberSurfaceElevated,
                    unfocusedContainerColor = CyberSurfaceElevated
                ),
                singleLine = true
            )
        }

        // Filter Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val filters = listOf(
                    "ALL" to "All (${allScans.size})",
                    "SAFE" to "Safe",
                    "SUSPICIOUS" to "Suspicious",
                    "HIGH_RISK" to "High Risk",
                    "FAVORITES" to "Favorites"
                )
                items(filters) { (key, label) ->
                    FilterChip(
                        selected = selectedFilter == key,
                        onClick = { selectedFilter = key },
                        label = { Text(label, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NeonCyan.copy(alpha = 0.2f),
                            selectedLabelColor = NeonCyan,
                            containerColor = CyberSurface,
                            labelColor = TextSecondary
                        )
                    )
                }
            }
        }

        // Scan Records List
        if (filteredScans.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.History, contentDescription = null, tint = TextMuted, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (isUrdu) "کوئی ریکارڈ نہیں ملا" else "No matching scan records found",
                            color = TextSecondary,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        } else {
            items(filteredScans, key = { it.id }) { scan ->
                HistoryRecordCard(
                    scan = scan,
                    isUrdu = isUrdu,
                    onInspect = {
                        viewModel.onQrScanned(scan.rawContent, scan.source)
                    },
                    onToggleFavorite = {
                        viewModel.toggleFavorite(scan.id, scan.isFavorite)
                    },
                    onDelete = {
                        viewModel.deleteScan(scan.id)
                    },
                    onShareReport = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, "Security Report: ${scan.title}")
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "=== QR GUARD AI SECURITY REPORT ===\nDate: ${SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(scan.timestamp))}\nTarget: ${scan.rawContent}\nRisk Level: ${scan.riskLevel}\nScore: ${scan.riskScore}/100\nExplanation: ${scan.explanation}\nRecommended Action: ${scan.recommendedAction}"
                            )
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Export Security Report"))
                    }
                )
            }
        }
    }

    if (showClearConfirm) {
        AlertDialog(
            onDismissRequest = { showClearConfirm = false },
            title = { Text("Clear All History?", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to permanently delete all scan records and security reports?", color = TextSecondary) },
            confirmButton = {
                Button(
                    onClick = {
                        showClearConfirm = false
                        viewModel.clearAllScans()
                        Toast.makeText(context, "History cleared", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SecurityHighRisk)
                ) {
                    Text("Delete All", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirm = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = CyberSurface
        )
    }
}

@Composable
fun HistoryRecordCard(
    scan: ScanRecordEntity,
    isUrdu: Boolean,
    onInspect: () -> Unit,
    onToggleFavorite: () -> Unit,
    onDelete: () -> Unit,
    onShareReport: () -> Unit
) {
    val risk = try { RiskLevel.valueOf(scan.riskLevel) } catch (e: Exception) { RiskLevel.UNKNOWN }
    val dateStr = SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault()).format(Date(scan.timestamp))

    GlassCard(
        borderColor = CyberBorder,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onInspect)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = scan.title.ifBlank { scan.qrType },
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f),
                    maxLines = 1
                )
                RiskBadge(riskLevel = risk, isUrdu = isUrdu)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = scan.rawContent,
                fontSize = 12.sp,
                color = TextSecondary,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dateStr,
                    fontSize = 11.sp,
                    color = TextMuted
                )

                Row {
                    IconButton(onClick = onToggleFavorite, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = if (scan.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (scan.isFavorite) SecurityHighRisk else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(onClick = onShareReport, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share Report",
                            tint = NeonCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
