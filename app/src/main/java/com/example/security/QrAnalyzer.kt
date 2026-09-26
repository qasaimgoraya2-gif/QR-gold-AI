package com.example.security

import java.net.URI
import java.util.Locale

enum class QrType {
    URL,
    TEXT,
    WIFI,
    CONTACT,
    EMAIL,
    PHONE,
    SMS,
    LOCATION,
    CALENDAR,
    PAYMENT,
    OTHER
}

enum class RiskLevel {
    SAFE,
    SUSPICIOUS,
    HIGH_RISK,
    UNKNOWN;

    fun getDisplayName(isUrdu: Boolean = false): String {
        return if (isUrdu) {
            when (this) {
                SAFE -> "محفوظ (Likely Safe)"
                SUSPICIOUS -> "مشکوک (Suspicious)"
                HIGH_RISK -> "خطرناک (High Risk)"
                UNKNOWN -> "تصدیق ناممکن (Unable to Verify)"
            }
        } else {
            when (this) {
                SAFE -> "Likely Safe"
                SUSPICIOUS -> "Suspicious"
                HIGH_RISK -> "High Risk"
                UNKNOWN -> "Unable to Verify"
            }
        }
    }
}

data class SecurityAnalysisResult(
    val qrType: QrType,
    val title: String,
    val destination: String,
    val riskLevel: RiskLevel,
    val riskScore: Int, // 0 to 100
    val detectedSignals: List<String>,
    val explanation: String,
    val recommendedAction: String,
    val canOpenSafely: Boolean
)

object QrAnalyzer {

    private val SUSPICIOUS_TLDS = setOf(
        "top", "xyz", "work", "click", "buzz", "gq", "cf", "ml", "tk", "ga", "country", "stream", "download"
    )

    private val SUSPICIOUS_KEYWORDS = listOf(
        "verify", "login", "signin", "account-update", "secure-bank", "free-gift",
        "crypto-bonus", "airdrop", "claim-reward", "password-reset", "auth-token",
        "update-billing", "wallet-connect", "suspended", "urgent-action"
    )

    private val SUSPICIOUS_EXTENSIONS = listOf(
        ".apk", ".exe", ".scr", ".bat", ".cmd", ".vbs", ".iso", ".dmg", ".ps1"
    )

    private val KNOWN_BRAND_TYPOS = mapOf(
        "g00gle" to "google",
        "paypa1" to "paypal",
        "paypai" to "paypal",
        "arnazon" to "amazon",
        "micros0ft" to "microsoft",
        "appie" to "apple",
        "faceb00k" to "facebook",
        "netfiix" to "netflix",
        "instagrarn" to "instagram",
        "wha1sapp" to "whatsapp"
    )

    private val KNOWN_SHORTENERS = setOf(
        "bit.ly", "tinyurl.com", "t.co", "is.gd", "cutt.ly", "rb.gy", "goo.gl", "ow.ly", "buff.ly"
    )

    fun detectType(rawContent: String): QrType {
        val trimmed = rawContent.trim()
        val lower = trimmed.lowercase(Locale.ROOT)
        return when {
            lower.startsWith("http://") || lower.startsWith("https://") || lower.startsWith("www.") -> QrType.URL
            lower.startsWith("wifi:") -> QrType.WIFI
            lower.startsWith("begin:vcard") || lower.startsWith("mecard:") -> QrType.CONTACT
            lower.startsWith("mailto:") || lower.startsWith("matmsg:") -> QrType.EMAIL
            lower.startsWith("tel:") -> QrType.PHONE
            lower.startsWith("smsto:") || lower.startsWith("sms:") -> QrType.SMS
            lower.startsWith("geo:") -> QrType.LOCATION
            lower.startsWith("begin:vevent") -> QrType.CALENDAR
            lower.startsWith("upi://") || lower.startsWith("bitcoin:") || lower.startsWith("ethereum:") ||
                    lower.contains("paypal.me/") -> QrType.PAYMENT
            else -> {
                // If it looks like a domain without scheme (e.g. example.com/path)
                if (trimmed.contains(".") && !trimmed.contains(" ") && trimmed.length < 150) {
                    QrType.URL
                } else {
                    QrType.TEXT
                }
            }
        }
    }

