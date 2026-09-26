package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.GeminiSecurityService
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceElevated
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SecuritySafe
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isUrdu by viewModel.isUrdu.collectAsState()
    val autoCopy by viewModel.autoCopyOnScan.collectAsState()
    val vibrate by viewModel.vibrateOnScan.collectAsState()
    val appLock by viewModel.appLockEnabled.collectAsState()
    val hasCloudApi = GeminiSecurityService.hasApiKey()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (isUrdu) "ترتیبات اور پرائیویسی" else "Settings & Security Preferences",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "QR Guard AI v1.0.0",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        // Section: Language
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Language, contentDescription = null, tint = NeonCyan)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Language / زبان", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text(if (isUrdu) "اردو فعال ہے" else "English active", fontSize = 12.sp, color = TextSecondary)
                            }
                        }

                        Switch(
                            checked = isUrdu,
                            onCheckedChange = { viewModel.isUrdu.value = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = NeonCyan,
                                checkedTrackColor = NeonPurple
                            )
                        )
                    }
                }
            }
        }

        // Section: Scanner Behaviors
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isUrdu) "اسکینر ترتیبات" else "Scanner Preferences",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Vibrate on Scan
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Vibration, contentDescription = null, tint = TextSecondary)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Haptic Feedback on Scan", fontSize = 13.sp, color = TextPrimary)
                        }
                        Switch(
                            checked = vibrate,
                            onCheckedChange = { viewModel.vibrateOnScan.value = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan, checkedTrackColor = CyberSurfaceElevated)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Auto Copy
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.QrCodeScanner, contentDescription = null, tint = TextSecondary)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Auto-Copy Result to Clipboard", fontSize = 13.sp, color = TextPrimary)
                        }
                        Switch(
                            checked = autoCopy,
                            onCheckedChange = { viewModel.autoCopyOnScan.value = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan, checkedTrackColor = CyberSurfaceElevated)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // App PIN / Biometric Lock
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Fingerprint, contentDescription = null, tint = TextSecondary)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("App Biometric / PIN Lock", fontSize = 13.sp, color = TextPrimary)
                        }
                        Switch(
                            checked = appLock,
                            onCheckedChange = {
                                viewModel.appLockEnabled.value = it
                                Toast.makeText(context, if (it) "App lock enabled" else "App lock disabled", Toast.LENGTH_SHORT).show()
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan, checkedTrackColor = CyberSurfaceElevated)
                        )
                    }
                }
            }
        }

        // Section: AI Engine Status
        item {
            GlassCard(borderColor = NeonPurple.copy(alpha = 0.35f), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = NeonPurple)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("AI Security Engine", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (hasCloudApi) SecuritySafe else NeonCyan)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (hasCloudApi) "Gemini 3.5 Flash Cloud Active" else "Local Cybersecurity Heuristic Reasoning Active",
                            fontSize = 12.sp,
                            color = if (hasCloudApi) SecuritySafe else NeonCyan,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Both local heuristics and Gemini multimodal vision collaborate to explain phishing, deepfake risks, and quishing threats.",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        // Section: Privacy & Transparency
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.PrivacyTip, contentDescription = null, tint = NeonCyan)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Privacy & Transparency Policy", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "• Local Storage: Scan history, bookmarks, and generated codes are stored entirely on your device in a secure Room database.\n• Zero Unwanted Tracking: Your camera stream is processed strictly in-memory during real-time scanning.\n• Data Control: You can export or wipe your history at any moment.\n• Responsible AI: AI assessments are designed to give actionable signals and confidence estimates without misleading claims of 100% certainty.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 17.sp
                    )
                }
            }
        }
    }
}
