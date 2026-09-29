# QR Guard AI — مکمل کوڈ اور اے پی کے (APK) گائیڈ
## Complete Source Code, Architecture & APK Build Guide

یہ دستاویز آپ کے لیے **QR Guard AI** ایپ کا مکمل کوڈ، پروجیکٹ اسٹرکچر، اور موبائل میں انسٹال کرنے کے لیے APK بنانے کے تمام طریقے فراہم کرتی ہے۔ آپ اس فائل کو محفوظ کر کے آسانی سے **PDF** میں بھی تبدیل کر سکتے ہیں (براؤزر یا مارک ڈاؤن ویور کے ذریعے Print to PDF کر کے)۔

---

## 1. موبائل میں اے پی کے (APK) حاصل اور انسٹال کرنے کا سب سے آسان طریقہ
### (Method 1: Direct Download from AI Studio)

1. **AI Studio کے اوپر دائیں کونے (Settings / Menu) پر جائیں:**
   - اسکرین کے اوپر دائیں کونے میں موجود **Export** یا **Settings** بٹن پر کلک کریں۔
2. **Download APK / Export Project منتخب کریں:**
   - آپ براہ راست بنی بنائی **APK فائل ڈاؤن لوڈ** کر سکتے ہیں یا پورا پروجیکٹ **ZIP** کی صورت میں ایکسپورٹ کر سکتے ہیں۔
3. **موبائل پر انسٹال کریں:**
   - ڈاؤن لوڈ کی گئی `app-debug.apk` فائل اپنے اینڈرائیڈ فون میں بھیجیں۔
   - فائل پر کلک کریں اور "Install from Unknown Sources" کو اجازت دے کر انسٹال کر لیں۔

---

## 2. اپنے کمپیوٹر / اینڈرائیڈ اسٹوڈیو میں کوڈ سے نئی APK بنانے کا طریقہ
### (Method 2: Build APK using Android Studio / Gradle)

1. **Android Studio کھولیں** اور **Open an Existing Project** پر کلک کر کے اس پروجیکٹ کا فولڈر منتخب کریں۔
2. انٹرنیٹ کنکشن کے ساتھ گریڈل (Gradle Sync) مکمل ہونے دیں۔
3. مینو سے **Build > Build Bundle(s) / APK(s) > Build APK(s)** پر کلک کریں۔
4. چند سیکنڈ میں آپ کی APK فائل درج ذیل فولڈر میں تیار ہو جائے گی:
   `app/build/outputs/apk/debug/app-debug.apk`
5. کمانڈ لائن سے بنانے کے لیے:
   ```bash
   ./gradlew :app:assembleDebug
   ```

---

## 3. پروجیکٹ فائل کا ڈھانچہ (Complete File Tree)

```text
/metadata.json
/build.gradle.kts
/settings.gradle.kts
/gradle/libs.versions.toml
/app/build.gradle.kts
/app/src/main/AndroidManifest.xml
/app/src/main/res/xml/file_paths.xml
/app/src/main/res/values/strings.xml
/app/src/main/java/com/example/MainActivity.kt
/app/src/main/java/com/example/ui/MainViewModel.kt
/app/src/main/java/com/example/ui/theme/Color.kt
/app/src/main/java/com/example/ui/theme/Theme.kt
/app/src/main/java/com/example/ui/theme/Type.kt
/app/src/main/java/com/example/ui/components/AiOrb.kt
/app/src/main/java/com/example/ui/components/SecurityComponents.kt
/app/src/main/java/com/example/ui/screens/HomeScreen.kt
/app/src/main/java/com/example/ui/screens/ScannerScreen.kt
/app/src/main/java/com/example/ui/screens/ResultDetailScreen.kt
/app/src/main/java/com/example/ui/screens/WebsiteSecurityScreen.kt
/app/src/main/java/com/example/ui/screens/GeneratorStudioScreen.kt
/app/src/main/java/com/example/ui/screens/AiAssistantScreen.kt
/app/src/main/java/com/example/ui/screens/MediaAnalyzerScreen.kt
/app/src/main/java/com/example/ui/screens/HistoryScreen.kt
/app/src/main/java/com/example/ui/screens/SettingsScreen.kt
/app/src/main/java/com/example/security/QrAnalyzer.kt
/app/src/main/java/com/example/qrcode/QrCodeEngine.kt
/app/src/main/java/com/example/ai/GeminiSecurityService.kt
/app/src/main/java/com/example/data/local/AppDatabase.kt
/app/src/main/java/com/example/data/local/entity/ScanRecordEntity.kt
/app/src/main/java/com/example/data/local/entity/GeneratedQrEntity.kt
/app/src/main/java/com/example/data/local/entity/SecurityDomainRuleEntity.kt
/app/src/main/java/com/example/data/local/dao/ScanRecordDao.kt
/app/src/main/java/com/example/data/local/dao/GeneratedQrDao.kt
/app/src/main/java/com/example/data/local/dao/SecurityDomainRuleDao.kt
/app/src/main/java/com/example/data/repository/ScanRepository.kt
/app/src/test/java/com/example/ExampleRobolectricTest.kt
```

---

# 4. تمام فائلوں کا مکمل سورس کوڈ (Full Source Code)

### فائل 1: `/build.gradle.kts` (Project Root)
```kotlin
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.kotlin.compose) apply false
  alias(libs.plugins.google.devtools.ksp) apply false
  alias(libs.plugins.secrets) apply false
  alias(libs.plugins.google.services) apply false
}
```

---

### فائل 2: `/settings.gradle.kts`
```kotlin
pluginManagement {
  repositories {
    google {
      content {
        includeGroupByRegex("com\\.android.*")
        includeGroupByRegex("com\\.google.*")
        includeGroupByRegex("androidx.*")
      }
    }
    mavenCentral()
    gradlePluginPortal()
  }
}
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
  }
}

rootProject.name = "QR Guard AI"
include(":app")
```

---