    fun analyze(rawContent: String, isUrdu: Boolean = false): SecurityAnalysisResult {
        val trimmed = rawContent.trim()
        val type = detectType(trimmed)

        return when (type) {
            QrType.URL -> analyzeUrl(trimmed, isUrdu)
            QrType.WIFI -> analyzeWifi(trimmed, isUrdu)
            QrType.PAYMENT -> analyzePayment(trimmed, isUrdu)
            QrType.PHONE, QrType.SMS -> analyzePhoneOrSms(trimmed, type, isUrdu)
            QrType.EMAIL -> analyzeEmail(trimmed, isUrdu)
            QrType.CONTACT -> analyzeContact(trimmed, isUrdu)
            QrType.LOCATION -> SecurityAnalysisResult(
                qrType = QrType.LOCATION,
                title = "Geographic Coordinates",
                destination = trimmed,
                riskLevel = RiskLevel.SAFE,
                riskScore = 5,
                detectedSignals = listOf("Standard geo-location URI format", "No executable payload detected"),
                explanation = if (isUrdu) "یہ مقام کی معلومات رکھتا ہے۔" else "Contains geographic coordinates for maps navigation.",
                recommendedAction = if (isUrdu) "نقشے میں کھولیں۔" else "View in your maps application if expected.",
                canOpenSafely = true
            )
            QrType.CALENDAR -> SecurityAnalysisResult(
                qrType = QrType.CALENDAR,
                title = "Calendar Event Invitation",
                destination = trimmed.take(80) + "...",
                riskLevel = RiskLevel.SAFE,
                riskScore = 10,
                detectedSignals = listOf("Standard iCalendar (vEvent) format"),
                explanation = if (isUrdu) "یہ کیلنڈر کی تقریب کا ڈیٹا ہے۔" else "Contains event details for your device calendar.",
                recommendedAction = if (isUrdu) "تاریخ اور وقت کی تصدیق کریں۔" else "Verify event date, host, and description before saving.",
                canOpenSafely = true
            )
            QrType.TEXT, QrType.OTHER -> analyzePlainText(trimmed, isUrdu)
        }
    }

