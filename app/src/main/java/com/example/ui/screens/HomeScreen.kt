package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.ScanRecordEntity
import com.example.security.RiskLevel
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.components.AiOrb
import com.example.ui.components.GlassCard
import com.example.ui.components.RiskBadge
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberBorderGlowing
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceElevated
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.ElectricBlue
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
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val totalScans by viewModel.totalScansCount.collectAsState()
    val safeScans by viewModel.safeScansCount.collectAsState()
    val suspiciousScans by viewModel.suspiciousScansCount.collectAsState()
    val highRiskScans by viewModel.highRiskScansCount.collectAsState()
    val recentScans by viewModel.recentScans.collectAsState()
    val isUrdu by viewModel.isUrdu.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // App Top Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "QR Guard",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "AI",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = NeonCyan
                        )
                    }
                    Text(
                        text = if (isUrdu) "اسکین کریں۔ تصدیق کریں۔ سمجھیں۔" else "Scan. Verify. Understand.",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Language toggle pill
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(CyberSurfaceVariant)
                        .border(1.dp, CyberBorder, RoundedCornerShape(20.dp))
                        .clickable { viewModel.isUrdu.value = !isUrdu }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Language",
                        tint = NeonCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isUrdu) "اردو" else "EN",
                        color = NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Hero Scanner Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(
                        BorderStroke(
                            1.5.dp,
                            Brush.horizontalGradient(listOf(NeonCyan, NeonPurple))
                        ),
                        RoundedCornerShape(24.dp)
                    )
                    .clickable { viewModel.navigateTo(AppScreen.SCAN) }
                    .testTag("hero_scan_button"),
                colors = CardDefaults.cardColors(
                    containerColor = CyberSurface
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    CyberSurfaceVariant,
                                    CyberSurface
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(SecuritySafe)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isUrdu) "حفاظتی اسکینر فعال ہے" else "Cyber Guard Active",
                                    fontSize = 12.sp,
                                    color = SecuritySafe,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (isUrdu) "کیو آر کوڈ اسکین کریں" else "Scan QR Code",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isUrdu) "کیمرہ، گیلری اور اسکرین شاٹ کا تجزیہ" else "Camera, gallery & screenshot inspection",
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                        }

                        // Animated AI Orb inside hero
                        AiOrb(size = 72.dp)
                    }
                }
            }
        }

        // Quick Actions Grid
        item {
            Text(
                text = if (isUrdu) "فوری اقدامات" else "Quick Actions",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionCard(
                    title = if (isUrdu) "کیو آر بنائیں" else "Generate QR",
                    subtitle = if (isUrdu) "ویب سائٹ اور دیگر" else "Web & Custom",
                    icon = Icons.Default.QrCode,
                    iconColor = NeonCyan,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.navigateTo(AppScreen.GENERATE) }
                )
                QuickActionCard(
                    title = if (isUrdu) "ویب سائٹ چیک" else "Check Website",
                    subtitle = if (isUrdu) "یو آر ایل تجزیہ" else "URL Security",
                    icon = Icons.Default.Security,
                    iconColor = NeonPurple,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.navigateTo(AppScreen.SECURITY) }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionCard(
                    title = if (isUrdu) "ویڈیو تجزیہ" else "Analyze Video",
                    subtitle = if (isUrdu) "جعلی میڈیا کی جانچ" else "Deepfake Signals",
                    icon = Icons.Default.Videocam,
                    iconColor = ElectricBlue,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.navigateTo(AppScreen.MEDIA_ANALYZER) }
                )
                QuickActionCard(
                    title = if (isUrdu) "اسکرین شاٹ" else "Analyze Image",
                    subtitle = if (isUrdu) "فراڈ پیغامات" else "Visual Inspection",
                    icon = Icons.Default.Image,
                    iconColor = SecuritySuspicious,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.navigateTo(AppScreen.MEDIA_ANALYZER) }
                )
            }
        }

        // AI Assistant Callout
        item {
            GlassCard(
                borderColor = NeonCyan.copy(alpha = 0.35f),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.navigateTo(AppScreen.AI_ASSISTANT) }
                    .testTag("home_ai_assistant_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AiOrb(size = 46.dp)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isUrdu) "اے آئی سیکیورٹی معاون" else "AI Security Assistant",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isUrdu) "کسی بھی کیو آر، مشکوک لنک یا تصویر کے بارے میں پوچھیں۔" else "Ask AI about any QR code, suspicious link, or warning.",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Open AI",
                        tint = NeonCyan
                    )
                }
            }
        }

        // Security Statistics Overview
        item {
            GlassCard(
                borderColor = CyberBorder,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isUrdu) "سیکیورٹی شماریات" else "Security Statistics",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "$totalScans Total Scans",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatPill(
                            label = if (isUrdu) "محفوظ" else "Safe",
                            count = safeScans,
                            color = SecuritySafe
                        )
                        StatPill(
                            label = if (isUrdu) "مشکوک" else "Suspicious",
                            count = suspiciousScans,
                            color = SecuritySuspicious
                        )
                        StatPill(
                            label = if (isUrdu) "خطرناک" else "High Risk",
                            count = highRiskScans,
                            color = SecurityHighRisk
                        )
                    }
                }
            }
        }

        // Recent Activity Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isUrdu) "حالیہ اسکینز" else "Recent Activity",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = if (isUrdu) "سب دیکھیں" else "View All",
                    fontSize = 13.sp,
                    color = NeonCyan,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { viewModel.navigateTo(AppScreen.HISTORY) }
                )
            }
        }

        if (recentScans.isEmpty()) {
            item {
                GlassCard(
                    borderColor = CyberBorder.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (isUrdu) "کوئی حالیہ اسکین نہیں ملا" else "No recent scans yet",
                            color = TextSecondary,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isUrdu) "کیمرے سے یا گیلری سے اسکین کریں" else "Scan your first QR code to inspect its security",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        } else {
            items(recentScans.take(5)) { scan ->
                RecentScanItem(
                    scan = scan,
                    onClick = {
                        viewModel.onQrScanned(scan.rawContent, scan.source)
                    }
                )
            }
        }

        // Scam Awareness Cybersecurity Tip
        item {
            GlassCard(
                borderColor = CyberBorder,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (isUrdu) "روزانہ سائبر سیکیورٹی مشورہ" else "Daily Quishing Awareness",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isUrdu)
                                "کبھی بھی عوامی مقامات (پارکنگ، دکانوں وغیرہ) پر لگے اسٹیکر کیو آر کوڈز پر آنکھیں بند کر کے لاگ ان نہ کریں۔ ہمیشہ ایڈریس بار میں مکمل یو آر ایل دیکھیں۔"
                            else
                                "Quishing (QR Phishing) involves fraudsters pasting rogue stickers over legitimate QR codes. Always inspect the destination domain and SSL before entering login or payment credentials.",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 17.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .border(BorderStroke(1.dp, CyberBorder), RoundedCornerShape(18.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = CyberSurface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun StatPill(
    label: String,
    count: Int,
    color: Color
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.1f))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$count",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = TextSecondary
        )
    }
}

@Composable
fun RecentScanItem(
    scan: ScanRecordEntity,
    onClick: () -> Unit
) {
    val risk = try { RiskLevel.valueOf(scan.riskLevel) } catch (e: Exception) { RiskLevel.UNKNOWN }
    val dateStr = SimpleDateFormat("MMM d, HH:mm", Locale.getDefault()).format(Date(scan.timestamp))

    GlassCard(
        borderColor = CyberBorder,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = scan.title.ifBlank { scan.qrType },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        maxLines = 1
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = scan.rawContent,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = dateStr,
                    fontSize = 10.sp,
                    color = TextMuted
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            RiskBadge(riskLevel = risk)
        }
    }
}
