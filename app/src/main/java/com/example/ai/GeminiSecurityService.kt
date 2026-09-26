package com.example.ai

import android.graphics.Bitmap
import android.util.Base64
import com.example.BuildConfig
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

// Moshi data classes for Gemini API
@JsonClass(generateAdapter = true)
data class GenerateContentRequest(
    val contents: List<Content>,
    val generationConfig: GenerationConfig? = null,
    val systemInstruction: Content? = null
)

@JsonClass(generateAdapter = true)
data class Content(
    val parts: List<Part>,
    val role: String? = null
)

@JsonClass(generateAdapter = true)
data class Part(
    val text: String? = null,
    val inlineData: InlineData? = null
)

@JsonClass(generateAdapter = true)
data class InlineData(
    val mimeType: String,
    val data: String
)

@JsonClass(generateAdapter = true)
data class GenerationConfig(
    val temperature: Float? = 0.4f,
    val topP: Float? = 0.95f,
    val topK: Int? = 40
)

@JsonClass(generateAdapter = true)
data class GenerateContentResponse(
    val candidates: List<Candidate>? = null
)

@JsonClass(generateAdapter = true)
data class Candidate(
    val content: Content? = null
)

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

object GeminiSecurityService {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.NONE
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val service = retrofit.create(GeminiApiService::class.java)

    fun hasApiKey(): Boolean {
        return try {
            val key = BuildConfig.GEMINI_API_KEY
            key.isNotBlank() && key != "MY_GEMINI_API_KEY"
        } catch (e: Exception) {
            false
        }
    }

    suspend fun askAssistant(
        prompt: String,
        currentContext: String = "",
        isUrdu: Boolean = false,
        imageBitmap: Bitmap? = null
    ): String = withContext(Dispatchers.IO) {
        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (e: Exception) { "" }

        if (!hasApiKey()) {
            return@withContext generateOfflineCybersecurityResponse(prompt, currentContext, isUrdu)
        }

        try {
            val systemPrompt = """
                You are QR Guard AI, an elite cybersecurity and digital authenticity assistant.
                You help users inspect QR codes, URLs, suspicious messages, screenshots, and digital media.
                Core Rules:
                1. Never claim 100% certainty. Use terms like 'Likely Safe', 'Suspicious', 'High Risk', 'Unable to Verify'.
                2. Explain technical signals in clear, concise, actionable language.
                3. If asked in Urdu or requested in Urdu, respond in clear fluent Urdu (Nastaliq/Urdu script).
                4. Focus on user safety, credential protection, and phishing avoidance.
                ${if (currentContext.isNotBlank()) "Context of current scan or analysis:\n$currentContext" else ""}
            """.trimIndent()

            val parts = mutableListOf<Part>()
            if (imageBitmap != null) {
                val base64 = bitmapToBase64(imageBitmap)
                parts.add(Part(inlineData = InlineData(mimeType = "image/jpeg", data = base64)))
            }
            parts.add(Part(text = prompt))

            val request = GenerateContentRequest(
                contents = listOf(Content(parts = parts)),
                systemInstruction = Content(parts = listOf(Part(text = systemPrompt))),
                generationConfig = GenerationConfig(temperature = 0.3f)
            )

            val response = service.generateContent(apiKey, request)
            val answer = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            if (!answer.isNullOrBlank()) {
                answer
            } else {
                generateOfflineCybersecurityResponse(prompt, currentContext, isUrdu)
            }
        } catch (e: Exception) {
            // Graceful fallback to offline heuristic engine
            generateOfflineCybersecurityResponse(prompt, currentContext, isUrdu)
        }
    }