    private fun analyzeUrl(rawUrl: String, isUrdu: Boolean): SecurityAnalysisResult {
        val normalizedUrl = if (!rawUrl.startsWith("http://") && !rawUrl.startsWith("https://")) {
            "https://$rawUrl"
        } else {
            rawUrl
        }

        val signals = mutableListOf<String>()
        var penalty = 0

        val uri = try {
            URI(normalizedUrl)
        } catch (e: Exception) {
            null
        }

        if (uri == null || uri.host.isNullOrBlank()) {
            return SecurityAnalysisResult(
                qrType = QrType.URL,
                title = "Malformed Destination URL",
                destination = rawUrl,
                riskLevel = RiskLevel.UNKNOWN,
                riskScore = 50,
                detectedSignals = listOf("Invalid or malformed URI structure"),
                explanation = if (isUrdu) "یو آر ایل کی ساخت خراب ہے، تصدیق ممکن نہیں۔" else "URL syntax could not be properly parsed into a valid host.",
                recommendedAction = if (isUrdu) "اس لنک کو نہ کھولیں۔" else "Do not open this destination.",
                canOpenSafely = false
            )
        }

        val host = uri.host.lowercase(Locale.ROOT)
        val path = (uri.path ?: "").lowercase(Locale.ROOT)
        val query = (uri.query ?: "").lowercase(Locale.ROOT)

        // 1. Check scheme
        val isHttps = normalizedUrl.startsWith("https://", ignoreCase = true)
        if (!isHttps) {
            signals.add("Insecure plain HTTP protocol (traffic is unencrypted and vulnerable to interception)")
            penalty += 25
        } else {
            signals.add("Valid HTTPS protocol in use (data in transit is encrypted)")
        }

        // 2. IP address host
        val isIpHost = host.matches(Regex("""^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$"""))
        if (isIpHost) {
            signals.add("Direct numeric IP address hostname (bypasses domain name reputation)")
            penalty += 45
        }

        // 3. Known URL Shortener
        if (KNOWN_SHORTENERS.contains(host)) {
            signals.add("URL shortener service ($host) hides final destination target")
            penalty += 20
        }

        // 4. Typosquatting / deceptive spelling
        for ((typo, target) in KNOWN_BRAND_TYPOS) {
            if (host.contains(typo)) {
                signals.add("Deceptive typosquatting indicator: matches suspicious imitation of '$target'")
                penalty += 50
            }
        }

        // 5. Suspicious keywords in host or path
        val matchedKeywords = SUSPICIOUS_KEYWORDS.filter { host.contains(it) || path.contains(it) || query.contains(it) }
        if (matchedKeywords.isNotEmpty()) {
            signals.add("Sensitive phishing keywords detected: ${matchedKeywords.joinToString(", ")}")
            penalty += 30
        }

        // 6. Dangerous file download extensions
        val matchedExt = SUSPICIOUS_EXTENSIONS.find { path.endsWith(it) || query.contains(it) }
        if (matchedExt != null) {
            signals.add("Direct executable/archive download link detected ($matchedExt)")
            penalty += 55
        }

        // 7. Excessive subdomain count (common in phishing redirects)
        val domainParts = host.split(".")
        if (domainParts.size > 4) {
            signals.add("High subdomain depth (${domainParts.size} parts) indicates possible subdomain spoofing")
            penalty += 25
        }

        // 8. Suspicious TLD
        val tld = domainParts.lastOrNull() ?: ""
        if (SUSPICIOUS_TLDS.contains(tld)) {
            signals.add("Top-Level Domain (.$tld) has elevated statistical frequency in malicious abuse reports")
            penalty += 20
        }

        // Determine Risk Level based on accumulated penalty points
        val riskScore = penalty.coerceIn(0, 100)
        val riskLevel = when {
            riskScore >= 60 -> RiskLevel.HIGH_RISK
            riskScore >= 25 -> RiskLevel.SUSPICIOUS
            signals.isEmpty() -> RiskLevel.UNKNOWN
            else -> RiskLevel.SAFE
        }

        val explanation = if (isUrdu) {
            when (riskLevel) {
                RiskLevel.HIGH_RISK -> "اس یو آر ایل میں شدید خطرے کے اشارے ملے ہیں جن میں مشکوک ہوسٹ یا دھوکہ دہی کے عناصر شامل ہیں۔"
                RiskLevel.SUSPICIOUS -> "یہ لنک مشکوک معلوم ہوتا ہے۔ غیر محفوظ پروٹوکول یا چھپی ہوئی منزل ہو سکتی ہے۔"
                RiskLevel.SAFE -> "بنیادی حفاظتی جانچ پاس ہو گئی ہے، کوئی واضح خطرہ نہیں ملا۔"
                RiskLevel.UNKNOWN -> "دستیاب تکنیکی سگنلز کی بنیاد پر مکمل تصدیق ممکن نہیں۔"
            }
        } else {
            when (riskLevel) {
                RiskLevel.HIGH_RISK -> "Severe risk indicators detected. The link presents high likelihood of phishing, credential theft, or unauthorized file download."
                RiskLevel.SUSPICIOUS -> "Potential security risks observed. The destination exhibits patterns common in obfuscated or unverified links."
                RiskLevel.SAFE -> "Standard technical safety checks passed. No known malware patterns or deceptive anomalies were identified."
                RiskLevel.UNKNOWN -> "Unable to conclusively verify with available technical signals."
            }
        }

        val recommendedAction = if (isUrdu) {
            when (riskLevel) {
                RiskLevel.HIGH_RISK -> "اس لنک کو ہرگز نہ کھولیں۔ ذاتی یا بینک کی معلومات داخل نہ کریں۔"
                RiskLevel.SUSPICIOUS -> "اگر آپ بھیجنے والے کو ذاتی طور پر جانتے ہیں تو ہی احتیاط سے آگے بڑھیں۔"
                RiskLevel.SAFE -> "آپ محفوظ طریقے سے براؤزر میں کھول سکتے ہیں۔"
                RiskLevel.UNKNOWN -> "لنک کی دستی طور پر تصدیق کریں۔"
            }
        } else {
            when (riskLevel) {
                RiskLevel.HIGH_RISK -> "DO NOT OPEN. High risk of malicious payload or credential harvesting."
                RiskLevel.SUSPICIOUS -> "Proceed with extreme caution. Verify sender and check browser address bar carefully."
                RiskLevel.SAFE -> "Safe to proceed. Open securely in your default browser."
                RiskLevel.UNKNOWN -> "Review destination carefully before proceeding."
            }
        }

        return SecurityAnalysisResult(
            qrType = QrType.URL,
            title = host,
            destination = normalizedUrl,
            riskLevel = riskLevel,
            riskScore = riskScore,
            detectedSignals = signals,
            explanation = explanation,
            recommendedAction = recommendedAction,
            canOpenSafely = riskLevel != RiskLevel.HIGH_RISK
        )
    }