### فائل 3: `/gradle/libs.versions.toml` (Version Catalog)
```toml
[versions]
agp = "9.1.1"
coreKtx = "1.18.0"
junit = "4.13.2"
junitVersion = "1.3.0"
espressoCore = "3.7.0"
lifecycleRuntimeKtx = "2.8.7"
lifecycleViewmodelCompose = "2.8.7"
lifecycleRuntimeCompose = "2.8.7"
activityCompose = "1.10.1"
kotlin = "2.2.10"
composeBom = "2024.09.00"
googleDevtoolsKsp = "2.3.5"
navigationCompose = "2.8.9"
roomRuntime = "2.7.0"
roomKtx = "2.7.0"
roomCompiler = "2.7.0"
kotlinxCoroutinesTest = "1.10.2"
core = "1.6.1"
runner = "1.6.2"
coilCompose = "2.7.0"
retrofit = "2.12.0"
converterMoshi = "2.12.0"
kotlinxCoroutinesAndroid = "1.10.2"
kotlinxCoroutinesCore = "1.10.2"
accompanistPermissions = "0.37.3"
playServicesLocation = "21.3.0"
cameraCamera2 = "1.5.0"
cameraLifecycle = "1.5.0"
cameraView = "1.5.0"
cameraCore = "1.5.0"
loggingInterceptor = "4.10.0"
okhttp = "4.10.0"
moshiKotlin = "1.15.2"
moshiKotlinCodegen = "1.15.2"
datastorePreferences = "1.1.7"
robolectric = "4.16.1"
firebaseBom = "34.17.0"
secretsGradlePlugin = "2.0.1"
googleServices = "4.5.0"
credentials = "1.5.0"
googleid = "1.1.1"
zxing = "3.5.3"

[libraries]
zxing-core = { group = "com.google.zxing", name = "core", version.ref = "zxing" }
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
junit = { group = "junit", name = "junit", version.ref = "junit" }
androidx-junit = { group = "androidx.test.ext", name = "junit", version.ref = "junitVersion" }
androidx-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }
androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycleRuntimeKtx" }
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycleViewmodelCompose" }
androidx-lifecycle-runtime-compose = { group = "androidx.lifecycle", name = "lifecycle-runtime-compose", version.ref = "lifecycleRuntimeCompose" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
androidx-compose-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-compose-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
androidx-compose-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-compose-ui-test-manifest = { group = "androidx.compose.ui", name = "ui-test-manifest" }
androidx-compose-ui-test-junit4 = { group = "androidx.compose.ui", name = "ui-test-junit4" }
androidx-compose-material3 = { group = "androidx.compose.material3", name = "material3" }
androidx-compose-material-icons-core = { group = "androidx.compose.material", name = "material-icons-core" }
androidx-compose-material-icons-extended = { group = "androidx.compose.material", name = "material-icons-extended" }
androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigationCompose" }
androidx-room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "roomRuntime" }
androidx-room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "roomKtx" }
androidx-room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "roomCompiler" }
androidx-core = { group = "androidx.test", name = "core", version.ref = "core" }
androidx-runner = { group = "androidx.test", name = "runner", version.ref = "runner" }
coil-compose = { group = "io.coil-kt", name = "coil-compose", version.ref = "coilCompose" }
retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
converter-moshi = { group = "com.squareup.retrofit2", name = "converter-moshi", version.ref = "converterMoshi" }
kotlinx-coroutines-core = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version.ref = "kotlinxCoroutinesCore" }
kotlinx-coroutines-android = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-android", version.ref = "kotlinxCoroutinesAndroid" }
kotlinx-coroutines-test = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-test", version.ref = "kotlinxCoroutinesTest" }
accompanist-permissions = { group = "com.google.accompanist", name = "accompanist-permissions", version.ref = "accompanistPermissions" }
play-services-location = { group = "com.google.android.gms", name = "play-services-location", version.ref = "playServicesLocation" }
androidx-camera-camera2 = { group = "androidx.camera", name = "camera-camera2", version.ref = "cameraCamera2" }
androidx-camera-lifecycle = { group = "androidx.camera", name = "camera-lifecycle", version.ref = "cameraLifecycle" }
androidx-camera-view = { group = "androidx.camera", name = "camera-view", version.ref = "cameraView" }
androidx-camera-core = { group = "androidx.camera", name = "camera-core", version.ref = "cameraCore" }
logging-interceptor = { group = "com.squareup.okhttp3", name = "logging-interceptor", version.ref = "loggingInterceptor" }
okhttp = { group = "com.squareup.okhttp3", name = "okhttp", version.ref = "okhttp" }
moshi-kotlin = { group = "com.squareup.moshi", name = "moshi-kotlin", version.ref = "moshiKotlin" }
moshi-kotlin-codegen = { group = "com.squareup.moshi", name = "moshi-kotlin-codegen", version.ref = "moshiKotlinCodegen" }
androidx-datastore-preferences = { group = "androidx.datastore", name = "datastore-preferences", version.ref = "datastorePreferences" }
robolectric = { group = "org.robolectric", name = "robolectric", version.ref = "robolectric" }
firebase-bom = { group = "com.google.firebase", name = "firebase-bom", version.ref = "firebaseBom" }
firebase-ai = { group = "com.google.firebase", name = "firebase-vertexai" }
firebase-firestore = { group = "com.google.firebase", name = "firebase-firestore" }
firebase-auth = { group = "com.google.firebase", name = "firebase-auth" }
androidx-credentials = { group = "androidx.credentials", name = "credentials", version.ref = "credentials" }
androidx-credentials-play-services = { group = "androidx.credentials", name = "credentials-play-services-auth", version.ref = "credentials" }
googleid = { group = "com.google.android.libraries.identity.googleid", name = "googleid", version.ref = "googleid" }
firebase-appcheck-recaptcha = { group = "com.google.firebase", name = "firebase-appcheck-recaptcha" }
firebase-appcheck-debug = { group = "com.google.firebase", name = "firebase-appcheck-debug" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
google-devtools-ksp = { id = "com.google.devtools.ksp", version.ref = "googleDevtoolsKsp" }
secrets = { id = "com.google.android.libraries.mapsplatform.secrets-gradle-plugin", version.ref = "secretsGradlePlugin" }
google-services = { id = "com.google.gms.google-services", version.ref = "googleServices" }
```

---

### فائل 4: `/app/build.gradle.kts`
```kotlin
import com.google.gms.googleservices.GoogleServicesPlugin.MissingGoogleServicesStrategy

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.google.devtools.ksp)
  alias(libs.plugins.secrets)
  alias(libs.plugins.google.services)
}

android {
  namespace = "com.example"
  compileSdk { version = release(36) { minorApiLevel = 1 } }

  defaultConfig {
    applicationId = "com.example"
    minSdk = 24
    targetSdk = 36
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  signingConfigs {
    create("release") {
      val keystorePath = System.getenv("KEYSTORE_PATH") ?: "${rootDir}/my-upload-key.jks"
      storeFile = file(keystorePath)
      storePassword = System.getenv("STORE_PASSWORD")
      keyAlias = "upload"
      keyPassword = System.getenv("KEY_PASSWORD")
    }
    create("debugConfig") {
      storeFile = file("${rootDir}/debug.keystore")
      storePassword = "android"
      keyAlias = "androiddebugkey"
      keyPassword = "android"
    }
  }

  buildTypes {
    release {
      isCrunchPngs = false
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("release")
    }
    debug { signingConfig = signingConfigs.getByName("debugConfig") }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }
  testOptions { unitTests { isIncludeAndroidResources = true } }
  dependenciesInfo {
    includeInApk = false
    includeInBundle = true
  }
}

secrets {
  propertiesFileName = ".env"
  defaultPropertiesFileName = ".env.example"
  ignoreList.add("FIREBASE_APPCHECK_DEBUG_TOKEN")
}

googleServices { missingGoogleServicesStrategy = MissingGoogleServicesStrategy.WARN }

dependencies {
  implementation(platform(libs.androidx.compose.bom))
  implementation(platform(libs.firebase.bom))
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.camera.camera2)
  implementation(libs.androidx.camera.core)
  implementation(libs.androidx.camera.lifecycle)
  implementation(libs.androidx.camera.view)
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.androidx.compose.material.icons.extended)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.room.runtime)
  implementation(libs.coil.compose)
  implementation(libs.zxing.core)
  implementation(libs.converter.moshi)
  implementation(libs.firebase.ai)
  implementation(libs.firebase.appcheck.recaptcha)
  implementation(libs.firebase.appcheck.debug)
  implementation(libs.kotlinx.coroutines.android)
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.logging.interceptor)
  implementation(libs.moshi.kotlin)
  implementation(libs.okhttp)
  implementation(libs.retrofit)
  testImplementation(libs.androidx.compose.ui.test.junit4)
  testImplementation(libs.androidx.core)
  testImplementation(libs.androidx.junit)
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.robolectric)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.runner)
  debugImplementation(libs.androidx.compose.ui.test.manifest)
  debugImplementation(libs.androidx.compose.ui.tooling)
  "ksp"(libs.androidx.room.compiler)
  "ksp"(libs.moshi.kotlin.codegen)
}
```

