package com.example.qrcode

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Shader
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.EnumMap

object QrCodeEngine {

    fun generateQrBitmap(
        content: String,
        width: Int = 800,
        height: Int = 800,
        foregroundColor: Int = 0xFF00E5FF.toInt(), // Electric cyan
        backgroundColor: Int = 0xFF0A0F1D.toInt(), // Dark navy
        hasGradient: Boolean = true,
        gradientColor: Int = 0xFF7C4DFF.toInt(), // Neon purple
        rounded: Boolean = true,
        logoType: String = "SHIELD", // NONE, SHIELD, LINK, WIFI, PHONE, SECURE
        margin: Int = 2
    ): Bitmap {
        val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java).apply {
            put(EncodeHintType.CHARACTER_SET, "UTF-8")
            put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H) // Highest error correction for logo tolerance
            put(EncodeHintType.MARGIN, margin)
        }

        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints)
        val matrixWidth = bitMatrix.width
        val matrixHeight = bitMatrix.height

        val bitmap = Bitmap.createBitmap(matrixWidth, matrixHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Draw background
        val bgPaint = Paint().apply {
            color = backgroundColor
            style = Paint.Style.FILL
        }
        canvas.drawRect(0f, 0f, matrixWidth.toFloat(), matrixHeight.toFloat(), bgPaint)

        // Setup module paint (with linear gradient if enabled)
        val modulePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            if (hasGradient) {
                shader = LinearGradient(
                    0f, 0f, matrixWidth.toFloat(), matrixHeight.toFloat(),
                    foregroundColor, gradientColor, Shader.TileMode.CLAMP
                )
            } else {
                color = foregroundColor
            }
        }

        // Module size
        val cellWidth = matrixWidth.toFloat() / matrixWidth
        val cellHeight = matrixHeight.toFloat() / matrixHeight

        val radius = if (rounded) (cellWidth * 0.4f).coerceAtLeast(2f) else 0f
        val rect = RectF()

        for (x in 0 until matrixWidth) {
            for (y in 0 until matrixHeight) {
                if (bitMatrix.get(x, y)) {
                    rect.set(
                        x.toFloat(),
                        y.toFloat(),
                        (x + 1).toFloat(),
                        (y + 1).toFloat()
                    )
                    if (rounded) {
                        canvas.drawRoundRect(rect, radius, radius, modulePaint)
                    } else {
                        canvas.drawRect(rect, modulePaint)
                    }
                }
            }
        }

        // Draw Center Logo / Badge if requested
        if (logoType != "NONE") {
            drawCenterBadge(canvas, matrixWidth.toFloat(), matrixHeight.toFloat(), logoType, backgroundColor, foregroundColor)
        }

        return bitmap
    }

    private fun drawCenterBadge(
        canvas: Canvas,
        width: Float,
        height: Float,
        logoType: String,
        badgeBgColor: Int,
        badgeFgColor: Int
    ) {
        val centerX = width / 2f
        val centerY = height / 2f
        val badgeRadius = width * 0.12f

        // Draw badge circular background cutout
        val badgeBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = badgeBgColor
            style = Paint.Style.FILL
        }
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = badgeFgColor
            style = Paint.Style.STROKE
            strokeWidth = width * 0.015f
        }

        canvas.drawCircle(centerX, centerY, badgeRadius, badgeBgPaint)
        canvas.drawCircle(centerX, centerY, badgeRadius, borderPaint)

        // Draw icon shape inside the badge
        val iconPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = badgeFgColor
            style = Paint.Style.STROKE
            strokeWidth = width * 0.02f
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }

        val iconFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = badgeFgColor
            style = Paint.Style.FILL
        }

        val s = badgeRadius * 0.5f

        when (logoType) {
            "SHIELD", "SECURE" -> {
                // Draw cybersecurity shield
                val path = android.graphics.Path().apply {
                    moveTo(centerX, centerY - s)
                    lineTo(centerX + s * 0.8f, centerY - s * 0.4f)
                    lineTo(centerX + s * 0.8f, centerY + s * 0.2f)
                    quadTo(centerX, centerY + s * 1.1f, centerX, centerY + s * 1.1f)
                    quadTo(centerX - s * 0.8f, centerY + s * 0.2f, centerX - s * 0.8f, centerY + s * 0.2f)
                    lineTo(centerX - s * 0.8f, centerY - s * 0.4f)
                    close()
                }
                canvas.drawPath(path, iconPaint)
                // Center dot
                canvas.drawCircle(centerX, centerY, s * 0.2f, iconFillPaint)
            }
            "WIFI" -> {
                // Draw Wi-Fi arcs
                canvas.drawCircle(centerX, centerY + s * 0.4f, s * 0.18f, iconFillPaint)
                val arcRect1 = RectF(centerX - s * 0.5f, centerY - s * 0.2f, centerX + s * 0.5f, centerY + s * 0.8f)
                canvas.drawArc(arcRect1, 200f, 140f, false, iconPaint)
                val arcRect2 = RectF(centerX - s * 0.9f, centerY - s * 0.6f, centerX + s * 0.9f, centerY + s * 1.2f)
                canvas.drawArc(arcRect2, 200f, 140f, false, iconPaint)
            }
            "LINK" -> {
                // Draw Link icon (two angled ovals or lines)
                canvas.drawLine(centerX - s * 0.4f, centerY - s * 0.4f, centerX + s * 0.4f, centerY + s * 0.4f, iconPaint)
                canvas.drawCircle(centerX - s * 0.35f, centerY - s * 0.35f, s * 0.25f, iconPaint)
                canvas.drawCircle(centerX + s * 0.35f, centerY + s * 0.35f, s * 0.25f, iconPaint)
            }
            "PHONE" -> {
                // Draw Phone handset outline
                val phoneRect = RectF(centerX - s * 0.4f, centerY - s * 0.7f, centerX + s * 0.4f, centerY + s * 0.7f)
                canvas.drawRoundRect(phoneRect, s * 0.2f, s * 0.2f, iconPaint)
                canvas.drawCircle(centerX, centerY + s * 0.45f, s * 0.1f, iconFillPaint)
            }
        }
    }

    /**
     * Decodes a QR code directly from an in-memory Bitmap.
     * Used for scanning from gallery, screenshots, and running "Scan Test" validation.
     */
    fun decodeQrFromBitmap(bitmap: Bitmap): String? {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        val source = RGBLuminanceSource(width, height, pixels)
        val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
        val reader = MultiFormatReader()

        return try {
            val result = reader.decode(binaryBitmap)
            result.text
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Performs a readability test on the generated QR code to ensure
     * customized styling (gradients, logos, rounded modules) didn't corrupt the QR data.
     */
    fun testReadability(bitmap: Bitmap, expectedContent: String): Boolean {
        val decoded = decodeQrFromBitmap(bitmap)
        return decoded == expectedContent
    }
}
