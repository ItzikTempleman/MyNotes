plugins {
    kotlin("kapt")
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
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

    kapt {
        correctErrorTypes = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
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

dependencies {



    implementation(libs.play.services.maps)

    //noinspection KaptUsageInsteadOfKsp
    kapt(libs.room.compiler)
    kapt(libs.hilt.compiler)
    annotationProcessor(libs.room.compiler)
    implementation(libs.coil.compose)

    implementation (libs.androidx.compose.ui.ui.tooling.preview2)
    implementation(platform(libs.androidx.compose.bom.v20240800))
    implementation(libs.material.v1110)
    implementation(libs.androidx.material.v166)
    implementation(libs.androidx.material.icons.core.v166)
    implementation(libs.androidx.material.icons.extended.v166)
    implementation(libs.androidx.compose.material3.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.coroutines.core.v180)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.gson)
    implementation(libs.converter.gson)
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.lifecycle.viewmodel.ktx.v270)
    implementation(libs.androidx.lifecycle.viewmodel.compose.v270)
    implementation(libs.androidx.lifecycle.livedata.ktx.v270)
    implementation(libs.androidx.lifecycle.runtime.ktx.v284)
    implementation(libs.androidx.lifecycle.runtime.compose.v270)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate.v270)
    implementation(libs.androidx.lifecycle.common.java8)
    implementation(libs.androidx.runtime.livedata.v166)
    implementation(libs.coil.compose)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.datetime)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.ui.v166)
    implementation(libs.toolbar.compose)
    implementation(libs.androidx.activity.compose.v191)
    implementation(libs.androidx.compose.ui.ui2)
    implementation(libs.androidx.compose.ui.ui.graphics)
    implementation(libs.androidx.compose.ui.ui.tooling.preview)
    implementation(libs.androidx.appcompat.v161)
    implementation(libs.androidx.core.ktx)
    implementation(libs.play.services.location.v2120)

    implementation (platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)



    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.v121)
    androidTestImplementation(libs.androidx.espresso.core.v361)
    androidTestImplementation(libs.androidx.compose.ui.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.ui.test.manifest)
    testImplementation (libs.kotlinx.coroutines.test)
    testImplementation (libs.mockito.core)
    testImplementation (libs.byte.buddy)
    testImplementation (libs.androidx.core)


}