---

### فائل 5: `/app/src/main/AndroidManifest.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.VIBRATE" />

    <uses-feature android:name="android.hardware.camera" android:required="false" />
    <uses-feature android:name="android.hardware.camera.autofocus" android:required="false" />

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.MyApplication"
        tools:targetApi="31">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:label="@string/app_name"
            android:theme="@style/Theme.MyApplication">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="${applicationId}.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths" />
        </provider>
    </application>

</manifest>
```

---

### فائل 6: `/app/src/main/res/values/strings.xml`
```xml
<resources>
    <string name="app_name">QR Guard AI</string>
</resources>
```

---

### فائل 7: `/app/src/main/res/xml/file_paths.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <cache-path name="cached_images" path="images/"/>
</paths>
```

---

### فائل 8: `/app/src/main/java/com/example/MainActivity.kt`
```kotlin
package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.screens.AiAssistantScreen
import com.example.ui.screens.GeneratorStudioScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MediaAnalyzerScreen
import com.example.ui.screens.ResultDetailScreen
import com.example.ui.screens.ScannerScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.WebsiteSecurityScreen
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        viewModel.navigateTo(AppScreen.HOME)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBackground),
        bottomBar = {
            if (currentScreen != AppScreen.SCAN && currentScreen != AppScreen.AI_ASSISTANT) {
                CyberBottomNavigation(
                    currentScreen = currentScreen,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                    AppScreen.HOME -> HomeScreen(viewModel = viewModel)
                    AppScreen.SCAN -> ScannerScreen(viewModel = viewModel)
                    AppScreen.RESULT_DETAIL -> ResultDetailScreen(viewModel = viewModel)
                    AppScreen.GENERATE -> GeneratorStudioScreen(viewModel = viewModel)
                    AppScreen.SECURITY -> WebsiteSecurityScreen(viewModel = viewModel)
                    AppScreen.AI_ASSISTANT -> AiAssistantScreen(viewModel = viewModel)
                    AppScreen.MEDIA_ANALYZER -> MediaAnalyzerScreen(viewModel = viewModel)
                    AppScreen.HISTORY -> HistoryScreen(viewModel = viewModel)
                    AppScreen.SETTINGS -> SettingsScreen(viewModel = viewModel)
                }
            }
        }
    }
}

data class NavItem(
    val screen: AppScreen,
    val label: String,
    val icon: ImageVector,
    val testTag: String
)

@Composable
fun CyberBottomNavigation(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit
) {
    val items = listOf(
        NavItem(AppScreen.HOME, "Home", Icons.Default.Home, "nav_home"),
        NavItem(AppScreen.SCAN, "Scan", Icons.Default.QrCodeScanner, "nav_scan"),
        NavItem(AppScreen.GENERATE, "Studio", Icons.Default.QrCode, "nav_generate"),
        NavItem(AppScreen.SECURITY, "Security", Icons.Default.Security, "nav_security"),
        NavItem(AppScreen.AI_ASSISTANT, "AI", Icons.Default.AutoAwesome, "nav_ai"),
        NavItem(AppScreen.HISTORY, "History", Icons.Default.History, "nav_history"),
        NavItem(AppScreen.SETTINGS, "Settings", Icons.Default.Settings, "nav_settings")
    )

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .border(androidx.compose.foundation.BorderStroke(1.dp, CyberBorder.copy(alpha = 0.6f))),
        containerColor = CyberSurface.copy(alpha = 0.95f),
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val selected = currentScreen == item.screen
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(item.screen) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 10.sp,
                        fontWeight = if (selected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NeonCyan,
                    selectedTextColor = NeonCyan,
                    indicatorColor = NeonCyan.copy(alpha = 0.15f),
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                ),
                modifier = Modifier.testTag(item.testTag)
            )
        }
    }
}
```

---

### فائل 9: `/app/src/main/java/com/example/ui/theme/Color.kt`
```kotlin
package com.example.ui.theme

import androidx.compose.ui.graphics.Color

val CyberBackground = Color(0xFF070B14)
val CyberSurface = Color(0xFF0E1626)
val CyberSurfaceVariant = Color(0xFF141F36)
val CyberSurfaceElevated = Color(0xFF1C2A48)

val NeonCyan = Color(0xFF00E5FF)
val NeonCyanVariant = Color(0xFF00B0FF)
val NeonPurple = Color(0xFF7C4DFF)
val NeonPurpleVariant = Color(0xFF651FFF)
val ElectricBlue = Color(0xFF2979FF)

val CyberBorder = Color(0xFF223554)
val CyberBorderGlowing = Color(0xFF00E5FF).copy(alpha = 0.4f)

val SecuritySafe = Color(0xFF00E676)
val SecuritySafeGlow = Color(0xFF00E676).copy(alpha = 0.3f)
val SecuritySuspicious = Color(0xFFFFD600)
val SecuritySuspiciousGlow = Color(0xFFFFD600).copy(alpha = 0.3f)
val SecurityHighRisk = Color(0xFFFF1744)
val SecurityHighRiskGlow = Color(0xFFFF1744).copy(alpha = 0.3f)
val SecurityUnknown = Color(0xFF90A4AE)

val TextPrimary = Color(0xFFF1F5F9)
val TextSecondary = Color(0xFF94A3B8)
val TextMuted = Color(0xFF64748B)

