package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.qrcode.QrCodeEngine
import com.example.security.QrType
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberBorder
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
import java.io.File
import java.io.FileOutputStream

@Composable
fun GeneratorStudioScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Content, 1: Studio Styling
    val isUrdu by viewModel.isUrdu.collectAsState()

    val content by viewModel.genContent.collectAsState()
    val qrType by viewModel.genType.collectAsState()
    val fgColor by viewModel.genFgColor.collectAsState()
    val bgColor by viewModel.genBgColor.collectAsState()
    val hasGradient by viewModel.genHasGradient.collectAsState()
    val gradientColor by viewModel.genGradientColor.collectAsState()
    val frameText by viewModel.genFrameText.collectAsState()
    val logoType by viewModel.genLogoType.collectAsState()
    val rounded by viewModel.genRounded.collectAsState()
    val testValid by viewModel.genReadabilityValid.collectAsState()

    // Generate real-time preview bitmap
    val previewBitmap = remember(content, fgColor, bgColor, hasGradient, gradientColor, rounded, logoType) {
        try {
            QrCodeEngine.generateQrBitmap(
                content = content.ifBlank { "https://example.com" },
                width = 500,
                height = 500,
                foregroundColor = fgColor.toInt(),
                backgroundColor = bgColor.toInt(),
                hasGradient = hasGradient,
                gradientColor = gradientColor.toInt(),
                rounded = rounded,
                logoType = logoType
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
        // Top Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
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

                Text(
                    text = if (isUrdu) "کیو آر جنریٹر اور اسٹوڈیو" else "QR Generator & Studio",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                IconButton(
                    onClick = {
                        // Reset defaults
                        viewModel.genFgColor.value = 0xFF00E5FF.toLong()
                        viewModel.genBgColor.value = 0xFF0A0F1D.toLong()
                        viewModel.genHasGradient.value = true
                        viewModel.genGradientColor.value = 0xFF7C4DFF.toLong()
                        viewModel.genFrameText.value = "SCAN ME"
                        viewModel.genLogoType.value = "SHIELD"
                        viewModel.genRounded.value = true
                        viewModel.genReadabilityValid.value = null
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.RestartAlt,
                        contentDescription = "Reset",
                        tint = TextSecondary
                    )
                }
            }
        }

        // Live QR Code Preview Card
        item {
            GlassCard(
                borderColor = NeonCyan.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Frame text header
                    if (frameText.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(fgColor.toInt()).copy(alpha = 0.15f))
                                .border(1.dp, Color(fgColor.toInt()).copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 14.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = frameText.uppercase(),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(fgColor.toInt()),
                                letterSpacing = 2.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // QR Bitmap
                    if (previewBitmap != null) {
                        Box(
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(bgColor.toInt()))
                                .border(1.dp, CyberBorder, RoundedCornerShape(16.dp))
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                bitmap = previewBitmap.asImageBitmap(),
                                contentDescription = "Generated QR Code",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Scan Test Status Banner
                    if (testValid != null) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (testValid == true) SecuritySafe.copy(alpha = 0.15f) else SecurityHighRisk.copy(alpha = 0.15f))
                                .border(1.dp, if (testValid == true) SecuritySafe else SecurityHighRisk, RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (testValid == true) Icons.Default.CheckCircle else Icons.Default.Warning,
                                contentDescription = null,
                                tint = if (testValid == true) SecuritySafe else SecurityHighRisk,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (testValid == true) "Scan Test Passed (100% Readable)" else "Caution: Low contrast, test failed",
                                color = if (testValid == true) SecuritySafe else SecurityHighRisk,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    // Scan Test and Action Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                val valid = viewModel.testQrReadability()
                                Toast.makeText(
                                    context,
                                    if (valid) "Readability Test Passed!" else "Test failed. Adjust colors for better contrast.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan)
                        ) {
                            Icon(imageVector = Icons.Default.Verified, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Scan Test", color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                viewModel.saveCurrentGeneratedQr("My QR (${qrType.name})")
                                Toast.makeText(context, "Saved to Generated History!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
                        ) {
                            Icon(imageVector = Icons.Default.Bookmark, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Save", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        IconButton(
                            onClick = {
                                if (previewBitmap != null) {
                                    shareBitmap(context, previewBitmap, content)
                                }
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(CyberSurfaceElevated)
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = NeonCyan)
                        }
                    }
                }
            }
        }

        // Tab Navigation: 1. Content & Type, 2. Studio Styling
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = CyberSurface,
                contentColor = NeonCyan,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = NeonCyan
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("1. QR Content", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("2. Studio Styling", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
                )
            }
        }

        // Tab 0: QR Content & Types
        if (selectedTab == 0) {
            item {
                Text("Select Type:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val types = listOf(
                        QrType.URL to "Website URL",
                        QrType.TEXT to "Plain Text",
                        QrType.WIFI to "Wi-Fi Network",
                        QrType.CONTACT to "vCard Contact",
                        QrType.PHONE to "Phone Call",
                        QrType.EMAIL to "Email Draft",
                        QrType.SMS to "SMS Message",
                        QrType.LOCATION to "Geo Location",
                        QrType.PAYMENT to "Payment / UPI"
                    )
                    items(types) { (type, label) ->
                        FilterChip(
                            selected = qrType == type,
                            onClick = {
                                viewModel.genType.value = type
                                when (type) {
                                    QrType.URL -> {
                                        viewModel.genContent.value = "https://mywebsite.com"
                                        viewModel.genFrameText.value = "VISIT WEBSITE"
                                        viewModel.genLogoType.value = "LINK"
                                    }
                                    QrType.WIFI -> {
                                        viewModel.genContent.value = "WIFI:S:MyHomeNetwork;T:WPA;P:SecretKey123;;"
                                        viewModel.genFrameText.value = "CONNECT WI-FI"
                                        viewModel.genLogoType.value = "WIFI"
                                    }
                                    QrType.CONTACT -> {
                                        viewModel.genContent.value = "BEGIN:VCARD\nVERSION:3.0\nN:Smith;John\nFN:John Smith\nTEL:+123456789\nEMAIL:john@example.com\nEND:VCARD"
                                        viewModel.genFrameText.value = "SAVE CONTACT"
                                        viewModel.genLogoType.value = "PHONE"
                                    }
                                    QrType.PAYMENT -> {
                                        viewModel.genContent.value = "upi://pay?pa=store@upi&pn=VerifiedMerchant&am=100"
                                        viewModel.genFrameText.value = "SECURE PAY"
                                        viewModel.genLogoType.value = "SHIELD"
                                    }
                                    else -> {
                                        viewModel.genContent.value = "Sample text content"
                                        viewModel.genFrameText.value = "SCAN ME"
                                        viewModel.genLogoType.value = "SHIELD"
                                    }
                                }
                            },
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

            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Payload Content:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = content,
                            onValueChange = {
                                viewModel.genContent.value = it
                                viewModel.genReadabilityValid.value = null
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("generator_content_input"),
                            minLines = 3,
                            maxLines = 6,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = CyberBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedContainerColor = CyberSurfaceElevated,
                                unfocusedContainerColor = CyberSurfaceElevated
                            ),
                            shape = RoundedCornerShape(14.dp)
                        )
                    }
                }
            }
        }

        // Tab 1: Studio Styling (Custom Colors, Gradients, Badges, Frame Text)
        if (selectedTab == 1) {
            // Preset Templates
            item {
                Text("Design Presets:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        PresetChip(
                            title = "⚡ Cyber Neon",
                            onClick = {
                                viewModel.genFgColor.value = 0xFF00E5FF.toLong()
                                viewModel.genBgColor.value = 0xFF0A0F1D.toLong()
                                viewModel.genHasGradient.value = true
                                viewModel.genGradientColor.value = 0xFF7C4DFF.toLong()
                                viewModel.genRounded.value = true
                                viewModel.genLogoType.value = "SHIELD"
                            }
                        )
                    }
                    item {
                        PresetChip(
                            title = "🛡️ Shield Dark",
                            onClick = {
                                viewModel.genFgColor.value = 0xFF00E676.toLong()
                                viewModel.genBgColor.value = 0xFF060913.toLong()
                                viewModel.genHasGradient.value = true
                                viewModel.genGradientColor.value = 0xFF00B0FF.toLong()
                                viewModel.genRounded.value = true
                                viewModel.genLogoType.value = "SHIELD"
                            }
                        )
                    }
                    item {
                        PresetChip(
                            title = "👑 Royal Gold",
                            onClick = {
                                viewModel.genFgColor.value = 0xFFFFD700.toLong()
                                viewModel.genBgColor.value = 0xFF140F00.toLong()
                                viewModel.genHasGradient.value = true
                                viewModel.genGradientColor.value = 0xFFFF6D00.toLong()
                                viewModel.genRounded.value = false
                                viewModel.genLogoType.value = "SECURE"
                            }
                        )
                    }
                    item {
                        PresetChip(
                            title = "⬛ Monolith Clean",
                            onClick = {
                                viewModel.genFgColor.value = 0xFFFFFFFF.toLong()
                                viewModel.genBgColor.value = 0xFF000000.toLong()
                                viewModel.genHasGradient.value = false
                                viewModel.genRounded.value = false
                                viewModel.genLogoType.value = "NONE"
                            }
                        )
                    }
                }
            }

            // Foreground & Gradient Colors
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Accent Colors:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val colors = listOf(
                                0xFF00E5FF.toLong() to "Cyan",
                                0xFF7C4DFF.toLong() to "Purple",
                                0xFF00E676.toLong() to "Emerald",
                                0xFFFFD600.toLong() to "Gold",
                                0xFFFF1744.toLong() to "Crimson",
                                0xFFFFFFFF.toLong() to "White"
                            )
                            colors.forEach { (c, name) ->
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Color(c.toInt()))
                                        .border(
                                            if (fgColor == c) 3.dp else 1.dp,
                                            if (fgColor == c) Color.White else CyberBorder,
                                            CircleShape
                                        )
                                        .clickable {
                                            viewModel.genFgColor.value = c
                                            viewModel.genReadabilityValid.value = null
                                        }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Gradient Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Enable Neon Gradient", fontSize = 13.sp, color = TextPrimary)
                            Switch(
                                checked = hasGradient,
                                onCheckedChange = { viewModel.genHasGradient.value = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = NeonCyan,
                                    checkedTrackColor = NeonPurple
                                )
                            )
                        }

                        // Rounded Modules Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Rounded Module Eyes", fontSize = 13.sp, color = TextPrimary)
                            Switch(
                                checked = rounded,
                                onCheckedChange = { viewModel.genRounded.value = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = NeonCyan,
                                    checkedTrackColor = CyberSurfaceElevated
                                )
                            )
                        }
                    }
                }
            }

            // Center Logo & Frame Text
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Center Security Logo:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            val logos = listOf(
                                "SHIELD" to Icons.Default.Shield,
                                "LINK" to Icons.Default.Link,
                                "WIFI" to Icons.Default.Wifi,
                                "PHONE" to Icons.Default.Phone,
                                "NONE" to Icons.Default.QrCode
                            )
                            logos.forEach { (type, icon) ->
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (logoType == type) NeonCyan.copy(alpha = 0.2f) else CyberSurfaceElevated)
                                        .border(
                                            if (logoType == type) 1.5.dp else 1.dp,
                                            if (logoType == type) NeonCyan else CyberBorder,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .clickable {
                                            viewModel.genLogoType.value = type
                                            viewModel.genReadabilityValid.value = null
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = type,
                                        tint = if (logoType == type) NeonCyan else TextSecondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Frame Badge Label:", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = frameText,
                            onValueChange = { viewModel.genFrameText.value = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = CyberBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedContainerColor = CyberSurfaceElevated,
                                unfocusedContainerColor = CyberSurfaceElevated
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PresetChip(
    title: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(CyberSurfaceVariant)
            .border(1.dp, CyberBorder, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(title, fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
    }
}

fun shareBitmap(context: Context, bitmap: Bitmap, contentText: String) {
    try {
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()
        val file = File(cachePath, "qr_guard_${System.currentTimeMillis()}.png")
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()

        val contentUri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, contentUri)
            putExtra(Intent.EXTRA_TEXT, "Generated via QR Guard AI:\n$contentText")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share QR Code"))
    } catch (e: Exception) {
        Toast.makeText(context, "Saved QR to cache", Toast.LENGTH_SHORT).show()
    }
}