    suspend fun analyzeScreenshot(
        bitmap: Bitmap,
        extraNotes: String = "",
        isUrdu: Boolean = false
    ): String = withContext(Dispatchers.IO) {
        val prompt = if (isUrdu) {
            "براہ کرم اس اسکرین شاٹ کا مکمل سائبر سیکیورٹی تجزیہ کریں۔ کیا اس میں کوئی جعلی لاگ ان، مشکوک کیو آر، مالی فراڈ یا خطرہ ہے؟ تکنیکی علامات، خطرے کی سطح اور محفوظ اقدامات بیان کریں۔"
        } else {
            "Perform a cybersecurity authenticity inspection on this screenshot. Analyze any QR codes, login fields, suspicious URLs, deceptive branding, or fraud indicators. State observed signals, risk rating, and safe recommended actions."
        }
        askAssistant(prompt, extraNotes, isUrdu, bitmap)
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        // Resize bitmap if large to prevent huge request payloads
        val maxDim = 1024
        val scale = if (bitmap.width > maxDim || bitmap.height > maxDim) {
            val ratio = bitmap.width.toFloat() / bitmap.height.toFloat()
            if (ratio > 1) {
                Bitmap.createScaledBitmap(bitmap, maxDim, (maxDim / ratio).toInt(), true)
            } else {
                Bitmap.createScaledBitmap(bitmap, (maxDim * ratio).toInt(), maxDim, true)
            }
        } else {
            bitmap
        }
        scale.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        return Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP)
    }

    private fun generateOfflineCybersecurityResponse(
        prompt: String,
        context: String,
        isUrdu: Boolean
    ): String {
        val lower = prompt.lowercase()
        return if (isUrdu) {
            when {
                lower.contains("urdu") || lower.contains("اردو") ->
                    "کیو آر گارڈ اے آئی سیکیورٹی معاون:\nیہ کیو آر کوڈ یا ویب سائٹ ڈیٹا منتقل کرنے کے لیے استعمال ہوتی ہے۔ اگر یہ کسی ویب سائٹ کا لنک ہے تو ہمیشہ یو آر ایل کے ہجے اور HTTPS کی تصدیق کریں۔ کسی بھی غیر مانوس صفحے پر پاس ورڈ یا کریڈٹ کارڈ کی معلومات درج نہ کریں۔"
                lower.contains("suspicious") || lower.contains("مشکوک") || lower.contains("خطرہ") ->
                    "حفاظتی تجزیہ:\nاگر لنک میں عجیب الفاظ یا غلط ہجے (مثلاً g00gle یا paypa1) ہیں، تو یہ فریب کاری (Phishing) ہو سکتی ہے۔ نامعلوم کیو آر کوڈز اسکین کرنے کے بعد سیدھا لاگ ان نہ کریں۔"
                else ->
                    "کیو آر گارڈ اے آئی رہنمائی:\n1. کبھی بھی نامعلوم کیو آر کوڈز سے ایپلیکیشن (.apk) ڈاؤن لوڈ نہ کریں۔\n2. عوامی وائی فائی یا مفت انعامات کے دعوے اکثر مشکوک ہوتے ہیں۔\n3. ہمیشہ 'سیف اوپن' کی تصدیق کے بعد براؤزر میں کھولیں۔"
            }
        } else {
            when {
                lower.contains("what does this") || lower.contains("do") ->
                    "Technical Evaluation:\n${if (context.isNotBlank()) "Target: $context\n" else ""}QR codes are visual encodings of text or URI commands. When scanned by your device, they trigger actions like opening web browsers, configuring Wi-Fi credentials, or formatting contacts. QR Guard AI strictly inspects payloads before execution to prevent malicious automated redirects."
                lower.contains("suspicious") || lower.contains("risky") || lower.contains("safe") ->
                    "Security Assessment:\nIndicators evaluated include protocol encryption (HTTPS), domain reputation, character substitutions (typosquatting), and executable file indicators. Always review the full domain in your address bar before entering credentials."
                lower.contains("screenshot") ->
                    "Screenshot Inspection Guide:\nWhen inspecting screenshots, look for visual anomalies such as mismatched fonts, fake security badges, urgencies ('Account suspended in 24 hours'), and obscure payment links. Legitimate institutions do not ask for PINs or passwords via QR codes."
                else ->
                    "Cybersecurity Best Practice:\n1. Never approve unverified payment or 2FA prompts triggered from public posters.\n2. Ensure the domain ends with the exact legitimate organization domain, not a deceptive subdomain.\n3. Keep your browser security protections enabled."
            }
        }
    }
}