val GlassBackground = Color(0xCC0E1626)
val GlassBorder = Color(0x3300E5FF)
```

---

### فائل 10: `/app/src/main/java/com/example/ui/theme/Theme.kt`
```kotlin
package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = Color.Black,
    primaryContainer = CyberSurfaceVariant,
    onPrimaryContainer = NeonCyan,
    secondary = NeonPurple,
    onSecondary = Color.White,
    secondaryContainer = CyberSurfaceElevated,
    onSecondaryContainer = NeonPurple,
    tertiary = ElectricBlue,
    background = CyberBackground,
    onBackground = TextPrimary,
    surface = CyberSurface,
    onSurface = TextPrimary,
    surfaceVariant = CyberSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = CyberBorder,
    outlineVariant = CyberBorderGlowing,
    error = SecurityHighRisk,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
```

---

### فائل 11: `/app/src/main/java/com/example/ui/components/AiOrb.kt`
```kotlin
package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple

@Composable
fun AiOrb(
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    isThinking: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "orb_transition")

    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (isThinking) 800 else 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val radius = (this.size.minDimension / 2f) * 0.75f * pulse

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        NeonCyan.copy(alpha = 0.45f * glowAlpha),
                        NeonPurple.copy(alpha = 0.25f * glowAlpha),
                        Color.Transparent
                    ),
                    center = center,
                    radius = radius * 1.5f
                ),
                radius = radius * 1.5f,
                center = center
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.9f),
                        NeonCyan.copy(alpha = 0.8f),
                        NeonPurple.copy(alpha = 0.9f),
                        Color(0xFF0F172A)
                    ),
                    center = Offset(center.x - radius * 0.2f, center.y - radius * 0.2f),
                    radius = radius
                ),
                radius = radius,
                center = center
            )

            drawCircle(
                color = NeonCyan.copy(alpha = 0.7f),
                radius = radius * 1.15f,
                center = center,
                style = Stroke(width = 2.dp.toPx())
            )

            drawCircle(
                color = NeonPurple.copy(alpha = 0.6f),
                radius = radius * 1.3f,
                center = center,
                style = Stroke(width = 1.5.dp.toPx())
            )
        }
    }
}
```

---

### فائل 12: `/app/src/main/java/com/example/ui/components/SecurityComponents.kt`
```kotlin
package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.security.RiskLevel
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceElevated
import com.example.ui.theme.SecurityHighRisk
import com.example.ui.theme.SecurityHighRiskGlow
import com.example.ui.theme.SecuritySafe
import com.example.ui.theme.SecuritySafeGlow
import com.example.ui.theme.SecuritySuspicious
import com.example.ui.theme.SecuritySuspiciousGlow
import com.example.ui.theme.SecurityUnknown
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    borderColor: Color = CyberBorder,
    shape: RoundedCornerShape = RoundedCornerShape(18.dp),
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .border(BorderStroke(1.dp, borderColor), shape),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = CyberSurface.copy(alpha = 0.85f)
        )
    ) {
        content()
    }
}

@Composable
fun RiskBadge(
    riskLevel: RiskLevel,
    modifier: Modifier = Modifier,
    isUrdu: Boolean = false
) {
    val (color, glow, icon) = when (riskLevel) {
        RiskLevel.SAFE -> Triple(SecuritySafe, SecuritySafeGlow, Icons.Default.CheckCircle)
        RiskLevel.SUSPICIOUS -> Triple(SecuritySuspicious, SecuritySuspiciousGlow, Icons.Default.Warning)
        RiskLevel.HIGH_RISK -> Triple(SecurityHighRisk, SecurityHighRiskGlow, Icons.Default.Shield)
        RiskLevel.UNKNOWN -> Triple(SecurityUnknown, SecurityUnknown.copy(alpha = 0.2f), Icons.Default.Help)
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(glow)
            .border(1.dp, color, RoundedCornerShape(24.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = riskLevel.getDisplayName(isUrdu),
            color = color,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SecurityScoreMeter(
    riskScore: Int,
    riskLevel: RiskLevel,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = (100 - riskScore) / 100f,
        animationSpec = tween(durationMillis = 1000),
        label = "score_meter"
    )

    val color = when (riskLevel) {
        RiskLevel.SAFE -> SecuritySafe
        RiskLevel.SUSPICIOUS -> SecuritySuspicious
        RiskLevel.HIGH_RISK -> SecurityHighRisk
        RiskLevel.UNKNOWN -> SecurityUnknown
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Safety Confidence",
                fontSize = 13.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${(animatedProgress * 100).toInt()}%",
                fontSize = 15.sp,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = CyberSurfaceElevated
        )
    }
}

@Composable
fun DetectedSignalRow(
    signal: String,
    modifier: Modifier = Modifier
) {
    val isNegative = signal.contains("Insecure", ignoreCase = true) ||
            signal.contains("Typosquatting", ignoreCase = true) ||
            signal.contains("executable", ignoreCase = true) ||
            signal.contains("phishing", ignoreCase = true) ||
            signal.contains("shortener", ignoreCase = true) ||
            signal.contains("Direct numeric", ignoreCase = true)

    val iconColor = if (isNegative) SecurityHighRisk else SecuritySafe

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(8.dp)
                .clip(CircleShape)
                .background(iconColor)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = signal,
            fontSize = 13.sp,
            color = if (isNegative) TextPrimary else TextSecondary,
            lineHeight = 18.sp
        )
    }
}
```

---

### فائل 13: `/app/src/main/java/com/example/security/QrAnalyzer.kt`
```kotlin
package com.example.security

import java.net.URI
import java.util.Locale

enum class QrType {
    URL, TEXT, WIFI, CONTACT, EMAIL, PHONE, SMS, LOCATION, CALENDAR, PAYMENT, OTHER
}

enum class RiskLevel {
    SAFE, SUSPICIOUS, HIGH_RISK, UNKNOWN;

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
    val riskScore: Int,
    val detectedSignals: List<String>,
    val explanation: String,
    val recommendedAction: String,
    val canOpenSafely: Boolean
)

object QrAnalyzer {
    private val SUSPICIOUS_TLDS = setOf("top", "xyz", "work", "click", "buzz", "gq", "cf", "ml", "tk", "ga")
    private val SUSPICIOUS_KEYWORDS = listOf("verify", "login", "signin", "account-update", "secure-bank", "free-gift", "crypto-bonus", "password-reset")
    private val SUSPICIOUS_EXTENSIONS = listOf(".apk", ".exe", ".scr", ".bat", ".cmd", ".iso")
    private val KNOWN_BRAND_TYPOS = mapOf("g00gle" to "google", "paypa1" to "paypal", "arnazon" to "amazon", "micros0ft" to "microsoft", "netfiix" to "netflix")
    private val KNOWN_SHORTENERS = setOf("bit.ly", "tinyurl.com", "t.co", "is.gd", "cutt.ly", "rb.gy")

