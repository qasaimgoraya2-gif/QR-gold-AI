package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qrcode.QrCodeEngine
import com.example.security.RiskLevel
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.components.AiOrb
import com.example.ui.components.DetectedSignalRow
import com.example.ui.components.GlassCard
import com.example.ui.components.RiskBadge
import com.example.ui.components.SecurityScoreMeter
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SecurityHighRisk
import com.example.ui.theme.SecuritySafe
import com.example.ui.theme.SecuritySuspicious
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ResultDetailScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val analysis = viewModel.currentAnalysis.collectAsState().value
    val isUrdu by viewModel.isUrdu.collectAsState()

    var showOpenSafeDialog by remember { mutableStateOf(false) }

    // Loading State: "Analyzing QR Security..."
    if (isAnalyzing || analysis == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(CyberBackground),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AiOrb(size = 96.dp, isThinking = true)
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = if (isUrdu) "کیو آر سیکیورٹی کا تجزیہ جاری ہے..." else "Analyzing QR Security...",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (isUrdu) "ڈومین، پروٹوکول، اور فشنگ سگنلز کی جانچ کی جا رہی ہے" else "Inspecting protocol, typosquatting & payload heuristics",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }
        }
        return
    }

    // Generate mini QR bitmap preview for display
    val qrBitmap = remember(analysis.destination) {
        try {
            QrCodeEngine.generateQrBitmap(
                content = analysis.destination,
                width = 300,
                height = 300,
                foregroundColor = 0xFF00E5FF.toInt(),
                backgroundColor = 0xFF0A0F1D.toInt(),
                hasGradient = true,
                logoType = "SHIELD"
            )
        } catch (e: Exception) {
            null
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Navigation Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateTo(AppScreen.HOME) },
                    modifier = Modifier
                        .size(42.dp)
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

                Text(
                    text = if (isUrdu) "سیکیورٹی تجزیہ" else "Security Analysis",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Row {
                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("QR Content", analysis.destination))
                            Toast.makeText(context, if (isUrdu) "کاپی ہو گیا" else "Copied to clipboard", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy",
                            tint = NeonCyan
                        )
                    }
                    IconButton(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "QR Guard AI Security Report")
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "QR Guard AI Report:\nDestination: ${analysis.destination}\nRisk: ${analysis.riskLevel.name}\nScore: ${analysis.riskScore}/100\nExplanation: ${analysis.explanation}"
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Security Report"))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = NeonCyan
                        )
                    }
                }
            }
        }

        // QR Visual Card & Risk Indicator
        item {
            GlassCard(
                borderColor = when (analysis.riskLevel) {
                    RiskLevel.SAFE -> SecuritySafe.copy(alpha = 0.5f)
                    RiskLevel.SUSPICIOUS -> SecuritySuspicious.copy(alpha = 0.5f)
                    RiskLevel.HIGH_RISK -> SecurityHighRisk.copy(alpha = 0.6f)
                    RiskLevel.UNKNOWN -> CyberBorder
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // QR preview image
                    if (qrBitmap != null) {
                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                                .padding(6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                bitmap = qrBitmap.asImageBitmap(),
                                contentDescription = "Scanned QR Preview",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    // QR Type Tag
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(CyberSurfaceVariant)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "TYPE: ${analysis.qrType.name}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = analysis.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = analysis.destination,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Big Risk Level Badge
                    RiskBadge(riskLevel = analysis.riskLevel, isUrdu = isUrdu)

                    Spacer(modifier = Modifier.height(14.dp))

                    // Safety Confidence Meter
                    SecurityScoreMeter(
                        riskScore = analysis.riskScore,
                        riskLevel = analysis.riskLevel
                    )
                }
            }
        }

        // Plain Language / AI Explanation
        item {
            GlassCard(
                borderColor = CyberBorder,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = NeonPurple,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isUrdu) "اے آئی سیکیورٹی وضاحت" else "AI Security Explanation",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = analysis.explanation,
                        fontSize = 13.sp,
                        color = TextPrimary,
                        lineHeight = 19.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "RECOMMENDED ACTION:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = analysis.recommendedAction,
                        fontSize = 13.sp,
                        color = if (analysis.riskLevel == RiskLevel.HIGH_RISK) SecurityHighRisk else SecuritySafe,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Detected Technical Signals
        item {
            GlassCard(
                borderColor = CyberBorder,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isUrdu) "دیکھے گئے تکنیکی سگنلز" else "Detected Technical Signals",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    analysis.detectedSignals.forEach { signal ->
                        DetectedSignalRow(signal = signal)
                    }
                }
            }
        }

        // Action Buttons: Open Safely & Ask AI Assistant
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Open Safely Button
                if (analysis.riskLevel == RiskLevel.HIGH_RISK) {
                    // Blocked button for high-risk targets
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(SecurityHighRisk.copy(alpha = 0.15f))
                            .border(1.dp, SecurityHighRisk, RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = SecurityHighRisk,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isUrdu) "کھولنا ممنوع ہے: انتہائی مشکوک منزل" else "Blocked: Dangerous / Phishing Destination",
                                color = SecurityHighRisk,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                } else {
                    Button(
                        onClick = { showOpenSafeDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("open_safely_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Launch,
                            contentDescription = null,
                            tint = CyberBackground,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isUrdu) "محفوظ طریقے سے کھولیں" else "Open Safely",
                            color = CyberBackground,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }

                // Ask AI Assistant Button
                OutlinedButton(
                    onClick = {
                        viewModel.sendAiMessage("Explain why '${analysis.destination}' has risk level '${analysis.riskLevel.name}' and what I should be careful about.")
                        viewModel.navigateTo(AppScreen.AI_ASSISTANT)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("ask_ai_button"),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonPurple),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = NeonPurple,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isUrdu) "اے آئی معاون سے پوچھیں" else "Ask AI Security Assistant",
                        color = NeonPurple,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }

    // Safe Open Confirmation Dialog
    if (showOpenSafeDialog) {
        AlertDialog(
            onDismissRequest = { showOpenSafeDialog = false },
            title = {
                Text(
                    text = if (isUrdu) "محفوظ طریقے سے کھولیں؟" else "Safe-Open Confirmation",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = if (analysis.riskLevel == RiskLevel.SUSPICIOUS) {
                            "Warning: This destination contains potential risk signals. Never enter your passwords, PINs, or financial information on unverified domains."
                        } else {
                            "You are about to navigate to:\n${analysis.destination}\n\nDo you want to proceed?"
                        },
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showOpenSafeDialog = false
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(analysis.destination))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Cannot open URL schema", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                ) {
                    Text("Proceed", color = CyberBackground, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showOpenSafeDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = CyberSurface
        )
    }
}
