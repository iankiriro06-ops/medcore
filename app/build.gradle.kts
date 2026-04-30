plugins {
    alias(libs.plugins.android.application)
    id("org.jetbrains.kotlin.android")    // ← no version, uses what AGP already loaded
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.gms.google.services)
}
android {
    namespace  = "com.example.medcore"
    compileSdk = 35

    defaultConfig {
        applicationId             = "com.example.medcore"
        minSdk                    = 24
        targetSdk                 = 35
        versionCode               = 1
        versionName               = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
}

// ← outside android{}, not inside it
kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
}

dependencies {
    // ── Core ──────────────────────────────────────────────────────────────────
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)   // ← added
    implementation(libs.androidx.activity.compose)

    // ── Compose BOM ───────────────────────────────────────────────────────────
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons)        // ← added: needed for all icons

    // ── Navigation ────────────────────────────────────────────────────────────
    implementation(libs.androidx.navigation.compose)            // ← fixed: was navigation-runtime-ktx
    //   (that one has no NavHost/composable())

    // ── Hilt ─────────────────────────────────────────────────────────────────
    implementation(libs.hilt.android)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)                           // ← added
    ksp(libs.hilt.android.compiler)                             // ← added: ksp(), NOT implementation
    implementation(libs.androidx.hilt.navigation.compose)       // ← added

    // ── Room ─────────────────────────────────────────────────────────────────
    implementation(libs.androidx.room.runtime)                  // ← added
    implementation(libs.androidx.room.ktx)                      // ← added
    ksp(libs.androidx.room.compiler)                            // ← added: ksp(), NOT implementation

    // ── DataStore ─────────────────────────────────────────────────────────────
    implementation(libs.androidx.datastore.preferences)         // ← added

    // ── Images ────────────────────────────────────────────────────────────────
    implementation(libs.coil.compose)                           // ← added

    // ── Splash screen ─────────────────────────────────────────────────────────
    implementation(libs.androidx.core.splashscreen)             // ← added

    // ── Testing ───────────────────────────────────────────────────────────────
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    // ← removed: libs.androidx.navigation.runtime.ktx  (wrong artifact)
    // ← removed: libs.androidx.compose.material3.lint  (lint artifact, not a runtime dep)
}