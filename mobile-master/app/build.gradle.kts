import java.util.Properties

// build.gradle.kts (app module)

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.user_module"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.user_module"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Load environment variables from local.properties
        val localProperties = File(rootDir, "local.properties")
        val properties = Properties()

        if (localProperties.exists()) {
            properties.load(localProperties.inputStream())
        }

        val smtpHost: String = properties.getProperty("smtpHost", "")
        val smtpPort: String = properties.getProperty("smtpPort", "")
        val emailAddress: String = properties.getProperty("emailAddress", "")
        val emailPassword: String = properties.getProperty("emailPassword", "")

        // Pass them as buildConfigFields
        buildConfigField("String", "SMTP_HOST", "\"$smtpHost\"")
        buildConfigField("String", "SMTP_PORT", "\"$smtpPort\"")
        buildConfigField("String", "EMAIL_ADDRESS", "\"$emailAddress\"")
        buildConfigField("String", "EMAIL_PASSWORD", "\"$emailPassword\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    // Enable BuildConfig generation
    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.javamail)
    implementation(libs.activation)
    implementation(libs.bcrypt)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.volley)
    implementation(libs.room.common)
    implementation(libs.room.runtime)
    implementation(libs.swiperefreshlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    annotationProcessor(libs.room.compiler)
    implementation(libs.appcompat) // AppCompat via alias
    implementation(libs.material) // Material components
    implementation(libs.activity) // AndroidX Activity
    implementation(libs.constraintlayout) // Constraint Layout
    testImplementation(libs.junit) // JUnit for testing
    androidTestImplementation(libs.ext.junit) // AndroidX JUnit extension
    androidTestImplementation(libs.espresso.core) // Espresso for UI tests

    // Ajoutez ces deux lignes pour Room (en Java)
    implementation("androidx.room:room-runtime:2.4.3")  // Room runtime
    annotationProcessor("androidx.room:room-compiler:2.4.3")  // Room compiler pour Java

    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0") // For annota




//
//
//    // Core Android and Kotlin dependencies
//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.lifecycle.runtime.ktx)
//    implementation(libs.androidx.activity.compose)
//    implementation(platform(libs.androidx.compose.bom))
//    implementation(libs.androidx.ui)
//    implementation(libs.androidx.ui.graphics)
//    implementation(libs.androidx.ui.tooling.preview)
//    implementation(libs.androidx.material3)
//
//    implementation("androidx.recyclerview:recyclerview:1.2.1") // Add this line for RecyclerView
//
//    implementation("androidx.appcompat:appcompat:1.6.1")  // Ensure this line is included for AppCompatActivity
//
//    implementation ("com.google.android.material:material:1.8.0") // or the latest version
//
//
//
//    // Test dependencies
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//    androidTestImplementation(platform(libs.androidx.compose.bom))
//    androidTestImplementation(libs.androidx.ui.test.junit4)
//    debugImplementation(libs.androidx.ui.tooling)
//    debugImplementation(libs.androidx.ui.test.manifest)
//
//    // Room dependencies
//    implementation("androidx.room:room-runtime:2.4.3")  // Room database runtime
//    implementation("androidx.room:room-ktx:2.4.3")  // Room with Kotlin support
//    kapt("androidx.room:room-compiler:2.4.3")  // Room annotation processor
//
//    // Optional: For Room testing
//    testImplementation("androidx.room:room-testing:2.4.3")
//
//    // Glide for image loading (optional, if you need it)
//    implementation("com.github.bumptech.glide:glide:4.12.0")
//    kapt("com.github.bumptech.glide:compiler:4.12.0")
}