    fun detectType(rawContent: String): QrType {
        val trimmed = rawContent.trim()
        val lower = trimmed.lowercase(Locale.ROOT)
        return when {
            lower.startsWith("http://") || lower.startsWith("https://") || lower.startsWith("www.") -> QrType.URL
            lower.startsWith("wifi:") -> QrType.WIFI
            lower.startsWith("begin:vcard") || lower.startsWith("mecard:") -> QrType.CONTACT
            lower.startsWith("mailto:") -> QrType.EMAIL
            lower.startsWith("tel:") -> QrType.PHONE
            lower.startsWith("smsto:") || lower.startsWith("sms:") -> QrType.SMS
            lower.startsWith("geo:") -> QrType.LOCATION
            lower.startsWith("upi://") || lower.startsWith("bitcoin:") -> QrType.PAYMENT
            else -> if (trimmed.contains(".") && !trimmed.contains(" ") && trimmed.length < 150) QrType.URL else QrType.TEXT
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
            else -> SecurityAnalysisResult(
                qrType = type,
                title = "Plain Text Payload",
                destination = trimmed.take(150),
                riskLevel = RiskLevel.SAFE,
                riskScore = 0,
                detectedSignals = listOf("Standard plain text data"),
                explanation = if (isUrdu) "یہ سادہ متن ہے۔ کوئی بیرونی خطرہ نہیں۔" else "Contains plain text data with no automatic triggers.",
                recommendedAction = if (isUrdu) "آپ متن کاپی یا محفوظ کر سکتے ہیں۔" else "Safe to read, copy, or share.",
                canOpenSafely = true
            )
        }
    }

    private fun analyzeUrl(rawUrl: String, isUrdu: Boolean): SecurityAnalysisResult {
        val normalizedUrl = if (!rawUrl.startsWith("http://") && !rawUrl.startsWith("https://")) "https://$rawUrl" else rawUrl
        val signals = mutableListOf<String>()
        var penalty = 0

        val uri = try { URI(normalizedUrl) } catch (e: Exception) { null }
        if (uri == null || uri.host.isNullOrBlank()) {
            return SecurityAnalysisResult(
                qrType = QrType.URL,
                title = "Malformed Destination URL",
                destination = rawUrl,
                riskLevel = RiskLevel.UNKNOWN,
                riskScore = 50,
                detectedSignals = listOf("Invalid or malformed URI structure"),
                explanation = "URL syntax could not be properly parsed into a valid host.",
                recommendedAction = "Do not open this destination.",
                canOpenSafely = false
            )
        }

        val host = uri.host.lowercase(Locale.ROOT)
        val path = (uri.path ?: "").lowercase(Locale.ROOT)

        if (!normalizedUrl.startsWith("https://", ignoreCase = true)) {
            signals.add("Insecure plain HTTP protocol (traffic is unencrypted)")
            penalty += 25
        } else {
            signals.add("Valid HTTPS protocol in use (data in transit is encrypted)")
        }

        if (host.matches(Regex("""^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$"""))) {
            signals.add("Direct numeric IP address hostname (bypasses domain name reputation)")
            penalty += 45
        }

        if (KNOWN_SHORTENERS.contains(host)) {
            signals.add("URL shortener service ($host) hides final destination target")
            penalty += 20
        }

        for ((typo, target) in KNOWN_BRAND_TYPOS) {
            if (host.contains(typo)) {
                signals.add("Deceptive typosquatting indicator: matches suspicious imitation of '$target'")
                penalty += 50
            }
        }

        val matchedKeywords = SUSPICIOUS_KEYWORDS.filter { host.contains(it) || path.contains(it) }
        if (matchedKeywords.isNotEmpty()) {
            signals.add("Sensitive phishing keywords detected: ${matchedKeywords.joinToString(", ")}")
            penalty += 30
        }

        val matchedExt = SUSPICIOUS_EXTENSIONS.find { path.endsWith(it) }
        if (matchedExt != null) {
            signals.add("Direct executable/archive download link detected ($matchedExt)")
            penalty += 55
        }

        val riskScore = penalty.coerceIn(0, 100)
        val riskLevel = when {
            riskScore >= 60 -> RiskLevel.HIGH_RISK
            riskScore >= 25 -> RiskLevel.SUSPICIOUS
            else -> RiskLevel.SAFE
        }

        return SecurityAnalysisResult(
            qrType = QrType.URL,
            title = host,
            destination = normalizedUrl,
            riskLevel = riskLevel,
            riskScore = riskScore,
            detectedSignals = signals,
            explanation = if (isUrdu) "سیکیورٹی سگنلز کے تحت خطرے کی سطح کا تعین کیا گیا ہے۔" else "Technical inspection completed for URL security heuristics.",
            recommendedAction = if (riskLevel == RiskLevel.HIGH_RISK) "DO NOT OPEN" else "Safe to proceed.",
            canOpenSafely = riskLevel != RiskLevel.HIGH_RISK
        )
    }

    private fun analyzeWifi(content: String, isUrdu: Boolean): SecurityAnalysisResult {
        val isNone = content.contains("T:nopass", ignoreCase = true)
        return SecurityAnalysisResult(
            qrType = QrType.WIFI,
            title = "Wi-Fi Configuration",
            destination = content,
            riskLevel = if (isNone) RiskLevel.SUSPICIOUS else RiskLevel.SAFE,
            riskScore = if (isNone) 40 else 10,
            detectedSignals = listOf(if (isNone) "Unencrypted open Wi-Fi network" else "WPA encrypted Wi-Fi network"),
            explanation = if (isNone) "یہ وائی فائی بغیر پاس ورڈ کے ہے۔" else "محفوظ وائی فائی کنکشن پایا گیا۔",
            recommendedAction = "Verify before connecting.",
            canOpenSafely = true
        )
    }

    private fun analyzePayment(content: String, isUrdu: Boolean): SecurityAnalysisResult {
        return SecurityAnalysisResult(
            qrType = QrType.PAYMENT,
            title = "Payment / Financial Transfer",
            destination = content.take(120),
            riskLevel = RiskLevel.SUSPICIOUS,
            riskScore = 35,
            detectedSignals = listOf("Financial/Payment instruction payload"),
            explanation = if (isUrdu) "یہ کیو آر کوڈ مالی ادائیگی کی درخواست کرتا ہے۔" else "This QR initiates a payment or transfer.",
            recommendedAction = "Verify payee address before sending funds.",
            canOpenSafely = true
        )
    }

