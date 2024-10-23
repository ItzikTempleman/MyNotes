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
        versionCode = 5
        versionName = "5.0"

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
    options.compilerArgs.addAll(
        listOf(
            "--add-exports=jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED"
        )
    )
}


dependencies {

    //Material
    implementation(libs.material.v1110)
    implementation(libs.androidx.material.v166)
    implementation(libs.androidx.material.v173)
    implementation(libs.androidx.compose.material3.material32)

    //Compose
    implementation(libs.androidx.compose.ui.ui.tooling.preview2)
    implementation(libs.androidx.activity.compose.v191)
    implementation(libs.androidx.compose.ui.ui)
    implementation(libs.androidx.ui.v151)
    implementation(libs.androidx.compose.ui.ui.graphics)
    implementation(libs.androidx.compose.ui.ui.tooling.preview)
    implementation(platform(libs.androidx.compose.bom.v20241000))
    androidTestImplementation(platform(libs.androidx.compose.bom.v20241000))

    //Constraint layout
    implementation(libs.androidx.constraintlayout.compose)

    //Android core
    implementation(libs.androidx.appcompat.v161)
    implementation(libs.androidx.core.ktx)

    //icons
    implementation(libs.androidx.material.icons.core.v173)
    implementation(libs.androidx.material.icons.extended.v173)
    implementation(libs.androidx.material.icons.core.v166)
    implementation(libs.androidx.material.icons.extended.v166)

    //Firebase
    implementation(libs.firebase.auth.ktx)
    implementation(libs.play.services.maps)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(platform(libs.firebase.bom.v3350))
    implementation(libs.google.firebase.analytics)

    //Room
    implementation(libs.androidx.room.ktx)
    annotationProcessor(libs.room.compiler)
    ksp(libs.androidx.room.compiler.v260)

    //Parcel, Serialize, Json, Gson
    implementation(libs.kotlin.parcelize.runtime)
    implementation(libs.gson)
    implementation(libs.converter.gson)
    implementation(libs.kotlinx.serialization.json)

    //Hilt
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android)

    //Coil
    implementation(libs.coil.compose)

    //Navigation
    implementation(libs.androidx.navigation.compose)

    //Coroutines
    implementation(libs.kotlinx.coroutines.core.v180)
    implementation(libs.kotlinx.coroutines.android)

    //Retrofit and interceptor
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)

    //LifeCycle and live data
    implementation(libs.androidx.lifecycle.livedata.ktx.v270)
    implementation(libs.androidx.lifecycle.runtime.ktx.v284)
    implementation(libs.androidx.lifecycle.runtime.compose.v270)

    //Viewmodel
    implementation(libs.androidx.lifecycle.viewmodel.savedstate.v270)
    implementation(libs.androidx.lifecycle.viewmodel.ktx.v270)
    implementation(libs.androidx.lifecycle.viewmodel.compose.v270)

    //Testing
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