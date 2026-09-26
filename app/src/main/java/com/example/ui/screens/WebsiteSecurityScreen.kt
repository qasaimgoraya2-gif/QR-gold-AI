package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.security.QrAnalyzer
import com.example.security.QrType
import com.example.security.RiskLevel
import com.example.security.SecurityAnalysisResult
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.components.DetectedSignalRow
import com.example.ui.components.GlassCard
import com.example.ui.components.RiskBadge
import com.example.ui.components.SecurityScoreMeter
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

@Composable
fun WebsiteSecurityScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var urlInput by remember { mutableStateOf("") }
    var activeCheckResult by remember { mutableStateOf<SecurityAnalysisResult?>(null) }
    val clipboardManager = LocalClipboardManager.current
    val isUrdu by viewModel.isUrdu.collectAsState()

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
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (isUrdu) "ویب سائٹ سیکیورٹی تجزیہ" else "Website Security Checker",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = if (isUrdu) "مشکوک لنکس اور فشنگ کی جانچ کریں" else "Inspect URLs for typosquatting & phishing",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        // URL Input Field & Check Button
        item {
            GlassCard(
                borderColor = NeonCyan.copy(alpha = 0.35f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isUrdu) "ویب سائٹ کا پتہ درج کریں" else "Enter Website URL:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = urlInput,
                        onValueChange = { urlInput = it },
                        placeholder = { Text("https://example.com", color = TextMuted) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("website_url_input"),
                        trailingIcon = {
                            IconButton(onClick = {
                                clipboardManager.getText()?.let {
                                    urlInput = it.text
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.ContentPaste,
                                    contentDescription = "Paste",
                                    tint = NeonCyan
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CyberBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = CyberSurfaceElevated,
                            unfocusedContainerColor = CyberSurfaceElevated
                        ),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            if (urlInput.isNotBlank()) {
                                activeCheckResult = QrAnalyzer.analyze(urlInput.trim(), isUrdu)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("analyze_url_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = CyberBackground,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isUrdu) "سیکیورٹی چیک کریں" else "Inspect Website",
                            color = CyberBackground,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }

        // Sample Test URLs
        item {
            Text(
                text = if (isUrdu) "تجرباتی یو آر ایل منتخب کریں:" else "Quick Test Benchmarks:",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    TestChip(
                        title = "🟢 Official Bank (Safe)",
                        onClick = {
                            urlInput = "https://www.chase.com"
                            activeCheckResult = QrAnalyzer.analyze("https://www.chase.com", isUrdu)
                        }
                    )
                }
                item {
                    TestChip(
                        title = "🔴 Typosquatting Phishing",
                        onClick = {
                            urlInput = "http://paypa1-account-alert.xyz/verify.php"
                            activeCheckResult = QrAnalyzer.analyze("http://paypa1-account-alert.xyz/verify.php", isUrdu)
                        }
                    )
                }
                item {
                    TestChip(
                        title = "🟡 Shortened URL Target",
                        onClick = {
                            urlInput = "https://bit.ly/3xSampleConcealed"
                            activeCheckResult = QrAnalyzer.analyze("https://bit.ly/3xSampleConcealed", isUrdu)
                        }
                    )
                }
            }
        }

        // Result Inspection Panel
        if (activeCheckResult != null) {
            val res = activeCheckResult!!
            item {
                GlassCard(
                    borderColor = when (res.riskLevel) {
                        RiskLevel.SAFE -> SecuritySafe.copy(alpha = 0.5f)
                        RiskLevel.SUSPICIOUS -> SecuritySuspicious.copy(alpha = 0.5f)
                        RiskLevel.HIGH_RISK -> SecurityHighRisk.copy(alpha = 0.6f)
                        RiskLevel.UNKNOWN -> CyberBorder
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = res.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            RiskBadge(riskLevel = res.riskLevel, isUrdu = isUrdu)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = res.destination,
                            fontSize = 12.sp,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        SecurityScoreMeter(riskScore = res.riskScore, riskLevel = res.riskLevel)

                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = if (isUrdu) "تکنیکی مشاہدات:" else "Technical Signals:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        res.detectedSignals.forEach { signal ->
                            DetectedSignalRow(signal = signal)
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = res.explanation,
                            fontSize = 13.sp,
                            color = TextPrimary,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Button to generate QR code directly from this verified website!
                        Button(
                            onClick = {
                                viewModel.genContent.value = res.destination
                                viewModel.genType.value = QrType.URL
                                viewModel.genFrameText.value = "VISIT WEBSITE"
                                viewModel.navigateTo(AppScreen.GENERATE)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = null,
                                tint = TextPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isUrdu) "اس ویب سائٹ کا کیو آر کوڈ بنائیں" else "Generate QR for this Website",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
