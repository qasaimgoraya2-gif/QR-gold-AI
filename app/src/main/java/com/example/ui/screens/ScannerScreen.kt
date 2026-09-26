package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.qrcode.QrCodeEngine
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.components.AiOrb
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SecurityHighRisk
import com.example.ui.theme.SecuritySafe
import com.example.ui.theme.SecuritySuspicious
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.concurrent.Executors

@Composable
fun ScannerScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isUrdu by viewModel.isUrdu.collectAsState()
    val vibrateOnScan by viewModel.vibrateOnScan.collectAsState()

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    var isTorchOn by remember { mutableStateOf(false) }
    var cameraControl: androidx.camera.core.CameraControl? by remember { mutableStateOf(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        }
    )

    // Gallery Picker for QR Scanning
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                try {
                    context.contentResolver.openInputStream(uri)?.use { stream ->
                        val bitmap = BitmapFactory.decodeStream(stream)
                        if (bitmap != null) {
                            val decodedText = QrCodeEngine.decodeQrFromBitmap(bitmap)
                            if (decodedText != null) {
                                triggerHaptic(context, vibrateOnScan)
                                viewModel.onQrScanned(decodedText, source = "GALLERY")
                            } else {
                                // If not direct QR, offer to analyze via Screenshot analyzer!
                                viewModel.analyzeScreenshotMedia(bitmap, "Scanned Image")
                                viewModel.navigateTo(AppScreen.MEDIA_ANALYZER)
                            }
                        }
                    }
                } catch (e: Exception) {
                    // Handled gracefully
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Scanning laser animation
    val infiniteTransition = rememberInfiniteTransition(label = "scanner_laser")
    val laserProgress by infiniteTransition.animateFloat(
        initialValue = 0.05f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground)
    ) {
        // Camera Viewport or Fallback
        if (hasCameraPermission) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        try {
                            val cameraProvider = cameraProviderFuture.get()
                            val preview = Preview.Builder().build().also {
                                it.surfaceProvider = previewView.surfaceProvider
                            }

                            val imageAnalysis = ImageAnalysis.Builder()
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .build()

                            // Image analyzer with ZXing
                            var scanCompleted = false
                            val cameraExecutor = Executors.newSingleThreadExecutor()
                            imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                                if (!scanCompleted) {
                                    val bitmap = imageProxy.toBitmap()
                                    val decoded = QrCodeEngine.decodeQrFromBitmap(bitmap)
                                    if (decoded != null) {
                                        scanCompleted = true
                                        triggerHaptic(context, vibrateOnScan)
                                        viewModel.onQrScanned(decoded, source = "CAMERA")
                                    }
                                }
                                imageProxy.close()
                            }

                            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                            cameraProvider.unbindAll()
                            val camera = cameraProvider.bindToLifecycle(
                                (ctx as androidx.lifecycle.LifecycleOwner),
                                cameraSelector,
                                preview,
                                imageAnalysis
                            )
                            cameraControl = camera.cameraControl
                        } catch (e: Exception) {
                            // Emulator without camera stream handled gracefully
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Permission request prompt
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (isUrdu) "کیمرے کی اجازت درکار ہے" else "Camera Access Required",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (isUrdu)
                        "کیو آر کوڈز اسکین کرنے کے لیے کیمرے کی اجازت دیں۔ آپ گیلری سے بھی اسکین کر سکتے ہیں۔"
                    else
                        "Grant camera permission to scan physical QR codes in real-time. Or select an image from your gallery.",
                    fontSize = 14.sp,
                    color = TextSecondary,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                ) {
                    Text(
                        text = if (isUrdu) "اجازت دیں" else "Enable Camera",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Semi-transparent Cyber Viewfinder Frame Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(36.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(2.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
            ) {
                // Animated glowing laser line
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val y = size.height * laserProgress
                    // Glow beam
                    drawLine(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                NeonCyan.copy(alpha = 0.9f),
                                NeonPurple.copy(alpha = 0.9f),
                                Color.Transparent
                            )
                        ),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 3.dp.toPx()
                    )
                }
            }
        }

        // Top Navigation Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateTo(AppScreen.HOME) },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(CyberSurface.copy(alpha = 0.8f))
                    .border(1.dp, CyberBorder, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }

            // Security Status Pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(CyberSurface.copy(alpha = 0.85f))
                    .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isUrdu) "حفاظتی فلٹر آن ہے" else "Auto Guard Active",
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Flashlight Toggle
            IconButton(
                onClick = {
                    isTorchOn = !isTorchOn
                    cameraControl?.enableTorch(isTorchOn)
                },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(CyberSurface.copy(alpha = 0.8f))
                    .border(1.dp, CyberBorder, CircleShape)
            ) {
                Icon(
                    imageVector = if (isTorchOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                    contentDescription = "Flashlight",
                    tint = if (isTorchOn) NeonCyan else TextPrimary
                )
            }
        }

        // Bottom Controls & Testing Quick Triggers (Crucial for testability & ease of use)
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, CyberBackground.copy(alpha = 0.95f), CyberBackground)
                    )
                )
                .padding(bottom = 90.dp, start = 16.dp, end = 16.dp)
        ) {
            // Gallery Upload Action Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        galleryLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberSurface),
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .border(1.dp, NeonCyan, RoundedCornerShape(24.dp))
                        .testTag("gallery_scan_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Gallery",
                        tint = NeonCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isUrdu) "گیلری یا اسکرین شاٹ منتخب کریں" else "Scan from Gallery / Screenshot",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Testing Preset QRs (Allows instant full-featured testing in any emulator environment)
            Text(
                text = if (isUrdu) "تجرباتی کیو آر کوڈز (ٹیسٹنگ کے لیے):" else "Test Scenarios (Instant Simulator):",
                fontSize = 12.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    TestChip(
                        title = "🟢 Safe Official URL",
                        onClick = {
                            triggerHaptic(context, vibrateOnScan)
                            viewModel.onQrScanned("https://www.google.com/safetycenter", "TEST_SAFE")
                        }
                    )
                }
                item {
                    TestChip(
                        title = "🔴 Typosquatting Phishing",
                        onClick = {
                            triggerHaptic(context, vibrateOnScan)
                            viewModel.onQrScanned("http://paypa1-security-verify.top/login.apk", "TEST_PHISHING")
                        }
                    )
                }
                item {
                    TestChip(
                        title = "🟡 Open Wi-Fi Network",
                        onClick = {
                            triggerHaptic(context, vibrateOnScan)
                            viewModel.onQrScanned("WIFI:S:Airport_Free_WiFi;T:nopass;;", "TEST_WIFI")
                        }
                    )
                }
                item {
                    TestChip(
                        title = "🟡 Crypto / UPI Transfer",
                        onClick = {
                            triggerHaptic(context, vibrateOnScan)
                            viewModel.onQrScanned("upi://pay?pa=merchant@upi&pn=UnknownStore&am=500", "TEST_PAYMENT")
                        }
                    )
                }
                item {
                    TestChip(
                        title = "⚪ Malformed URL",
                        onClick = {
                            triggerHaptic(context, vibrateOnScan)
                            viewModel.onQrScanned("http://:::malformed-host", "TEST_MALFORMED")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TestChip(
    title: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(CyberSurface)
            .border(1.dp, CyberBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = title,
            fontSize = 11.sp,
            color = TextPrimary,
            fontWeight = FontWeight.Medium
        )
    }
}

fun triggerHaptic(context: Context, enabled: Boolean) {
    if (!enabled) return
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator?.vibrate(
                VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
            )
        } else {
            @Suppress("DEPRECATION")
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            vibrator?.vibrate(50)
        }
    } catch (e: Exception) {
        // Ignored
    }
}