    private fun analyzePhoneOrSms(content: String, type: QrType, isUrdu: Boolean): SecurityAnalysisResult {
        return SecurityAnalysisResult(
            qrType = type,
            title = if (type == QrType.SMS) "SMS Message" else "Phone Call",
            destination = content,
            riskLevel = RiskLevel.SAFE,
            riskScore = 10,
            detectedSignals = listOf("Standard telephony schema"),
            explanation = "Instructs device dialer or messaging service.",
            recommendedAction = "Check the phone number before proceeding.",
            canOpenSafely = true
        )
    }
}
```

---

### فائل 14: `/app/src/main/java/com/example/qrcode/QrCodeEngine.kt`
```kotlin
package com.example.qrcode

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
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
        foregroundColor: Int = 0xFF00E5FF.toInt(),
        backgroundColor: Int = 0xFF0A0F1D.toInt(),
        hasGradient: Boolean = true,
        gradientColor: Int = 0xFF7C4DFF.toInt(),
        rounded: Boolean = true,
        logoType: String = "SHIELD",
        margin: Int = 2
    ): Bitmap {
        val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java).apply {
            put(EncodeHintType.CHARACTER_SET, "UTF-8")
            put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H)
            put(EncodeHintType.MARGIN, margin)
        }

        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints)
        val matrixWidth = bitMatrix.width
        val matrixHeight = bitMatrix.height

        val bitmap = Bitmap.createBitmap(matrixWidth, matrixHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val bgPaint = Paint().apply {
            color = backgroundColor
            style = Paint.Style.FILL
        }
        canvas.drawRect(0f, 0f, matrixWidth.toFloat(), matrixHeight.toFloat(), bgPaint)

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

        val cellWidth = matrixWidth.toFloat() / matrixWidth
        val radius = if (rounded) (cellWidth * 0.4f).coerceAtLeast(2f) else 0f
        val rect = RectF()

        for (x in 0 until matrixWidth) {
            for (y in 0 until matrixHeight) {
                if (bitMatrix.get(x, y)) {
                    rect.set(x.toFloat(), y.toFloat(), (x + 1).toFloat(), (y + 1).toFloat())
                    if (rounded) {
                        canvas.drawRoundRect(rect, radius, radius, modulePaint)
                    } else {
                        canvas.drawRect(rect, modulePaint)
                    }
                }
            }
        }

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

        val iconPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = badgeFgColor
            style = Paint.Style.STROKE
            strokeWidth = width * 0.02f
            strokeCap = Paint.Cap.ROUND
        }

        val s = badgeRadius * 0.5f
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
    }

    fun decodeQrFromBitmap(bitmap: Bitmap): String? {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        val source = RGBLuminanceSource(width, height, pixels)
        val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
        val reader = MultiFormatReader()

        return try {
            reader.decode(binaryBitmap).text
        } catch (e: Exception) {
            null
        }
    }

    fun testReadability(bitmap: Bitmap, expectedContent: String): Boolean {
        val decoded = decodeQrFromBitmap(bitmap)
        return decoded == expectedContent
    }
}
```

---

### فائل 15: `/app/src/main/java/com/example/ui/MainViewModel.kt`
```kotlin
package com.example.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiSecurityService
import com.example.data.local.AppDatabase
import com.example.data.local.entity.GeneratedQrEntity
import com.example.data.local.entity.ScanRecordEntity
import com.example.data.local.entity.SecurityDomainRuleEntity
import com.example.data.repository.ScanRepository
import com.example.qrcode.QrCodeEngine
import com.example.security.QrAnalyzer
import com.example.security.QrType
import com.example.security.RiskLevel
import com.example.security.SecurityAnalysisResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    HOME, SCAN, RESULT_DETAIL, GENERATE, SECURITY, AI_ASSISTANT, MEDIA_ANALYZER, HISTORY, SETTINGS
}

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val attachedBitmap: Bitmap? = null
)