    private fun analyzeWifi(content: String, isUrdu: Boolean): SecurityAnalysisResult {
        val signals = mutableListOf<String>()
        var riskLevel = RiskLevel.SAFE
        var riskScore = 10

        val isWep = content.contains("T:WEP", ignoreCase = true)
        val isNone = content.contains("T:nopass", ignoreCase = true) || content.contains("T:;", ignoreCase = true)

        if (isNone) {
            signals.add("Unencrypted open Wi-Fi network (no password protection)")
            signals.add("Network traffic may be visible to nearby interceptors")
            riskLevel = RiskLevel.SUSPICIOUS
            riskScore = 40
        } else if (isWep) {
            signals.add("Obsolete WEP encryption scheme (cryptographically broken)")
            riskLevel = RiskLevel.SUSPICIOUS
            riskScore = 55
        } else {
            signals.add("WPA2/WPA3 modern Wi-Fi encryption standard detected")
            signals.add("Password-protected connection")
        }

        return SecurityAnalysisResult(
            qrType = QrType.WIFI,
            title = "Wi-Fi Access Configuration",
            destination = content,
            riskLevel = riskLevel,
            riskScore = riskScore,
            detectedSignals = signals,
            explanation = if (isUrdu) {
                if (riskLevel == RiskLevel.SUSPICIOUS) "یہ وائی فائی نیٹ ورک غیر محفوظ یا کھلے سگنل پر ہے۔" else "محفوظ وائی فائی کنکشن پایا گیا۔"
            } else {
                if (riskLevel == RiskLevel.SUSPICIOUS) "Unsecured or weakly protected Wi-Fi network detected." else "Encrypted wireless network configuration."
            },
            recommendedAction = if (isUrdu) "غیر مانوس کھلے نیٹ ورک پر بینکنگ نہ کریں۔" else "Do not transmit sensitive financial credentials over open unencrypted Wi-Fi.",
            canOpenSafely = true
        )
    }

    private fun analyzePayment(content: String, isUrdu: Boolean): SecurityAnalysisResult {
        val signals = listOf(
            "Financial/Payment instruction payload",
            "Triggers external wallet or UPI processing",
            "Verify recipient address and requested amount prior to confirmation"
        )
        return SecurityAnalysisResult(
            qrType = QrType.PAYMENT,
            title = "Payment / Financial Transfer",
            destination = content.take(120),
            riskLevel = RiskLevel.SUSPICIOUS, // Payment QRs should always alert caution
            riskScore = 35,
            detectedSignals = signals,
            explanation = if (isUrdu) "یہ کیو آر کوڈ مالی ادائیگی کی درخواست کرتا ہے۔" else "This QR initiates a financial transfer or cryptocurrency transaction.",
            recommendedAction = if (isUrdu) "رقم بھیجنے سے پہلے وصول کنندہ کا نام چیک کریں۔" else "Carefully verify payee address and payment sum before approving.",
            canOpenSafely = true
        )
    }

