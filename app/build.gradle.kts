plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ktorfit)
}

android {
    namespace = "com.ghost.android.test"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ghost.android.test"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = "17"
    }
    
    buildFeatures {
        compose = true
    }

    sourceSets {
        getByName("main") {
            manifest.srcFile("src/main/AndroidManifest.xml")
        }
    }
}

ksp {
    arg("ghost.moduleName", "app")
}

dependencies {
    // AndroidX & Lifecycle
    implementation(libs.bundles.androidx.essential)
    implementation(libs.androidx.metrics)
    
    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose.ui)
    
    // Ghost Serialization (Consumption from Catalog)
    implementation(libs.ghost.api)
    implementation(libs.ghost.serialization)
    implementation(libs.ghost.retrofit)
    implementation(libs.ghost.ktor)
    ksp(libs.ghost.compiler)

    // Benchmark Contenders & Networking
    implementation(libs.bundles.retrofit.moshi)
    implementation(libs.retrofit.gson)
    implementation(libs.bundles.ktor.moshi)
    implementation(libs.ktorfit.lib)
    ksp(libs.ktorfit.ksp)
    implementation(libs.ktor.serialization.kotlinx)
    implementation(libs.gson)
    implementation(libs.kotlinx.serialization.json)

    // Networking & Image Loading
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.ktor.client.okhttp)
}