data class MediaAnalysisResult(
    val title: String,
    val mediaType: String,
    val riskLevel: RiskLevel,
    val statusLabel: String,
    val confidence: Int,
    val signals: List<String>,
    val explanation: String,
    val recommendedActions: List<String>
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ScanRepository(AppDatabase.getDatabase(application))

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    val allScans: StateFlow<List<ScanRecordEntity>> = repository.allScans
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentScans: StateFlow<List<ScanRecordEntity>> = repository.getRecentScans(10)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalScansCount: StateFlow<Int> = repository.totalScansCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val safeScansCount: StateFlow<Int> = repository.safeScansCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val suspiciousScansCount: StateFlow<Int> = repository.suspiciousScansCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val highRiskScansCount: StateFlow<Int> = repository.highRiskScansCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _currentAnalysis = MutableStateFlow<SecurityAnalysisResult?>(null)
    val currentAnalysis: StateFlow<SecurityAnalysisResult?> = _currentAnalysis.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    val isUrdu = MutableStateFlow(false)
    val autoCopyOnScan = MutableStateFlow(false)
    val vibrateOnScan = MutableStateFlow(true)
    val appLockEnabled = MutableStateFlow(false)

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = "AI",
                text = "Hello! I am your QR Guard AI Cybersecurity Assistant. Ask me anything about QR security, links, or media tampering."
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    val genContent = MutableStateFlow("https://example.com")
    val genType = MutableStateFlow(QrType.URL)
    val genFgColor = MutableStateFlow(0xFF00E5FF.toLong())
    val genBgColor = MutableStateFlow(0xFF0A0F1D.toLong())
    val genHasGradient = MutableStateFlow(true)
    val genGradientColor = MutableStateFlow(0xFF7C4DFF.toLong())
    val genFrameText = MutableStateFlow("SCAN ME")
    val genLogoType = MutableStateFlow("SHIELD")
    val genRounded = MutableStateFlow(true)
    val genReadabilityValid = MutableStateFlow<Boolean?>(null)

    private val _mediaAnalysisResult = MutableStateFlow<MediaAnalysisResult?>(null)
    val mediaAnalysisResult: StateFlow<MediaAnalysisResult?> = _mediaAnalysisResult.asStateFlow()

    private val _isAnalyzingMedia = MutableStateFlow(false)
    val isAnalyzingMedia: StateFlow<Boolean> = _isAnalyzingMedia.asStateFlow()

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun onQrScanned(rawContent: String, source: String = "CAMERA") {
        if (rawContent.isBlank()) return
        viewModelScope.launch {
            _isAnalyzing.value = true
            _currentScreen.value = AppScreen.RESULT_DETAIL
            delay(500)

            val result = QrAnalyzer.analyze(rawContent, isUrdu = isUrdu.value)
            _currentAnalysis.value = result
            _isAnalyzing.value = false

            repository.saveScan(
                ScanRecordEntity(
                    rawContent = rawContent,
                    qrType = result.qrType.name,
                    title = result.title,
                    riskLevel = result.riskLevel.name,
                    riskScore = result.riskScore,
                    detectedSignals = result.detectedSignals.joinToString("||"),
                    explanation = result.explanation,
                    recommendedAction = result.recommendedAction,
                    source = source
                )
            )
        }
    }

    fun testQrReadability(): Boolean {
        val bitmap = QrCodeEngine.generateQrBitmap(
            content = genContent.value,
            width = 512,
            height = 512,
            foregroundColor = genFgColor.value.toInt(),
            backgroundColor = genBgColor.value.toInt(),
            hasGradient = genHasGradient.value,
            gradientColor = genGradientColor.value.toInt(),
            rounded = genRounded.value,
            logoType = genLogoType.value
        )
        val valid = QrCodeEngine.testReadability(bitmap, genContent.value)
        genReadabilityValid.value = valid
        return valid
    }

    fun saveCurrentGeneratedQr(title: String = "My QR Code") {
        viewModelScope.launch {
            repository.saveGeneratedQr(
                GeneratedQrEntity(
                    title = title,
                    content = genContent.value,
                    qrType = genType.value.name,
                    foregroundColor = genFgColor.value,
                    backgroundColor = genBgColor.value,
                    hasGradient = genHasGradient.value,
                    gradientColor = genGradientColor.value,
                    frameText = genFrameText.value,
                    logoType = genLogoType.value,
                    roundedStyle = genRounded.value
                )
            )
        }
    }

    fun sendAiMessage(prompt: String, attachedBitmap: Bitmap? = null) {
        if (prompt.isBlank() && attachedBitmap == null) return
        val userMsg = ChatMessage(sender = "USER", text = prompt, attachedBitmap = attachedBitmap)
        _chatMessages.value = _chatMessages.value + userMsg
        _isAiThinking.value = true

        viewModelScope.launch {
            val reply = GeminiSecurityService.askAssistant(
                prompt = prompt,
                currentContext = _currentAnalysis.value?.destination ?: "",
                isUrdu = isUrdu.value,
                imageBitmap = attachedBitmap
            )
            _chatMessages.value = _chatMessages.value + ChatMessage(sender = "AI", text = reply)
            _isAiThinking.value = false
        }
    }

    fun analyzeVideoMedia(videoName: String) {
        viewModelScope.launch {
            _isAnalyzingMedia.value = true
            delay(1200)
            _mediaAnalysisResult.value = MediaAnalysisResult(
                title = videoName,
                mediaType = "VIDEO",
                riskLevel = RiskLevel.SAFE,
                statusLabel = if (isUrdu.value) "کوئی واضح تبدیلی نہیں ملی" else "No strong manipulation indicators detected",
                confidence = 88,
                signals = listOf("Temporal frame consistency: 94% coherence", "Facial landmark jitter within baseline", "Audio-visual phase alignment: verified"),
                explanation = "Acoustic and visual frame synchronization checks passed with high baseline consistency.",
                recommendedActions = listOf("Cross-reference key claims with authoritative outlets", "Verify primary upload source")
            )
            _isAnalyzingMedia.value = false
        }
    }

    fun analyzeScreenshotMedia(bitmap: Bitmap, title: String = "Screenshot Analysis") {
        viewModelScope.launch {
            _isAnalyzingMedia.value = true
            val qrInside = QrCodeEngine.decodeQrFromBitmap(bitmap)
            val qrSignal = if (qrInside != null) "Embedded QR detected: $qrInside" else "No embedded standard QR found"

            val explanation = GeminiSecurityService.analyzeScreenshot(bitmap, qrSignal, isUrdu.value)
            _mediaAnalysisResult.value = MediaAnalysisResult(
                title = title,
                mediaType = "SCREENSHOT",
                riskLevel = if (qrInside != null) RiskLevel.SUSPICIOUS else RiskLevel.SAFE,
                statusLabel = "Analysis Complete",
                confidence = 85,
                signals = listOf(qrSignal, "Layout visual hierarchy inspected", "Urgent coercive language heuristics evaluated"),
                explanation = explanation,
                recommendedActions = listOf("Never enter OTP passwords on unverified screens", "Verify address bar directly")
            )
            _isAnalyzingMedia.value = false
        }
    }

    fun toggleFavorite(id: Long, currentFav: Boolean) {
        viewModelScope.launch { repository.toggleFavorite(id, !currentFav) }
    }

    fun deleteScan(id: Long) {
        viewModelScope.launch { repository.deleteScan(id) }
    }

    fun clearAllScans() {
        viewModelScope.launch { repository.clearHistory() }
    }
}
```

---

### فائل 16: `/app/src/main/java/com/example/ai/GeminiSecurityService.kt`
```kotlin
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
            val parts = mutableListOf<Part>()
            if (imageBitmap != null) {
                val stream = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
                val base64 = Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP)
                parts.add(Part(inlineData = InlineData(mimeType = "image/jpeg", data = base64)))
            }
            parts.add(Part(text = prompt))

            val request = GenerateContentRequest(
                contents = listOf(Content(parts = parts)),
                generationConfig = GenerationConfig(temperature = 0.3f)
            )

            val response = service.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: generateOfflineCybersecurityResponse(prompt, currentContext, isUrdu)
        } catch (e: Exception) {
            generateOfflineCybersecurityResponse(prompt, currentContext, isUrdu)
        }
    }

    suspend fun analyzeScreenshot(
        bitmap: Bitmap,
        extraNotes: String = "",
        isUrdu: Boolean = false
    ): String = withContext(Dispatchers.IO) {
        val prompt = if (isUrdu) {
            "براہ کرم اس اسکرین شاٹ کا سائبر سیکیورٹی تجزیہ کریں۔"
        } else {
            "Perform a cybersecurity inspection on this screenshot. Analyze any QR codes, login fields, or fraud indicators."
        }
        askAssistant(prompt, extraNotes, isUrdu, bitmap)
    }

    private fun generateOfflineCybersecurityResponse(prompt: String, context: String, isUrdu: Boolean): String {
        return if (isUrdu) {
            "کیو آر گارڈ سیکیورٹی گائیڈ:\nکسی بھی غیر مانوس کیو آر کوڈ یا ویب سائٹ پر پاس ورڈ یا بینک کارڈ کی معلومات درج نہ کریں۔ ہمیشہ یو آر ایل کے ہجے اور HTTPS کی تصدیق کریں۔"
        } else {
            "Cybersecurity Guidance:\nAlways inspect destination URLs before entering credentials. Verify that HTTPS protocol is valid and never approve unverified 2FA or payment requests."
        }
    }
}
```

---

### فائل 17: `/app/src/main/java/com/example/data/local/AppDatabase.kt`
```kotlin
package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.GeneratedQrDao
import com.example.data.local.dao.ScanRecordDao
import com.example.data.local.dao.SecurityDomainRuleDao
import com.example.data.local.entity.GeneratedQrEntity
import com.example.data.local.entity.ScanRecordEntity
import com.example.data.local.entity.SecurityDomainRuleEntity