    private fun analyzePhoneOrSms(content: String, type: QrType, isUrdu: Boolean): SecurityAnalysisResult {
        val isSms = type == QrType.SMS
        val signals = mutableListOf<String>()
        var riskScore = 15
        var riskLevel = RiskLevel.SAFE

        if (content.contains("900") || content.contains("premium", ignoreCase = true)) {
            signals.add("Possible toll/premium rate dialer pattern")
            riskScore = 50
            riskLevel = RiskLevel.SUSPICIOUS
        } else {
            signals.add("Standard telephone/SMS messaging action")
        }

        return SecurityAnalysisResult(
            qrType = type,
            title = if (isSms) "SMS Message Action" else "Telephone Call Action",
            destination = content,
            riskLevel = riskLevel,
            riskScore = riskScore,
            detectedSignals = signals,
            explanation = if (isUrdu) "فون کال یا میسج کی ہدایت۔" else "Instructs device dialer or messaging service.",
            recommendedAction = if (isUrdu) "نمبر کی جانچ کریں۔" else "Check the destination number before placing the call or sending.",
            canOpenSafely = true
        )
    }

    private fun analyzeEmail(content: String, isUrdu: Boolean): SecurityAnalysisResult {
        return SecurityAnalysisResult(
            qrType = QrType.EMAIL,
            title = "Email Draft Instruction",
            destination = content,
            riskLevel = RiskLevel.SAFE,
            riskScore = 10,
            detectedSignals = listOf("Standard mailto schema", "Pre-composed email draft"),
            explanation = if (isUrdu) "یہ ای میل بھیجنے کا لنک ہے۔" else "Composes an email message with pre-filled recipient or subject.",
            recommendedAction = if (isUrdu) "وصول کنندہ کا ای میل چیک کریں۔" else "Review recipient address and text body before sending.",
            canOpenSafely = true
        )
    }

    private fun analyzeContact(content: String, isUrdu: Boolean): SecurityAnalysisResult {
        return SecurityAnalysisResult(
            qrType = QrType.CONTACT,
            title = "vCard Contact Card",
            destination = content.take(100) + "...",
            riskLevel = RiskLevel.SAFE,
            riskScore = 10,
            detectedSignals = listOf("Standard electronic business card format", "No active scripts"),
            explanation = if (isUrdu) "یہ ڈیجیٹل رابطہ کارڈ ہے۔" else "Contains contact name, phone, or email information.",
            recommendedAction = if (isUrdu) "رابطہ محفوظ کرنے سے پہلے تفصیلات دیکھیں۔" else "Verify contact details before saving to address book.",
            canOpenSafely = true
        )
    }

    private fun analyzePlainText(content: String, isUrdu: Boolean): SecurityAnalysisResult {
        return SecurityAnalysisResult(
            qrType = QrType.TEXT,
            title = "Plain Text Payload",
            destination = content.take(150),
            riskLevel = RiskLevel.SAFE,
            riskScore = 0,
            detectedSignals = listOf("Unstructured plain alphanumeric text", "No external URI scheme detected"),
            explanation = if (isUrdu) "یہ سادہ متن ہے۔ کوئی بیرونی لنک موجود نہیں۔" else "Contains plain text data with no automatic execution triggers.",
            recommendedAction = if (isUrdu) "آپ متن کاپی یا محفوظ کر سکتے ہیں۔" else "Safe to read, copy, or share.",
            canOpenSafely = true
        )
    }
}
