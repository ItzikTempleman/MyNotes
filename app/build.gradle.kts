

plugins {
    kotlin("kapt")
    id("com.android.application")
    id("org.jetbrains.kotlin.android") version "1.9.0"
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp") version "1.9.0-1.0.13"
    id("com.google.gms.google-services")
}


android {
    namespace = "com.itzik.mynotes"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.itzik.mynotes"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }


    kapt {

        correctErrorTypes = true

    }

    kotlinOptions {
        jvmTarget = "17"

    }


    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.addAll(listOf(
        "--add-exports=jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED"
    ))
}

// Dependencies
dependencies {
    implementation(libs.play.services.maps)
    implementation(libs.androidx.room.ktx) // Google Play Services Maps

    // Kapt and KSP for annotation processors
    ksp("androidx.room:room-compiler:2.6.0")
    ksp("com.google.dagger:hilt-compiler:2.50")

    implementation(libs.hilt.android)
    annotationProcessor(libs.room.compiler)
    implementation(libs.coil.compose)

    implementation(libs.kotlin.parcelize.runtime)

    implementation(libs.material)
    implementation(libs.androidx.material.v173)
    implementation(libs.androidx.material.icons.core.v173)
    implementation(libs.androidx.material.icons.extended.v173)

    // Jetpack Compose and Material dependencies
    implementation(libs.androidx.compose.ui.ui.tooling.preview2)
    implementation(platform(libs.androidx.compose.bom.v20240903))


    implementation(libs.androidx.compose.material3.material3)
    implementation(libs.androidx.navigation.compose)

    // Kotlin Coroutines and Serialization
    implementation(libs.kotlinx.coroutines.core.v180)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)

    // Retrofit and Gson for API calls
    implementation(libs.gson)
    implementation(libs.converter.gson)
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)

    // AndroidX Lifecycle and ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx.v270)
    implementation(libs.androidx.lifecycle.viewmodel.compose.v270)
    implementation(libs.androidx.lifecycle.livedata.ktx.v270)
    implementation(libs.androidx.lifecycle.runtime.ktx.v284)
    implementation(libs.androidx.lifecycle.runtime.compose.v270)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate.v270)

    // Jetpack Compose UI dependencies
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.activity.compose.v191)
    implementation(libs.androidx.compose.ui.ui2)
    implementation(libs.androidx.compose.ui.ui.graphics)
    implementation(libs.androidx.compose.ui.ui.tooling.preview)

    // AppCompat and Core KTX
    implementation(libs.androidx.appcompat.v161)
    implementation(libs.androidx.core.ktx)

    // Google Play Services and Firebase
    implementation(libs.play.services.location.v2120)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    // Testing dependencies
    androidTestImplementation(platform(libs.androidx.compose.bom.v20240903))
    androidTestImplementation(libs.androidx.compose.ui.ui.test.junit42)
    debugImplementation(libs.androidx.compose.ui.ui.test.manifest2)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.v121)
    androidTestImplementation(libs.androidx.espresso.core.v361)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.core)
    testImplementation(libs.byte.buddy)
    testImplementation(libs.androidx.core.testing)
}
