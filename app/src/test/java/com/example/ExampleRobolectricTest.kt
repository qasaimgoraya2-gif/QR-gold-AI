package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.security.QrAnalyzer
import com.example.security.QrType
import com.example.security.RiskLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("QR Guard AI", appName)
    }

    @Test
    fun `qr analyzer detects safe url`() {
        val result = QrAnalyzer.analyze("https://www.google.com")
        assertEquals(QrType.URL, result.qrType)
        assertEquals(RiskLevel.SAFE, result.riskLevel)
        assertTrue(result.canOpenSafely)
    }

    @Test
    fun `qr analyzer flags typosquatting phishing`() {
        val result = QrAnalyzer.analyze("http://paypa1-security-verify.top/login.apk")
        assertEquals(QrType.URL, result.qrType)
        assertEquals(RiskLevel.HIGH_RISK, result.riskLevel)
    }

    @Test
    fun `qr analyzer detects wifi payload`() {
        val result = QrAnalyzer.analyze("WIFI:S:MyHome;T:WPA;P:pass123;;")
        assertEquals(QrType.WIFI, result.qrType)
        assertEquals(RiskLevel.SAFE, result.riskLevel)
    }
}