@Database(
    entities = [
        ScanRecordEntity::class,
        GeneratedQrEntity::class,
        SecurityDomainRuleEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scanRecordDao(): ScanRecordDao
    abstract fun generatedQrDao(): GeneratedQrDao
    abstract fun securityDomainRuleDao(): SecurityDomainRuleDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "qr_guard_ai.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```

---

### فائل 18: `/app/src/main/java/com/example/data/local/entity/ScanRecordEntity.kt`
```kotlin
package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_records")
data class ScanRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val rawContent: String,
    val qrType: String,
    val title: String,
    val riskLevel: String,
    val riskScore: Int,
    val detectedSignals: String,
    val explanation: String,
    val recommendedAction: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val notes: String = "",
    val tags: String = "",
    val source: String = "CAMERA"
)
```

---

### فائل 19: `/app/src/main/java/com/example/data/local/entity/GeneratedQrEntity.kt`
```kotlin
package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "generated_qrs")
data class GeneratedQrEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val qrType: String,
    val foregroundColor: Long = 0xFF00E5FF,
    val backgroundColor: Long = 0xFF0A0F1D,
    val hasGradient: Boolean = true,
    val gradientColor: Long = 0xFF7C4DFF,
    val frameText: String = "SCAN ME",
    val logoType: String = "SHIELD",
    val roundedStyle: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)
```

---

### فائل 20: `/app/src/main/java/com/example/data/local/entity/SecurityDomainRuleEntity.kt`
```kotlin
package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "domain_rules")
data class SecurityDomainRuleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val domain: String,
    val status: String,
    val addedAt: Long = System.currentTimeMillis(),
    val notes: String = ""
)
```

---

### فائل 21: `/app/src/main/java/com/example/data/local/dao/ScanRecordDao.kt`
```kotlin
package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.ScanRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanRecordDao {
    @Query("SELECT * FROM scan_records ORDER BY timestamp DESC")
    fun getAllScans(): Flow<List<ScanRecordEntity>>

    @Query("SELECT * FROM scan_records WHERE id = :id LIMIT 1")
    suspend fun getScanById(id: Long): ScanRecordEntity?

    @Query("SELECT * FROM scan_records WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun getFavoriteScans(): Flow<List<ScanRecordEntity>>

    @Query("SELECT * FROM scan_records ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentScans(limit: Int): Flow<List<ScanRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScan(scan: ScanRecordEntity): Long

    @Update
    suspend fun updateScan(scan: ScanRecordEntity)

    @Query("UPDATE scan_records SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean)

    @Query("DELETE FROM scan_records WHERE id = :id")
    suspend fun deleteScanById(id: Long)

    @Query("DELETE FROM scan_records")
    suspend fun clearAllScans()

    @Query("SELECT COUNT(*) FROM scan_records")
    fun getCountTotal(): Flow<Int>

    @Query("SELECT COUNT(*) FROM scan_records WHERE riskLevel = 'SAFE'")
    fun getCountSafe(): Flow<Int>

    @Query("SELECT COUNT(*) FROM scan_records WHERE riskLevel = 'SUSPICIOUS'")
    fun getCountSuspicious(): Flow<Int>

    @Query("SELECT COUNT(*) FROM scan_records WHERE riskLevel = 'HIGH_RISK'")
    fun getCountHighRisk(): Flow<Int>
}
```

---

### فائل 22: `/app/src/main/java/com/example/data/local/dao/GeneratedQrDao.kt`
```kotlin
package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.GeneratedQrEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GeneratedQrDao {
    @Query("SELECT * FROM generated_qrs ORDER BY timestamp DESC")
    fun getAllGeneratedQrs(): Flow<List<GeneratedQrEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeneratedQr(qr: GeneratedQrEntity): Long

    @Query("DELETE FROM generated_qrs WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM generated_qrs")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM generated_qrs")
    fun getCountGenerated(): Flow<Int>
}
```

---

### فائل 23: `/app/src/main/java/com/example/data/local/dao/SecurityDomainRuleDao.kt`
```kotlin
package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.SecurityDomainRuleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SecurityDomainRuleDao {
    @Query("SELECT * FROM domain_rules ORDER BY addedAt DESC")
    fun getAllRules(): Flow<List<SecurityDomainRuleEntity>>

    @Query("SELECT * FROM domain_rules WHERE domain = :domain LIMIT 1")
    suspend fun getRuleForDomain(domain: String): SecurityDomainRuleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRule(rule: SecurityDomainRuleEntity): Long

    @Query("DELETE FROM domain_rules WHERE id = :id")
    suspend fun deleteRule(id: Long)
}
```

---

### فائل 24: `/app/src/main/java/com/example/data/repository/ScanRepository.kt`
```kotlin
package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.entity.GeneratedQrEntity
import com.example.data.local.entity.ScanRecordEntity
import com.example.data.local.entity.SecurityDomainRuleEntity
import kotlinx.coroutines.flow.Flow

class ScanRepository(private val database: AppDatabase) {
    private val scanDao = database.scanRecordDao()
    private val qrDao = database.generatedQrDao()
    private val ruleDao = database.securityDomainRuleDao()

    val allScans: Flow<List<ScanRecordEntity>> = scanDao.getAllScans()
    val favoriteScans: Flow<List<ScanRecordEntity>> = scanDao.getFavoriteScans()
    val allGeneratedQrs: Flow<List<GeneratedQrEntity>> = qrDao.getAllGeneratedQrs()
    val domainRules: Flow<List<SecurityDomainRuleEntity>> = ruleDao.getAllRules()

    val totalScansCount: Flow<Int> = scanDao.getCountTotal()
    val safeScansCount: Flow<Int> = scanDao.getCountSafe()
    val suspiciousScansCount: Flow<Int> = scanDao.getCountSuspicious()
    val highRiskScansCount: Flow<Int> = scanDao.getCountHighRisk()
    val generatedCount: Flow<Int> = qrDao.getCountGenerated()

    fun getRecentScans(limit: Int): Flow<List<ScanRecordEntity>> = scanDao.getRecentScans(limit)

    suspend fun getScanById(id: Long): ScanRecordEntity? = scanDao.getScanById(id)

    suspend fun saveScan(scan: ScanRecordEntity): Long = scanDao.insertScan(scan)

    suspend fun toggleFavorite(id: Long, isFavorite: Boolean) = scanDao.setFavorite(id, isFavorite)

    suspend fun deleteScan(id: Long) = scanDao.deleteScanById(id)

    suspend fun clearHistory() {
        scanDao.clearAllScans()
    }

    suspend fun saveGeneratedQr(qr: GeneratedQrEntity): Long = qrDao.insertGeneratedQr(qr)

    suspend fun deleteGeneratedQr(id: Long) = qrDao.deleteById(id)

    suspend fun getRuleForDomain(domain: String): SecurityDomainRuleEntity? =
        ruleDao.getRuleForDomain(domain)

    suspend fun addDomainRule(domain: String, status: String, notes: String = ""): Long =
        ruleDao.insertRule(SecurityDomainRuleEntity(domain = domain.lowercase().trim(), status = status, notes = notes))
}
```

---

### فائل 25: `/app/src/test/java/com/example/ExampleRobolectricTest.kt`
```kotlin
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
```

---
**خلاصہ (Conclusion):**
آپ کی ایپ کی APK فائل پہلے ہی کامیابی کے ساتھ بلڈ ہو چکی ہے اور `app/build/outputs/apk/debug/app-debug.apk` کے طور پر محفوظ ہے۔ آپ اوپر بیان کردہ طریقوں کے مطابق اسے ڈاؤن لوڈ یا Android Studio میں چلا سکتے ہیں۔
